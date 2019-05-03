package com.thundersoft.app.documentmanager.util.filePathHelper;

import android.content.Context;

import java.util.List;

public interface FilePathHelper {

    //得到所有图片文件的路径
    List<String> getAllImagePath(Context context);

    //得到所有音频文件的路径
    List<String> getAllVideoPath(Context context);

    //得到所有视频文件的路径
    List<String> getAllAudioPath(Context context);

    //得到所有文档文件的路径和名称
    List<String> getAllDocumentAbsolutePath();
    List<String> getAllDocumentName();

    //得到所有下载文件的路径和名称
    List<String> getAllDownloadAbsolutePath();
    List<String> getAllDownloadName();

    //得到所有Apk文件的路径和名称
    List<String> getAllApkAbsolutePath();
    List<String> getAllApkName();

    //得到最近的四张图片的路径
    List<String> getRecentFourImages(Context context);

    //得到最近一定时间的图片
    List<String> getRecentImage(Context context, int recentTime);

}
