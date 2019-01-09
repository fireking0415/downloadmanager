## 多任务下载管理器

支持多任务单线程下载，使用线程池管理任务。

切记在application中注册

```简单使用

/**
 * 设置初始化下载器
 */
DownloadOptions options = new DownloadOptions.Builder(this)
        .setCoreThreadSize(3)
        .create();

UUDownloadManager.getInstance().init(options);
```

使用```UUDownloadManager```管理队列的入栈出栈

### 常用方法

* UUDownloadManager.getInstance().dataSynchronization(requestList);
使用该方法同步数据库数据和本地数据

* getAllDownloadTask
获取任务，该任务是已经处理了用户在下载状态手机直接从进程清除app进程之后的数据
* public void pauseTask(DownloadRequest request)
暂停任务
* public void resumeTask(DownloadRequest request)
恢复任务请求
* public void retryTask(DownloadRequest request)
重试任务请求
* public void removeTask(DownloadRequest request)
移除任务请求
* public void enqueue(DownloadRequest request)
任务添加到队列
* DownloadRequest#public void setDownloadListener(SimpleDownloadListener downloadListener)
设置请求监听，该方法是可以多次调用的，在不同界面设置，处理具体逻辑，可以实现多页面同时刷新


数据错乱问题，按照常规处理，使用tag进行处理就行了，使用参见demo


其他具体使用，参见demo