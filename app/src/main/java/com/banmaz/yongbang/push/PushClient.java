package com.banmaz.yongbang.push;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import com.google.gson.reflect.TypeToken;
import com.mdroid.lib.core.eventbus.EventBus;
import com.mdroid.lib.core.eventbus.EventBusEvent;
import com.banmaz.yongbang.base.App;
import com.banmaz.yongbang.base.Constants;
import com.banmaz.yongbang.bean.apibean.LinePlan;
import com.banmaz.yongbang.bean.localbean.Ack;
import com.banmaz.yongbang.bean.localbean.LocationInfo;
import com.banmaz.yongbang.bean.localbean.PushMessage;
import com.banmaz.yongbang.bean.localbean.PushType;
import com.banmaz.yongbang.bean.localbean.RouteResult;
import com.banmaz.yongbang.bus.EventType;
import com.banmaz.yongbang.network.HttpClient;
import com.banmaz.yongbang.utils.LogUtils;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import java.lang.reflect.Type;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.LinkedBlockingQueue;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

/**
 * Description: 长连接客户端
 *
 * <p>Created by liuguoquan on 2018/5/14 16:53.
 */
public final class PushClient extends WebSocketListener {
  private static final int WHAT_CONNECT = 100;
  private static final int WHAT_HEART_BEAT = 110;
  private static final int WHAT_HEART_BEAT_CANCEL = 111;
  private static final int WHAT_SEND_LOCATION = 122;
  public static final int QUEUE_SIZE = 60;
  private static PushClient mPushClient = new PushClient();
  private OkHttpClient mOkHttpClient;
  private WebSocket mWebSocket;
  private boolean mQuit = false;
  private boolean mIsConnected = false;
  private int mHeartCount = 0;
  private LinkedBlockingQueue<LocationInfo> mQueue = new LinkedBlockingQueue<>(QUEUE_SIZE);

