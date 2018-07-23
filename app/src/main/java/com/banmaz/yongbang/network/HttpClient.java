package com.banmaz.yongbang.network;

/**
 * Description:
 *
 * <p>Created by liuguoquan on 2017/8/7 17:14.
 */

import android.os.Build;
import android.text.TextUtils;
import com.banmaz.yongbang.BuildConfig;
import com.banmaz.yongbang.base.App;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import okhttp3.ConnectionPool;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/** 匿名 SSL HttpClient */
public class HttpClient {
  public static OkHttpClient getDefaultHttpClient() {
    OkHttpClient.Builder builder = new OkHttpClient.Builder();
    builder.connectTimeout(45, TimeUnit.SECONDS);
    builder.writeTimeout(45, TimeUnit.SECONDS);
    builder.readTimeout(45, TimeUnit.SECONDS);
    builder.interceptors().add(new TokenValidationInterceptor());
    builder
        .interceptors()
        .add(
            new Interceptor() {
              @Override
              public Response intercept(Chain chain) throws IOException {
                okhttp3.Request.Builder builder = chain.request().newBuilder();
                builder.addHeader("manufacturer", Build.MANUFACTURER);
                builder.addHeader("device_model", Build.MODEL);
                builder.addHeader("platformId", "android");
                builder.addHeader("os", Build.VERSION.SDK_INT + "");
                String token = App.getInstance().getToken();
                if (!TextUtils.isEmpty(token)) {
                  builder.addHeader("token", token);
                }
                builder.addHeader("app_version", BuildConfig.VERSION_NAME);

                return chain.proceed(builder.build());
              }
            });
    if (BuildConfig.DEBUG) {
      HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
      loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
      builder.interceptors().add(loggingInterceptor);
    }
    try {
      SSLContext sc = SSLContext.getInstance("SSL");
      sc.init(
          null, new TrustManager[] { new TrustAnyTrustManager() },
          new java.security.SecureRandom());
      builder.sslSocketFactory(sc.getSocketFactory());
      builder.hostnameVerifier(new TrustAnyHostnameVerifier());
    } catch (KeyManagementException ignored) {
    } catch (NoSuchAlgorithmException ignored) {
    }
    return builder.build();
  }

  public static OkHttpClient getWebSocketHClient() {
    OkHttpClient.Builder builder = new OkHttpClient.Builder();
    builder.connectTimeout(3, TimeUnit.SECONDS);
    builder.writeTimeout(3, TimeUnit.SECONDS);
    builder.readTimeout(3, TimeUnit.SECONDS);
    builder.retryOnConnectionFailure(false);
    builder.connectionPool(new ConnectionPool(3, 1, TimeUnit.SECONDS));
    if (BuildConfig.DEBUG) {
      HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
      loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
      builder.interceptors().add(loggingInterceptor);
    }
    try {
      SSLContext sc = SSLContext.getInstance("SSL");
      sc.init(
          null, new TrustManager[] { new TrustAnyTrustManager() },
          new java.security.SecureRandom());
      builder.sslSocketFactory(sc.getSocketFactory());
      builder.hostnameVerifier(new TrustAnyHostnameVerifier());
    } catch (KeyManagementException ignored) {
    } catch (NoSuchAlgorithmException ignored) {
    }
    return builder.build();
  }

  private static class TrustAnyTrustManager implements X509TrustManager {
    public void checkClientTrusted(X509Certificate[] chain, String authType)
        throws CertificateException {
    }

    public void checkServerTrusted(X509Certificate[] chain, String authType)
        throws CertificateException {
    }

    public X509Certificate[] getAcceptedIssuers() {
      return new X509Certificate[] {};
    }
  }

  private static class TrustAnyHostnameVerifier implements HostnameVerifier {
    public boolean verify(String hostname, SSLSession session) {
      return true;
    }
  }
}
