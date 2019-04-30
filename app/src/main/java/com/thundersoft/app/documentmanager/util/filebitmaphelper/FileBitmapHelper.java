package com.thundersoft.app.documentmanager.util.filebitmaphelper;

import android.content.Context;
import android.graphics.Bitmap;

public interface FileBitmapHelper {

    //获取不同格式的文件的对应的小图片
    Bitmap getFileBitmap(Context context, String filePath, final boolean isDefault);

}