  private Handler mHandler =
      new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
          super.handleMessage(msg);
          switch (msg.what) {
            case WHAT_CONNECT:
              disConnect();
              connect();
              break;
            case WHAT_HEART_BEAT:
              sendHeartBeat();
              break;
            case WHAT_HEART_BEAT_CANCEL:
              cancelHeartBeat();
              break;
            case WHAT_SEND_LOCATION:
              sendLocation();
              break;
          }
        }
      };
  private Request mRequest;

  public static PushClient getInstance() {
    if (null == mPushClient) {
      mPushClient = new PushClient();
    }
    return mPushClient;
  }

  public void connect() {
    if (isConnected()) {
      return;
    }
    if (!App.getInstance().isLogin()) {
      return;
    }
    try {
      mOkHttpClient = null;
      mOkHttpClient = HttpClient.getWebSocketHClient();
      // ws://echo.websocket.org
      // ws://192.168.1.104:9090
      mRequest =
          new Request.Builder()
              .addHeader("token", App.getInstance().getToken())
              .addHeader("cid", "da")
              .url(Constants.WSS)
              .build();
      mWebSocket = mOkHttpClient.newWebSocket(mRequest, this);
      mOkHttpClient.dispatcher().executorService().shutdown();
      mOkHttpClient.connectionPool().evictAll();
    } catch (Exception e) {
      LogUtils.d("connect>>>", e.getMessage());
      mIsConnected = false;
      mHandler.sendEmptyMessageDelayed(WHAT_CONNECT, 5000);
    }
  }

  public void disConnect() {
    mIsConnected = false;
    if (mWebSocket != null) {
      mWebSocket.close(1000, "客户端主动关闭连接");
    }
  }

  public void send(String text) {
    if (mWebSocket != null && isConnected()) {
      mWebSocket.send(text);
    }
  }

  @Override
  public void onOpen(WebSocket webSocket, Response response) {
    LogUtils.d("onOpen>>>", "code: " + response.code());
    mIsConnected = true;
    mQuit = false;
    mHandler.sendEmptyMessage(WHAT_SEND_LOCATION);
    mHandler.sendEmptyMessage(WHAT_HEART_BEAT);
  }

  @Override
  public void onMessage(WebSocket webSocket, String text) {
    // LogUtils.d("onMessage>>>", text);
    try {
      PushMessage pushMessage = App.getAppGson().fromJson(text, PushMessage.class);
      intercept(pushMessage.type, pushMessage.event, text);
    } catch (RuntimeException e) {
      LogUtils.d("onMessage Failure>>>", e.getMessage());
    }
  }

  @Override
  public void onMessage(WebSocket webSocket, ByteString bytes) {
  }

  @Override
  public void onClosing(WebSocket webSocket, int code, String reason) {
    LogUtils.d("onClosing>>>", "code: " + code + "-- reason: " + reason);
    mIsConnected = false;
    mHandler.sendEmptyMessage(WHAT_HEART_BEAT_CANCEL);
  }

  @Override
  public void onClosed(WebSocket webSocket, int code, String reason) {
    LogUtils.d("onClosed>>>", "code: " + code + "-- reason: " + reason);
    mIsConnected = false;
    mHandler.sendEmptyMessage(WHAT_HEART_BEAT_CANCEL);
  }

  @Override
  public void onFailure(WebSocket webSocket, Throwable t, @Nullable Response response) {
    LogUtils.d(
        "onFailure>>>",
        "reason: "
            + t.getMessage()
            + "--- code: "
            + (response == null ? "" : response.code() + "")
            + " quit: "
            + mQuit);
    mIsConnected = false;
    mHandler.sendEmptyMessage(WHAT_HEART_BEAT_CANCEL);
    if (!mQuit) {
      mHandler.sendEmptyMessageDelayed(WHAT_CONNECT, 5000);
    }
  }

  private void intercept(String type, String event, String msg) {
    if (type.equals(PushType.TYPE_PONG)) {
      PushMessage pushMessage = App.getAppGson().fromJson(msg, PushMessage.class);
      LogUtils.d("receive heart", pushMessage.toString());
      mHeartCount = 0;
    }

    if (type.equals(PushType.TYPE_BUS)) {

      if (event.equals(PushType.EVENT_START)) {
        // doEventStart(msg);
      }

      if (event.equals(PushType.EVENT_GPS_UPLOAD)) {
      }

      if (event.equals(PushType.EVENT_PASSENGER)) {
        doEventPassenger(msg);
      }

      if (event.equals(PushType.EVENT_ROUTE)) {
        doEventRoute(msg);
      }
    }
  }

  private Runnable mUploadRunnable =
      new Runnable() {
        @Override
        public void run() {
          while (!mQuit) {
            if (mWebSocket != null && isConnected()) {
              LocationInfo info = mQueue.poll();
              if (info == null) {
                continue;
              }
              PushMessage message = new PushMessage();
              message.data = info;
              message.type = PushType.TYPE_BUS;
              message.event = PushType.EVENT_GPS_UPLOAD;
              String location;
              try {
                location = App.getAppGson().toJson(message, PushMessage.class);
                mWebSocket.send(location);
                // LogUtils.d("upload GPS>>>", location);
              } catch (RuntimeException e) {
                LogUtils.d("upload GPS: ", e.getMessage());
              }
            }
          }
          LogUtils.d("Upload GPS Stop>>>", Thread.currentThread().getName());
        }
      };

  /** 上报GPS */
  private void sendLocation() {
    if (mQuit) {
      disConnect();
      connect();
    }
    new Thread(mUploadRunnable).start();
  }

  public synchronized void addLocation(LocationInfo locationInfo) {
    Observable.just(locationInfo)
        .subscribeOn(Schedulers.io())
        .subscribe(
            new Consumer<LocationInfo>() {
              @Override
              public void accept(LocationInfo locationInfo) throws Exception {
                if (mQueue.size() >= QUEUE_SIZE) {
                  mQueue.poll();
                } else {
                  mQueue.put(locationInfo);
                }
                // LogUtils.d("add location>>>", mQueue.size() + "");
              }
            },
            new Consumer<Throwable>() {
              @Override
              public void accept(Throwable throwable) throws Exception {
                LogUtils.d("add location>>>", throwable.getMessage());
              }
            });
  }

  private Timer mSenderHeartTimer;

  /** 发送心跳 */
  private void sendHeartBeat() {
    if (mSenderHeartTimer != null) {
      mSenderHeartTimer.cancel();
    }
    mSenderHeartTimer = new Timer();
    mSenderHeartTimer.schedule(
        new TimerTask() {
          @Override
          public void run() {
            if (mWebSocket != null) {
              if (mHeartCount >= 3) {
                mHeartCount = 0;
                mHandler.sendEmptyMessage(WHAT_CONNECT);
              } else {
                PushMessage message = new PushMessage();
                message.type = PushType.TYPE_PING;
                String msg = App.getAppGson().toJson(message, PushMessage.class);
                mWebSocket.send(msg);
                LogUtils.d("send heart>>>", msg + Thread.currentThread().getName());
              }
              mHeartCount++;
            }
          }
        },
        10000,
        10000);
  }

  private void cancelHeartBeat() {
    LogUtils.d("cancelHeartBeat>>>", "");
    if (mSenderHeartTimer != null) {
      mSenderHeartTimer.cancel();
      mSenderHeartTimer = null;
    }
  }

  /**
   * 更新路线
   *
   * @param msg
   */
  private void doEventRoute(String msg) {
    Type type = new TypeToken<PushMessage<RouteResult>>() {
    }.getType();
    PushMessage<RouteResult> pushMessage = App.getAppGson().fromJson(msg, type);
    LogUtils.d("Receive Event Route>>>", pushMessage.toString());
    LogUtils.d("Receive Event Route>>>", pushMessage.data.routeList.toString());
    EventBus.bus().post(new EventBusEvent(EventType.TYPE_ROUTE, pushMessage.data));

    PushMessage reply = new PushMessage();
    reply.type = PushType.TYPE_BUS;
    reply.event = PushType.EVENT_ROUTE;
    Ack ack = new Ack();
    ack.ack = "ok";
    reply.data = ack;
    String replayMsg = App.getAppGson().toJson(reply, PushMessage.class);
    if (mWebSocket != null) {
      mWebSocket.send(replayMsg);
      LogUtils.d("Reply Event Route>>>", replayMsg.toString());
    }
  }

  /**
   * 更新乘客信息
   *
   * @param msg
   */
  private void doEventPassenger(String msg) {
    Type type = new TypeToken<PushMessage<LinePlan>>() {
    }.getType();
    PushMessage<LinePlan> pushMessage = App.getAppGson().fromJson(msg, type);
    LinePlan info = pushMessage.data;
    LogUtils.d("Receive Event Passenger>>>", pushMessage.toString());
    LogUtils.d("Receive Event Passenger>>>>", info.toString());
    EventBus.bus().post(new EventBusEvent(EventType.TYPE_PASSENGER, pushMessage.data));

    PushMessage reply = new PushMessage();
    reply.type = PushType.TYPE_BUS;
    reply.event = PushType.EVENT_PASSENGER;
    Ack ack = new Ack();
    ack.ack = "ok";
    reply.data = ack;
    String replayMsg = App.getAppGson().toJson(reply, PushMessage.class);
    if (mWebSocket != null) {
      mWebSocket.send(replayMsg);
      LogUtils.d("Reply Event Passenger>>>", replayMsg.toString());
    }
  }

  /**
   * 发车
   *
   * @param msg
   */
  private void doEventStart(String msg) {
    PushMessage pushMessage = App.getAppGson().fromJson(msg, PushMessage.class);
    LogUtils.d("Receive Event Start>>>", pushMessage.toString());
    EventBus.bus().post(new EventBusEvent(EventType.TYPE_START, pushMessage));

    PushMessage reply = new PushMessage();
    reply.type = PushType.TYPE_BUS;
    reply.event = PushType.EVENT_START;
    Ack ack = new Ack();
    ack.ack = "ok";
    reply.data = ack;
    String replayMsg = App.getAppGson().toJson(reply, PushMessage.class);
    if (mWebSocket != null) {
      mWebSocket.send(replayMsg);
      LogUtils.d("Reply Event Start>>>", replayMsg.toString());
    }
  }

  /**
   * GPS上传
   *
   * @param msg
   */
  private void doEventGps(String msg) {
    Type type = new TypeToken<PushMessage<LocationInfo>>() {
    }.getType();
    PushMessage<LocationInfo> pushMessage = App.getAppGson().fromJson(msg, type);
    LocationInfo info = pushMessage.data;
    LogUtils.d("Receive Event Gps>>>", pushMessage.toString());
  }

  public void setQuit(boolean mQuit) {
    this.mQuit = mQuit;
  }

  public boolean isQuit() {
    return mQuit;
  }

  public boolean isConnected() {
    return mIsConnected;
  }

  public void destroy() {
    LogUtils.d("destroy>>>", "quit");
    mQuit = true;
    if (mHandler != null) {
      mHandler.sendEmptyMessage(WHAT_HEART_BEAT_CANCEL);
    }
    disConnect();
    mPushClient = null;
  }
}
