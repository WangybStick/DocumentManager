package com.thundersoft.app.documentmanager.util.fileopen;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import android.util.Log;

import java.io.File;

public class FileOpenImpl implements FileOpen {

    private Context mContext;

    public FileOpenImpl(Context context) {
        mContext = context;
    }

    @Override
    public Intent getOpenFileIntent(String filePath) {
        File file = new File(filePath);
        if(!file.exists()) return null;
        /* 取得扩展名 */
        String end=file.getName().substring(file.getName().lastIndexOf(".") + 1,file.getName().length()).toLowerCase();
        /* 依扩展名的类型决定MimeType */
        if(end.equals("m4a")||end.equals("mp3")||end.equals("mid")||
                end.equals("xmf")||end.equals("ogg")||end.equals("wav")||end.equals("amr")){
            return getAudioFileIntent(filePath);
        }else if(end.equals("3gp")||end.equals("mp4")||end.equals("flv")){
            return getVideoFileIntent(filePath);
        }else if(end.equals("jpg")||end.equals("gif")||end.equals("png")||
                end.equals("jpeg")||end.equals("bmp")){
            return getImageFileIntent(filePath);
        }else if(end.equals("apk")){
            return getApkFileIntent(filePath);
        }else if(end.equals("ppt")||end.equals("doc")){
            return getPptFileIntent(filePath);
        }else if(end.equals("xls")){
            return getExcelFileIntent(filePath);
        }else if(end.equals("pdf")){
            return getPdfFileIntent(filePath);
        }else if(end.equals("chm")){
            return getChmFileIntent(filePath);
        }else if(end.equals("txt")){
            return getTextFileIntent(filePath,false);
        }else{
            return getAllIntent(filePath);
        }
    }

    //Android获取一个用于打开APK文件的intent
    public Intent getAllIntent(String param) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        File file = new File(param );
        Uri uri = FileProvider.getUriForFile(mContext,mContext.getApplicationContext().getPackageName() + ".provider", file);
        intent.setDataAndType(uri,"*/*");
        return intent;
    }

    //Android获取一个用于打开APK文件的intent
    public Intent getApkFileIntent( String param ) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        File file = new File(param );
        Uri uri = FileProvider.getUriForFile(mContext,mContext.getApplicationContext().getPackageName() + ".provider", file);
        intent.setDataAndType(uri,"application/vnd.android.package-archive");
        return intent;
    }

    //Android获取一个用于打开VIDEO文件的intent
    public Intent getVideoFileIntent( String param ) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        File file = new File(param );
        Uri uri = FileProvider.getUriForFile(mContext,mContext.getApplicationContext().getPackageName() + ".provider", file);
        intent.setDataAndType(uri, "video/*");
        return intent;
    }

    //Android获取一个用于打开AUDIO文件的intent
    public Intent getAudioFileIntent( String param ){

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        File file = new File(param );
        Uri uri = FileProvider.getUriForFile(mContext,mContext.getApplicationContext().getPackageName() + ".provider", file);
        intent.setDataAndType(uri, "audio/*");
        return intent;
    }

    //Android获取一个用于打开图片文件的intent
    public Intent getImageFileIntent(String param ) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        File file = new File(param);
        Uri uri = FileProvider.getUriForFile(mContext,mContext.getApplicationContext().getPackageName() + ".provider", file);
        intent.setDataAndType(uri, "image/*");
        return intent;
    }

    //Android获取一个用于打开PPT文件的intent
    public Intent getPptFileIntent( String param ){

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        File file = new File(param );
        Uri uri = FileProvider.getUriForFile(mContext,mContext.getApplicationContext().getPackageName() + ".provider", file);        intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
        return intent;
    }

    //Android获取一个用于打开Excel文件的intent
    public Intent getExcelFileIntent( String param ){

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        File file = new File(param );
        Uri uri = FileProvider.getUriForFile(mContext,mContext.getApplicationContext().getPackageName() + ".provider", file);
        intent.setDataAndType(uri, "application/vnd.ms-excel");
        return intent;
    }

    //Android获取一个用于打开CHM文件的intent
    public Intent getChmFileIntent( String param ){

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        File file = new File(param );
        Uri uri = FileProvider.getUriForFile(mContext,mContext.getApplicationContext().getPackageName() + ".provider", file);        intent.setDataAndType(uri, "application/x-chm");
        return intent;
    }

    //Android获取一个用于打开文本文件的intent
    public Intent getTextFileIntent( String param, boolean paramBoolean){

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (paramBoolean){
            File file = new File(param );
            Uri uri1 = FileProvider.getUriForFile(mContext,mContext.getApplicationContext().getPackageName() + ".provider", file);
            intent.setDataAndType(uri1, "text/plain");
        }else{
            File file = new File(param );
            Uri uri2 = FileProvider.getUriForFile(mContext,mContext.getApplicationContext().getPackageName() + ".provider", file);
            intent.setDataAndType(uri2, "text/plain");
        }
        return intent;
    }
    //Android获取一个用于打开PDF文件的intent
    public Intent getPdfFileIntent( String param ){

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        File file = new File(param );
        Uri uri = FileProvider.getUriForFile(mContext,mContext.getApplicationContext().getPackageName() + ".provider", file);        intent.setDataAndType(uri, "application/pdf");
        return intent;
    }
}
