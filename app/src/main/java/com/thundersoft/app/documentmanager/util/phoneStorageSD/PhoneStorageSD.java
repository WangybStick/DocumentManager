package com.thundersoft.app.documentmanager.util.phoneStorageSD;

public interface PhoneStorageSD {

    //获取手机SD内存总容量
    String getTotalSize();

    //获取手机SD内存可用容量
    String getAvailableSize();

}