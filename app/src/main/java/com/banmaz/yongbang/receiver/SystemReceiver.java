package com.banmaz.yongbang.receiver;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import com.mdroid.DBUtils;
import com.banmaz.yongbang.base.DBKeys;
import java.io.File;

/**
 * Description:
 *
 * <p>Created by liuguoquan on 2017/12/5 17:45.
 */
public class SystemReceiver extends BroadcastReceiver {
  @Override
  public void onReceive(Context context, Intent intent) {
    if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(intent.getAction())) {
      DownloadManager manager =
          (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
      long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
      if (id != 0 && id == DBUtils.read(DBKeys.UPDATE_APK_ID, 0L)) {
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(id);
        Cursor cursor = manager.query(query);
        if (cursor != null && cursor.moveToFirst()) {
          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Intent startIntent = new Intent(Intent.ACTION_VIEW);
            startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            String name = DBUtils.read(DBKeys.UPDATE_APK_NAME);
            Uri contentUri =
                FileProvider.getUriForFile(
                    context,
                    "net.sibat.ydbus.fileProvider",
                    new File(
                        Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_DOWNLOADS),
                        name));
            startIntent.setDataAndType(contentUri, "application/vnd.android.package-archive");
            context.startActivity(startIntent);
          } else {
            String filename =
                cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME));
            if (!TextUtils.isEmpty(filename)) {
              Intent startIntent = new Intent(Intent.ACTION_VIEW);
              startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
              startIntent.setDataAndType(
                  Uri.parse("file://" + filename), "application/vnd.android.package-archive");
              context.startActivity(startIntent);
            }
          }
        }
      }
    }
  }
}
