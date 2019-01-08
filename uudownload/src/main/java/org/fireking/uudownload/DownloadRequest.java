package org.fireking.uudownload;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity(nameInDb = "download_entity")
public class DownloadRequest {

    /**
     * 未处理状态
     */
    @Transient
    public static final int STATE_NORMAL = 0;

    /**
     * 下载中
     */
    @Transient
    public static final int STATE_DOWNLOADING = 1;

    /**
     * 下载完成
     */
    @Transient
    public static final int STATE_COMPLETED = 2;

    /**
     * 下载暂停
     */
    @Transient
    public static final int STATE_PAUSE = 3;

    /**
     * 文件操作失败
     */
    @Transient
    public static final int STATE_FILE_ERROR = 6;

    /**
     * 网络出错
     */
    @Transient
    public static final int STATE_NETWORK_ERROR = 7;

    /**
     * 获取文件大小失败
     */
    @Transient
    public static final int STATE_FILE_LENGTH_ERROR = 8;

    /**
     * 准备下载
     */
    @Transient
    public static final int STATE_DOWNLOAD_PRE = 9;

    /**
     * 资源连接中
     */
    @Transient
    public static final int STATE_DOWNLOAD_CONNECTING = 10;

    /**
     * 准备下载状态
     */
    @Transient
    public static final int STATE_DOWNLOAD_INIT = 11;

    @Id(autoincrement = true)
    @Property(nameInDb = "download_id")
    private Long id;
    /**
     * 下载地址
     */
    @Property(nameInDb = "download_url")
    private String downloadUrl;

    /**
     * 下载文件的md5值
     */
    @Property(nameInDb = "md5")
    private String md5;

    /**
     * 下载链接的hash值
     */
    @Property(nameInDb = "hash_code")
    private int hashCode;

    /**
     * 任务下载状态
     */
    @Property(nameInDb = "state")
    private int state;

    /**
     * 文件中的大小
     */
    @Property(nameInDb = "content_length")
    private long contentLength;

    /**
     * 文件下载大小
     */
    @Property(nameInDb = "current_length")
    private long currentLength;

    /**
     * 存贮路径
     */
    @Property(nameInDb = "store_direction")
    private String storeDirection;

    /**
     * 文件存贮路径
     */
    @Property(nameInDb = "store_file_name")
    private String storeFileName;

    @Transient
    private static final int DOWNLOAD_REFRESH = 0x101;

    /**
     * 任务下载监听
     */
    @Transient
    private List<SimpleDownloadListener> downloadListeners = new ArrayList<>();

    public DownloadRequest() {
    }

