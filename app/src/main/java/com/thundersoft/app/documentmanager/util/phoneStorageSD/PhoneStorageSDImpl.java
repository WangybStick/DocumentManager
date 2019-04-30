package com.thundersoft.app.documentmanager.util.phoneStorageSD;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;

import java.io.File;

public class PhoneStorageSDImpl implements PhoneStorageSD{

    private Context mContext;

    public PhoneStorageSDImpl(Context context) {
        mContext = context;
    }

    //获取手机SD内存总容量
    @Override
    public String getTotalSize() {
        File path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return Formatter.formatFileSize(mContext, blockSize * totalBlocks);
    }

    //获取手机SD内存可用容量
    @Override
    public String getAvailableSize() {
        File path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return Formatter.formatFileSize(mContext, blockSize * availableBlocks);
    }
}
