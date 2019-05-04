package com.thundersoft.app.documentmanager.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.thundersoft.app.documentmanager.util.filePathHelper.FilePathHelper;
import com.thundersoft.app.documentmanager.util.filePathHelper.FilePathHelperImpl;
import java.util.List;

public class MyDataBaseManager {

    private static final String PHONE_INTERNAL_STORAGE_ROOT_DIRECTORY = "/storage/emulated/0";
    private static final String DOCUMENT_URI = "Document_Uri";
    private static final String DOCUMENT_NAME = "Document_name";
    private static final String DOCUMENT = "Document";
    private static final String DOWNLOAD_URI = "Download_Uri";
    private static final String DOWNLOAD_NAME = "Download_name";
    private static final String DOWNLOAD = "Download";
    private static final String APK_URI = "Apk_Uri";
    private static final String APK_NAME = "Apk_Name";
    private static final String APK = "Apk";
    private SQLiteDatabase mSQLiteDatabase;
    private MyDatabaseHelper mMyDatabaseHelper;
    private ContentValues mContentValues;
    private FilePathHelper mFilePathHelper;
    private Context mContext;
    private List<String> mDocumentPathList;
    private List<String> mDocumentNameList;
    private List<String> mDownloadPathList;
    private List<String> mDownloadNameList;
    private List<String> mApkPathList;
    private List<String> mApkNameList;

    //封装一个可读写的数据库
    public MyDataBaseManager(Context context) {
        mMyDatabaseHelper = new MyDatabaseHelper(context);
        mSQLiteDatabase = mMyDatabaseHelper.getWritableDatabase();
        mContentValues = new ContentValues();
        mContext = context;
    }

    //得到一个可读写的数据库
    public SQLiteDatabase getmSQLiteDatabase() {
        return mSQLiteDatabase;
    }

    //初始化数据库的三张表
    public boolean initMyDataBase() {
        mFilePathHelper = new FilePathHelperImpl();
        ((FilePathHelperImpl) mFilePathHelper).getAllSystemFileList(PHONE_INTERNAL_STORAGE_ROOT_DIRECTORY,
                mContext);
        mDocumentPathList = mFilePathHelper.getAllDocumentAbsolutePath();
        mDocumentNameList = mFilePathHelper.getAllDocumentName();
        for (int i = 0; i < mDocumentPathList.size(); i++) {
            insertDocument(mDocumentPathList.get(i), mDocumentNameList.get(i));
        }
        mDownloadPathList = mFilePathHelper.getAllDownloadAbsolutePath();
        mDownloadNameList = mFilePathHelper.getAllDownloadName();
        for (int i = 0; i < mDownloadPathList.size(); i++) {
            insertDownLoad(mDownloadPathList.get(i), mDownloadNameList.get(i));
        }
        mApkPathList = mFilePathHelper.getAllApkAbsolutePath();
        mApkNameList = mFilePathHelper.getAllApkName();
        for (int i = 0; i < mApkPathList.size(); i++) {
            insertApk(mApkPathList.get(i), mApkNameList.get(i));
        }
        return true;
    }

    //将手机内部存储根目录下的文档文件加入到数据库表
    public boolean insertDocument(String documentUri, String documentName) {
        mContentValues.put(DOCUMENT_URI, documentUri);
        mContentValues.put(DOCUMENT_NAME, documentName);
        mSQLiteDatabase.insert(DOCUMENT, null, mContentValues);
        mContentValues.clear();
        return true;
    }

    //将手机内部存储根目录下的下载文件加入到数据库表
    public boolean insertDownLoad(String downLoadUri, String downLoadName) {
        mContentValues.put(DOWNLOAD_URI, downLoadUri);
        mContentValues.put(DOWNLOAD_NAME, downLoadName);
        mSQLiteDatabase.insert(DOWNLOAD, null, mContentValues);
        mContentValues.clear();
        return true;
    }

    //将手机内部存储根目录下的下载文件加入到数据库表
    public boolean insertApk(String apkUri, String apkName) {
        mContentValues.put(APK_URI, apkUri);
        mContentValues.put(APK_NAME, apkName);
        mSQLiteDatabase.insert(APK, null, mContentValues);
        mContentValues.clear();
        return true;
    }

}
