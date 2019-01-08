package org.fireking.uudownload;

import android.text.TextUtils;

import org.fireking.uudownload.greendao.DaoMaster;
import org.fireking.uudownload.greendao.DaoSession;
import org.fireking.uudownload.greendao.DownloadRequestDao;
import org.greenrobot.greendao.database.Database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class UUDownloadManager {

    private static UUDownloadManager sInstance;

    /**
     * 任务调度线程池
     */
    private ThreadPoolExecutor mExecutor;

    /**
     * 下载任务参数
     */
    private DownloadOptions mOption;

    /**
     * 工作任务列表
     */
    private LinkedBlockingQueue<Runnable> mWorkQueues;

    /**
     * okhttp网络请求
     */
    private OkHttpClient mOkHttpClient;

    /**
     * 数据库session
     */
    private DaoSession mDaoSession;

    /**
     * 任务添加数据存贮
     */
    private LinkedHashMap<DownloadRequest, DownloadTask> mWorkTaskMap = new LinkedHashMap<>();

    private UUDownloadManager() {

    }

    public static UUDownloadManager getInstance() {
        if (sInstance == null) {
            synchronized (UUDownloadManager.class) {
                if (sInstance == null) {
                    sInstance = new UUDownloadManager();
                }
            }
        }
        return sInstance;
    }

    /**
     * 在application中初始化
     */
    public void init(DownloadOptions option) {

        mOption = option;

        mExecutor = new ThreadPoolExecutor(mOption.getCoreThreadSize(),
                mOption.getCoreThreadSize(), mOption.getKeepAlive(), mOption.getUnit(),
                new LinkedBlockingQueue<Runnable>());

        mWorkQueues = (LinkedBlockingQueue<Runnable>) mExecutor.getQueue();

        mOkHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor())
                .build();

        DaoMaster.DevOpenHelper openHelper = new DaoMaster.DevOpenHelper(mOption.getContext(),
                mOption.getDownloadDbName());
        Database db = openHelper.getWritableDb();
        DaoMaster daoMaster = new DaoMaster(db);
        mDaoSession = daoMaster.newSession();
    }

    /**
     * 获取数据库操作对象
     */
    public DownloadRequestDao getDao() {
        return mDaoSession.getDownloadRequestDao();
    }

    /**
     * 获取所有的下载任务，包含已经完成的任务
     * 获取的是原始的数据库存贮状态，没有对数据的状态进行处理
     */
    public List<DownloadRequest> getAllDownloadTaskOrigin() {
        return getDao().queryBuilder().list();
    }

    public List<DownloadRequest> getAllDownloadTask() {
        List<DownloadRequest> tasks = getAllDownloadTaskOrigin();
        if (tasks != null && tasks.size() > 0) {
            for (DownloadRequest task : tasks) {
                if (task.getState() == DownloadRequest.STATE_DOWNLOADING
                        || task.getState() == DownloadRequest.STATE_DOWNLOAD_PRE
                        || task.getState() == DownloadRequest.STATE_DOWNLOAD_CONNECTING
                        || task.getState() == DownloadRequest.STATE_DOWNLOAD_INIT) {
                    task.setState(DownloadRequest.STATE_PAUSE);
                }
            }
            return tasks;
        }
        return null;
    }

    /**
     * 进行数据同步，方便数据请求，数据恢复
     */
    public void dataSynchronization(List<DownloadRequest> requestList) {
        List<DownloadRequest> allDownloadTask = UUDownloadManager.getInstance().getAllDownloadTask();
        if (allDownloadTask != null && allDownloadTask.size() > 0) {
            for (DownloadRequest downloadRequest : requestList) {
                for (DownloadRequest downloadTask : allDownloadTask) {
                    if (downloadRequest.equals(downloadTask)) {
                        IOUtils.copy(downloadRequest, downloadTask);
                    }
                }
            }
        }
    }

    /**
     * 暂停指定任务
     */
    public void pauseTask(DownloadRequest request) {
        if (mWorkTaskMap.containsKey(request)) {

            DownloadTask task = mWorkTaskMap.get(request);
            mWorkQueues.remove(task);

            if (task != null) {
                task.stop();
            }
            mWorkTaskMap.remove(request);
        }
    }

    /**
     * 恢复指定任务
     */
    public void resumeTask(DownloadRequest request) {
        this.enqueue(request);
    }

    /**
     * 重试下载任务
     */
    public void retryTask(DownloadRequest request) {
        this.enqueue(request);
    }

    /**
     * 删除下载任务
     */
    public void removeTask(DownloadRequest request) {
        this.pauseTask(request);
        request.setRequestState(DownloadRequest.STATE_NORMAL);
        getDao().delete(request);
    }

    /**
     * 添加任务到队列中
     */
    public void enqueue(DownloadRequest request) {

        //检查是否下载存贮路径可用，不存在，则创建出来一个可用的存贮路径
        if (IOUtils.hasDirectory(mOption.getCachePath())) {
            if (!TextUtils.isEmpty(request.getDownloadUrl())
                    && !mWorkTaskMap.containsKey(request)) {

                DownloadRequest downloadRequest = getDao().queryBuilder()
                        .where(DownloadRequestDao.Properties.DownloadUrl.eq(request.getDownloadUrl()))
                        .build()
                        .unique();

                //如果数据库中已经存在该条数据，则直接获取该数据，进行一次拷贝,如果不存在的话，直接插入数据
                if (downloadRequest == null) {
                    request.setStoreDirection(mOption.getCachePath());

                    //向数据库中插入一条下载数据
                    long rowId = getDao().insert(request);
                    request.setId(rowId);
                } else {
                    IOUtils.copy(request, downloadRequest);
                }

                request.setRequestState(DownloadRequest.STATE_DOWNLOAD_INIT);
                DownloadTask task = new DownloadTask(request);
                mWorkTaskMap.put(request, task);
                mExecutor.execute(task);
            }
        } else {
            LogUtils.e("文件下载目录创建失败");
            request.setRequestState(DownloadRequest.STATE_FILE_ERROR);
        }
    }

    public LinkedHashMap<DownloadRequest, DownloadTask> getWorkTaskMap() {
        return mWorkTaskMap;
    }

    public OkHttpClient getOkHttpClient() {
        if (mOkHttpClient == null) {
            throw new ExceptionInInitializerError("DownloadManager对象未能正常初始化，" +
                    "请在Application中调用init()进行初始化");
        }
        return mOkHttpClient;
    }
}
