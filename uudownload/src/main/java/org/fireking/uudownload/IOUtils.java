package org.fireking.uudownload;

import android.support.annotation.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * <p>
 * <h1>io文件操作</h1>
 * </p>
 */
public class IOUtils {

    /**
     * 判断存贮路径是否存在，如果不存在，则创建路径，
     * 创建失败，返回false，创建成功返回ture
     */
    public static boolean hasDirectory(String cachePath) {
        File directory = new File(cachePath);
        if (directory.exists()) {
            return true;
        }
        return directory.mkdirs();
    }

    /**
     * 关闭输入输出流
     */
    public static void closeIO(@Nullable InputStream ins, @Nullable FileOutputStream fos) {
        try {
            if (ins != null) {
                ins.close();
            }
            if (fos != null) {
                fos.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 进行下载数据拷贝
     */
    public static void copy(DownloadRequest src, DownloadRequest dist) {
        src.setDownloadUrl(dist.getDownloadUrl());
        src.setContentLength(dist.getContentLength());
        src.setCurrentLength(dist.getCurrentLength());
        src.setId(dist.getId());
        src.setMd5(dist.getMd5());
        src.setState(dist.getState());
        src.setStoreDirection(dist.getStoreDirection());
        src.setStoreFileName(dist.getStoreFileName());
        src.setHashCode(dist.getHashCode());
    }
}
