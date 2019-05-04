package com.thundersoft.app.documentmanager.util.datamanipulate;

import java.util.List;

public interface DataManipulate {

    //查找所有图片文件的路径
    List<String> queryImagePath();

    //从MediaStore数据库中删除对应图片
    void deleteImageItemFromMediaStore(String imagePath);

    //从MediaStore数据库中修改图片名称
    void renameImageItemFromMediaStore(String imagePath, String newName);

    //查找所有视频文件的路径
    List<String> queryVideoPath();

    //从MediaStore数据库中删除对应视频
    void deleteVideoItemFromMediaStore(String videoPath);

    //从MediaStore数据库中修改视频名称
    void renameVideoItemFromMediaStore(String videoPath, String newName);

    //查找所有音频文件的路径
    List<String> queryAudioPath();

    //从MediaStore数据库中删除对应音频
    void deleteAudioItemFromMediaStore(String audioPath);

    //从MediaStore数据库中修改音频名称
    void renameAudioItemFromMediaStore(String audioPath, String newName);

    //查找所有文档文件的路径
    List<String> queryDocumentPath();

    //从自定义数据库中删除对应文档
    void deleteDocumentItemFromMyDataBase(String documentPath);

    //从自定义数据库中修改文档名称
    void renameDocumentItemFromMediaStore(String documentPath, String newName);

    //查找所有下载文件的路径
    List<String> queryDownloadPath();

    //从自定义数据库中删除对应下载文件
    void deleteDownloadItemFromMyDataBase(String downloadPath);

    //从自定义数据库中修改下载文件名称
    void renameDownloadItemFromMediaStore(String downloadPath, String newName);

    //查找所有Apk文件的路径
    List<String> queryApkPath();

    //从自定义数据库中删除对应Apk
    void deleteApkItemFromMyDataBase(String apkPath);

    //从自定义数据库中修改Apk名称
    void renameApkItemFromMediaStore(String apkPath, String newName);
}
