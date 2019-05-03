package com.thundersoft.app.documentmanager.util.datamanipulate;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.MediaStore;

import com.thundersoft.app.documentmanager.util.filePathHelper.FilePathHelper;

import java.util.ArrayList;
import java.util.List;

public class DataManipulateImpl implements DataManipulate {

    private FilePathHelper mFilePathHelper;
    private SQLiteDatabase mSQLiteDatabase;
    private Context mContext;

    public DataManipulateImpl (FilePathHelper filePathHelper, SQLiteDatabase mSQLiteDatabase, Context context) {
        this.mFilePathHelper = filePathHelper;
        this.mSQLiteDatabase = mSQLiteDatabase;
        this.mContext = context;
    }


    @Override
    public List<String> queryImagePath() {
        return mFilePathHelper.getAllImagePath();
    }

    @Override
    public List<String> queryVideoPath() {
        return mFilePathHelper.getAllVideoPath();
    }

    @Override
    public List<String> queryAudioPath() {
        return mFilePathHelper.getAllAudioPath();
    }

    @Override
    public List<String> queryDocumentPath() {
        List<String> documentPathList = new ArrayList<>();
        Cursor cursor = mSQLiteDatabase.query("Document",null,null,
                null,null,null,null,null);
        while (cursor.moveToNext()) {
            documentPathList.add(cursor.getString(cursor.getColumnIndex("Document_Uri")));
        }
        cursor.close();
        return documentPathList;
    }

    @Override
    public List<String> queryDownloadPath() {
        List<String> downloadPathList = new ArrayList<>();
        Cursor cursor = mSQLiteDatabase.query("Download",null,null,
                null,null,null,null,null);
        while (cursor.moveToNext()) {
            downloadPathList.add(cursor.getString(cursor.getColumnIndex("Download_Uri")));
        }
        cursor.close();
        return downloadPathList;
    }

    @Override
    public List<String> queryApkPath() {
        List<String> apkPathList = new ArrayList<>();
        Cursor cursor = mSQLiteDatabase.query("Apk",null,null,
                null,null,null,null,null);
        while (cursor.moveToNext()) {
            apkPathList.add(cursor.getString(cursor.getColumnIndex("Apk_Uri")));
        }
        cursor.close();
        return apkPathList;
    }

    @Override
    public void deleteImageItemFromMediaStore(String imagePath) {
        mContext.getContentResolver().delete(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"_data = ?",new String[] {imagePath});
    }

    @Override
    public void deleteVideoItemFromMediaStore(String videoPath) {
        mContext.getContentResolver().delete(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,"_data = ?",new String[] {videoPath});
    }

    @Override
    public void deleteAudioItemFromMediaStore(String audioPath) {
        mContext.getContentResolver().delete(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,"_data = ?",new String[] {audioPath});
    }

    @Override
    public void deleteDocumentItemFromMyDataBase(String documentPath) {
        mSQLiteDatabase.delete("Document", "Document_Uri = ?", new String[] {documentPath});
    }

    @Override
    public void deleteDownloadItemFromMyDataBase(String downloadPath) {
        mSQLiteDatabase.delete("Download","Download_Uri = ?",new String[] {downloadPath});
    }

    @Override
    public void deleteApkItemFromMyDataBase(String apkPath) {
        mSQLiteDatabase.delete("Apk", "Apk_Uri = ?", new String[] {apkPath});
    }

    @Override
    public void renameImageItemFromMediaStore(String imagePath, String newName) {
        ContentValues values = new ContentValues();
        String newPath = imagePath.substring(0, imagePath.lastIndexOf("/") + 1) + newName;
        values.put("_display_name", newName);
        values.put("_data", newPath);
        mContext.getContentResolver().update(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values,
                "_data = ?", new String[] {imagePath});
    }

    @Override
    public void renameVideoItemFromMediaStore(String videoPath, String newName) {
        ContentValues values = new ContentValues();
        String newPath = videoPath.substring(0, videoPath.lastIndexOf("/") + 1) + newName;
        values.put("_display_name", newName);
        values.put("_data", newPath);
        mContext.getContentResolver().update(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values,
                "_data = ?", new String[] {videoPath});
    }

    @Override
    public void renameAudioItemFromMediaStore(String audioPath, String newName) {
        ContentValues values = new ContentValues();
        String newPath = audioPath.substring(0, audioPath.lastIndexOf("/") + 1) + newName;
        values.put("_display_name", newName);
        values.put("_data", newPath);
        mContext.getContentResolver().update(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, values,
                "_data = ?", new String[] {audioPath});
    }

    @Override
    public void renameDocumentItemFromMediaStore(String documentPath, String newName) {
        ContentValues values = new ContentValues();
        String newPath = documentPath.substring(0, documentPath.lastIndexOf("/") + 1) + newName;
        values.put("Document_Uri", newPath);
        mSQLiteDatabase.update("Document", values, "Document_Uri = ?",
                new String[] {documentPath});
    }

    @Override
    public void renameDownloadItemFromMediaStore(String downloadPath, String newName) {
        ContentValues values = new ContentValues();
        String newPath = downloadPath.substring(0, downloadPath.lastIndexOf("/") + 1) + newName;
        values.put("Download_Uri", newPath);
        mSQLiteDatabase.update("Download", values, "Download_Uri = ?",
                new String[] {downloadPath});
    }

    @Override
    public void renameApkItemFromMediaStore(String apkPath, String newName) {
        ContentValues values = new ContentValues();
        String newPath = apkPath.substring(0, apkPath.lastIndexOf("/") + 1) + newName;
        values.put("Apk_Uri", newPath);
        mSQLiteDatabase.update("Apk", values, "Apk_uri = ?", new String[] {apkPath});
    }
}
