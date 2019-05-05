package com.thundersoft.app.documentmanager.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    private static MyDatabaseHelper singleton;

    //数据库相关信息定义
    private static final String DB_Name = "myFile.db";
    private static final int version = 2;
    private static final String CREATE_DOCUMENT = "create table Document ("
            + "id integer primary key autoincrement, "
            + "Document_name text,"
            + "Document_Uri text)";
    private static final String CREATE_DOWNLOAD = "create table DownLoad ("
            + "id integer primary key autoincrement, "
            + "Download_name text,"
            + "Download_Uri text)";
    private static final String CREATE_APK= "create table Apk ("
            + "id integer primary key autoincrement, "
            + "Apk_name text,"
            + "Apk_Uri text)";

    private MyDatabaseHelper(Context context) {
        super(context, DB_Name, null, version);
    }

    public static MyDatabaseHelper getSingleton(Context context) {
        if (singleton == null) {
            singleton = new MyDatabaseHelper(context);
            return singleton;
        } else {
            return singleton;
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DOCUMENT);
        db.execSQL(CREATE_DOWNLOAD);
        db.execSQL(CREATE_APK);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists Document");
        db.execSQL("drop table if exists Download");
        db.execSQL("drop table if exists Apk");
        onCreate(db);
    }

}
