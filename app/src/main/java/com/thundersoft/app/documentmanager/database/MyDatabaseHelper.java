package com.thundersoft.app.documentmanager.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class MyDatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_Name = "myFile.db";
    private static final int version = 2;
    public static final String CREATE_IMAGE = "create table Image ("
            + "id integer primary key autoincrement, "
            + "Image_name text,"
            + "Image_Uri text)";
    public static final String CREATE_VIDEO = "create table Video ("
            + "id integer primary key autoincrement, "
            + "Video_name text,"
            + "Video_Uri text)";
    public static final String CREATE_AUDIO = "create table Audio ("
            + "id integer primary key autoincrement, "
            + "Audio_name text,"
            + "Audio_Uri text)";
    public static final String CREATE_DOCUMENT = "create table Document ("
            + "id integer primary key autoincrement, "
            + "Document_name text,"
            + "Document_Uri text)";
    public static final String CREATE_DOWNLOAD = "create table DownLoad ("
            + "id integer primary key autoincrement, "
            + "DownLoad_name text,"
            + "DownLoad_Uri text)";
    public static final String CREATE_APK= "create table Apk ("
            + "id integer primary key autoincrement, "
            + "Apk_name text,"
            + "Apk_uri text)";

    public MyDatabaseHelper(Context context) {
        //public SQLiteOpenHelper(Context context, String name, CusorFactory factory, int version)
        super(context, DB_Name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_IMAGE);
        db.execSQL(CREATE_VIDEO);
        db.execSQL(CREATE_AUDIO);
        db.execSQL(CREATE_DOCUMENT);
        db.execSQL(CREATE_DOWNLOAD);
        db.execSQL(CREATE_APK);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}
