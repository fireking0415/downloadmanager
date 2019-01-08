package org.fireking.uudownload;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * <h1>下载配置项目</h1>
 * </p>
 */
public class DownloadOptions {

    /**
     * 主工作线程数量
     */
    private int coreThreadSize;

    /**
     * 下载存贮位置
     */
    private String cachePath;

    /**
     * 数据存贮数据库名称
     */
    private String downloadDbName;

    /**
     * 设置线程过期时间
     */
    private long keepAlive;

    /**
     * 设置线程过期时间单位
     */
    private TimeUnit unit;

    private Context context;

    private DownloadOptions(Context context) {
        this.context = context;
    }

    public static class Builder {

        DownloadOptions mOptions;

        public Builder(Context context) {
            this.mOptions = new DownloadOptions(context);
        }

        /**
         * 设置主工作线程大小
         */
        public Builder setCoreThreadSize(int coreThreadSize) {
            mOptions.coreThreadSize = coreThreadSize;
            return this;
        }

        /**
         * 设置存贮位置
         */
        public Builder setCachePath(String cachePath) {
            mOptions.cachePath = cachePath;
            return this;
        }

        /**
         * 设置数据库名称
         */
        public Builder setDownloadDbName(String dbName) {
            mOptions.downloadDbName = dbName;
            return this;
        }

        /**
         * 设置线程过期时间
         */
        public Builder setKeepAlive(long keepAlive, TimeUnit timeUnit) {
            mOptions.keepAlive = keepAlive;
            mOptions.unit = timeUnit;
            return this;
        }

        public DownloadOptions create() {

            // 如果主工作线程没有设置，默认设置为1个
            if (mOptions.coreThreadSize < 1) {
                mOptions.coreThreadSize = 1;
            }

            // 如果没有设置默认存贮路径,则设置为自己的存贮路径
            if (TextUtils.isEmpty(mOptions.cachePath)) {
                mOptions.cachePath = mOptions.context.getFilesDir().getAbsolutePath()
                        + File.separator + "download";
            }

            //如果没有设置数据库下载地址，则设置为uu_download.db
            if (TextUtils.isEmpty(mOptions.downloadDbName)) {
                mOptions.downloadDbName = "uu_download.db";
            }

            //如果没有设置线程空闲回收时间，则设置为60秒后自动回收
            if (mOptions.keepAlive == 0) {
                mOptions.keepAlive = 60L;
                mOptions.unit = TimeUnit.SECONDS;
            }

            return mOptions;
        }
    }

    public long getKeepAlive() {
        return keepAlive;
    }

    public TimeUnit getUnit() {
        return unit;
    }

    public int getCoreThreadSize() {
        return coreThreadSize;
    }

    public String getCachePath() {
        return cachePath;
    }

    public String getDownloadDbName() {
        return downloadDbName;
    }

    public Context getContext() {
        return context;
    }

    public static DownloadOptions getDefault(Context context) {
        return new DownloadOptions.Builder(context).create();
    }
}
