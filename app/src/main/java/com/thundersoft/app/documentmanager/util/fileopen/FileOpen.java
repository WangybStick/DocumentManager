package com.thundersoft.app.documentmanager.util.fileopen;

import android.content.Intent;

public interface FileOpen {

    //根据文件路径得到打开相应的文件intent
    Intent getOpenFileIntent(String filePath);

}
