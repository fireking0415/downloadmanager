package org.fireking.uudownload;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * <p>
 * <h1>下载真实任务类</h1>
 * </p>
 */
public class DownloadTask implements Runnable {

    private DownloadRequest mRequest;
    private OkHttpClient mClient;

    /**
     * 是否暂停当前任务
     */
    private volatile boolean isStopTask = false;

    public DownloadTask(DownloadRequest request) {
        this.mRequest = request;
        mClient = UUDownloadManager.getInstance().getOkHttpClient();
    }

    @Override
    public void run() {

        //总的文件大小
        long contentLength = mRequest.getContentLength();

        //已经下载的文件大小
        long currentLength = mRequest.getCurrentLength();

        if (contentLength <= 0) {

            mRequest.setRequestState(DownloadRequest.STATE_DOWNLOAD_CONNECTING);

            long fileContentLength = getContentLength();

            LogUtils.d("文件大小为: " + fileContentLength);

            if (fileContentLength <= 0) {
                if (fileContentLength == -100) {
                    mRequest.setRequestState(DownloadRequest.STATE_NETWORK_ERROR);
                } else {
                    mRequest.setRequestState(DownloadRequest.STATE_FILE_LENGTH_ERROR);
                }
                UUDownloadManager.getInstance().pauseTask(mRequest);
                return;
            }

            contentLength = fileContentLength;
            mRequest.setContentLength(contentLength);
        }

        if (isStopTask) {
            mRequest.setRequestState(DownloadRequest.STATE_PAUSE);
            return;
        }

        mRequest.setRequestState(DownloadRequest.STATE_DOWNLOAD_PRE);

        //开始文件下载操作
        Request downloadRequest = new Request.Builder()
                .addHeader("RANGE", "bytes=" + currentLength + "-" + contentLength)
                .url(mRequest.getDownloadUrl())
                .build();
        Call call = mClient.newCall(downloadRequest);

        File storeFile = new File(mRequest.getStoreDirection(), mRequest.getStoreFileName());
        InputStream ins = null;
        FileOutputStream fos = null;
        try {
            Response response = call.execute();

            //开始下载状态
            mRequest.setRequestState(DownloadRequest.STATE_DOWNLOADING);

            if (response.isSuccessful()) {
                if (response.body() != null) {
                    ins = response.body().byteStream();
                    fos = new FileOutputStream(storeFile, true);
                    byte[] buffer = new byte[2048];
                    int len;
                    while ((len = ins.read(buffer)) != -1) {
                        if (isStopTask) {
                            mRequest.setRequestState(DownloadRequest.STATE_PAUSE);
                            break;
                        }
                        fos.write(buffer, 0, len);
                        currentLength += len;
                        mRequest.refreshCurrentLength(currentLength);
                    }
                }
                if (fos != null) {
                    fos.flush();
                }
                if (!isStopTask && currentLength == contentLength) {
                    mRequest.setRequestState(DownloadRequest.STATE_COMPLETED);
                }
            } else {
                mRequest.setRequestState(DownloadRequest.STATE_NETWORK_ERROR);
                UUDownloadManager.getInstance().removeTask(mRequest);
                LogUtils.e("网络失败");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeIO(ins, fos);
        }
    }

    /**
     * 获取文件大小
     */
    private long getContentLength() {
        Request request = new Request.Builder()
                .url(mRequest.getDownloadUrl())
                .build();

        long currentLength = -1;

        try {
            Response response = mClient.newCall(request).execute();
            if (response.isSuccessful()) {
                if (response.body() != null) {
                    currentLength = response.body().contentLength();
                }
                response.close();
            } else {
                currentLength = -100;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return currentLength;
    }

    /**
     * 停止任务
     */
    public void stop() {
        isStopTask = true;
    }
}
