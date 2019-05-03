package com.thundersoft.app.documentmanager.util.filemanipulate;

import android.content.Context;

import java.util.List;

public interface FileManipulate {

    //删除对应文件
    boolean deleteFile(String filePath);

    //得到文件属性信息
    List<String> getFileAttribute(String filePath);

    //展示文件属性信息
    void showFileAttribute(List<String> list, Context context);

}
