package org.fireking.uudownload;

public interface SimpleDownloadListener {

    /**
     * 网络请求失败
     */
    void onNetworkError();

    /**
     * 获取网络文件失败
     */
    void onGetNetFileError();

    /**
     * 更新下载进度
     */
    void updateProgress(int progress, String speed);

    /**
     * 正在缓冲中
     */
    void onBufferListener();

    /**
     * 开始下载
     */
    void onDownloadStart();

    /**
     * 暂停任务处理
     */
    void onPauseListener();

    /**
     * 下载完成
     */
    void onCompleteListener();

    /**
     * 等待下载状态
     */
    void onWaitingListener();

    /**
     * 移除等待任务，任务恢复默认状态
     */
    void onNormalListener();
}
