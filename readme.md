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

其他使用，参见demo