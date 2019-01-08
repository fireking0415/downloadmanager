package org.fireking.uugame;

import android.app.Application;

import com.facebook.stetho.Stetho;

import org.fireking.uudownload.DownloadOptions;
import org.fireking.uudownload.UUDownloadManager;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Stetho.initializeWithDefaults(this);

        /**
         * 设置初始化下载器
         */
        DownloadOptions options = new DownloadOptions.Builder(this)
                .setCoreThreadSize(3)
                .create();

        UUDownloadManager.getInstance().init(options);
    }
}