    public DownloadRequest(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    @Generated(hash = 965542234)
    public DownloadRequest(Long id, String downloadUrl, String md5, int hashCode, int state, long contentLength,
                           long currentLength, String storeDirection, String storeFileName) {
        this.id = id;
        this.downloadUrl = downloadUrl;
        this.md5 = md5;
        this.hashCode = hashCode;
        this.state = state;
        this.contentLength = contentLength;
        this.currentLength = currentLength;
        this.storeDirection = storeDirection;
        this.storeFileName = storeFileName;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public DownloadRequest setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
        return this;
    }

    public String getMd5() {
        return md5;
    }

    public DownloadRequest setMd5(String md5) {
        this.md5 = md5;
        return this;
    }

    public int getState() {
        return state;
    }

    public DownloadRequest setRequestState(int state) {
        this.state = state;
        UUDownloadManager.getInstance().getDao().update(this);
        refreshDownloadState();
        return this;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Transient
    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == DOWNLOAD_REFRESH && downloadListeners != null && downloadListeners.size() > 0) {
                switch (state) {
                    case STATE_FILE_LENGTH_ERROR:
                        for (SimpleDownloadListener l1 : downloadListeners) {
                            l1.onGetNetFileError();
                        }
                        break;
                    case STATE_NETWORK_ERROR:
                        for (SimpleDownloadListener l2 : downloadListeners) {
                            l2.onNetworkError();
                        }
                        break;
                    case STATE_DOWNLOADING:
                        for (SimpleDownloadListener l3 : downloadListeners) {
                            l3.updateProgress((int) (currentLength * 1F / contentLength * 100), getSpeed());
                        }
                        break;
                    case STATE_DOWNLOAD_PRE:
                        for (SimpleDownloadListener l4 : downloadListeners) {
                            l4.onDownloadStart();
                        }
                        break;
                    case STATE_DOWNLOAD_CONNECTING:
                        for (SimpleDownloadListener l5 : downloadListeners) {
                            l5.onBufferListener();
                        }
                        break;
                    case STATE_PAUSE:
                        for (SimpleDownloadListener l6 : downloadListeners) {
                            l6.onPauseListener();
                        }
                        break;
                    case STATE_COMPLETED:
                        for (SimpleDownloadListener l7 : downloadListeners) {
                            l7.onCompleteListener();
                        }
                        break;
                    case STATE_DOWNLOAD_INIT:
                        for (SimpleDownloadListener l8 : downloadListeners) {
                            l8.onWaitingListener();
                        }
                        break;
                    case STATE_NORMAL:
                        for (SimpleDownloadListener l9 : downloadListeners) {
                            l9.onNormalListener();
                        }
                        break;
                }
            }
        }
    };

    /**
     * 获取当前的下载速度
     */
    private String getSpeed() {
        long speed = contentLength - currentLength;
        if (speed > 1024) {
            if (speed > 1024 * 1024) {
                BigDecimal decimal = new BigDecimal(speed / 1024 * 1F / 1024 * 1F)
                        .setScale(2, BigDecimal.ROUND_HALF_UP);
                return decimal.toString() + "mb/s";
            } else {
                long currentSpeed = speed / 1024;
                return currentSpeed + "kb/s";
            }
        } else {
            return speed + "b/s";
        }
    }

    private void refreshDownloadState() {
        handler.obtainMessage(DOWNLOAD_REFRESH).sendToTarget();
    }

    public long getContentLength() {
        return contentLength;
    }

    public void setContentLength(long contentLength) {
        this.contentLength = contentLength;
    }

    public long getCurrentLength() {
        return currentLength;
    }

    public void refreshCurrentLength(long currentLength) {
        this.currentLength = currentLength;
        UUDownloadManager.getInstance().getDao().update(this);
        refreshDownloadState();
    }

    public void setCurrentLength(long currentLength) {
        this.currentLength = currentLength;
    }

    public String getStoreDirection() {
        return storeDirection;
    }

    public void setStoreDirection(String storeDirection) {
        this.storeDirection = storeDirection;
    }

    public String getStoreFileName() {
        return storeFileName;
    }

    public DownloadRequest setStoreFileName(String storeFileName) {
        this.storeFileName = storeFileName;
        return this;
    }

    public void setDownloadListener(SimpleDownloadListener downloadListener) {
        this.downloadListeners.add(downloadListener);
    }

    public int getHashCode() {
        return hashCode;
    }

    public void setHashCode(int hashCode) {
        this.hashCode = hashCode;
    }

    public int getProgress() {
        return (int) (currentLength * 1F / contentLength * 100);
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DownloadRequest that = (DownloadRequest) o;

        return downloadUrl != null ? downloadUrl.equals(that.downloadUrl) : that.downloadUrl == null;
    }

    @Override
    public int hashCode() {
        return downloadUrl != null ? downloadUrl.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "DownloadRequest{" +
                "id=" + id +
                ", downloadUrl='" + downloadUrl + '\'' +
                ", md5='" + md5 + '\'' +
                ", hashCode=" + hashCode +
                ", state=" + state +
                ", contentLength=" + contentLength +
                ", currentLength=" + currentLength +
                ", storeDirection='" + storeDirection + '\'' +
                ", storeFileName='" + storeFileName + '\'' +
                '}';
    }
}
