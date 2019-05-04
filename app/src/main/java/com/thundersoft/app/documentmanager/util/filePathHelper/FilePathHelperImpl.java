package com.thundersoft.app.documentmanager.util.filePathHelper;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import com.thundersoft.app.documentmanager.util.mimeTypeMatch.MimeTypeMatch;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FilePathHelperImpl implements FilePathHelper {

    private final static int ONE_WEEK = 7;
    private final static int ONE_MONTH = 30;
    private final static int THREE_MONTH = 90;
    private final static int ONE_YEAR = 365;
    private static final String SORT_ORDER_TIME_DESC = "date_modified DESC";
    private List<String> mImagePathList;
    private List<String> mImageNameList;
    private List<String> mVideoPathList;
    private List<String> mVideoNameList;
    private List<String> mAudioPathList;
    private List<String> mAudioNameList;
    private List<String> mDocumentPathList;
    private List<String> mDocumentNameList;
    private List<String> mDownloadPathList;
    private List<String> mDownloadNameList;
    private List<String> mApkPathList;
    private List<String> mApkNameList;
    private List<String> mRecentFourImagesList;

    public FilePathHelperImpl() {
        mRecentFourImagesList = new ArrayList<>();
        mImagePathList = new ArrayList<>();
        mImageNameList = new ArrayList<>();
        mVideoPathList = new ArrayList<>();
        mVideoNameList = new ArrayList<>();
        mAudioPathList = new ArrayList<>();
        mAudioNameList = new ArrayList<>();
        mDocumentPathList = new ArrayList<>();
        mDocumentNameList = new ArrayList<>();
        mDownloadPathList = new ArrayList<>();
        mDownloadNameList = new ArrayList<>();
        mApkPathList = new ArrayList<>();
        mApkNameList = new ArrayList<>();
    }

    @Override
    public List<String> getRecentFourImages(Context context) {
        Cursor cursorImage = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, null, null, SORT_ORDER_TIME_DESC);
        int size = 0;
        while(cursorImage.moveToNext() && size < 4) {
            String recentImagePath = cursorImage.getString(cursorImage.getColumnIndex(MediaStore.Images.Media.DATA));
            mRecentFourImagesList.add(recentImagePath);
            size++;
        }
        cursorImage.close();
        return mRecentFourImagesList;
    }

    @Override
    public List<String> getRecentImage(Context context, int recentTime) {
        List<String> fileList = new ArrayList<>();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null,
                null, SORT_ORDER_TIME_DESC);
        while (cursor.moveToNext()) {
            long fileTime = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.DATE_MODIFIED));
            long currentTime = System.currentTimeMillis();
            switch (recentTime) {
                case ONE_WEEK:
                    if (recentOneWeek(fileTime, currentTime)) {
                        String filePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                        fileList.add(filePath);
                    }
                    break;
                case ONE_MONTH:
                    if (recentOneMonth(fileTime, currentTime)) {
                        String filePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                        fileList.add(filePath);
                    }
                    break;
                case THREE_MONTH:
                    if (recentThreeMonth(fileTime, currentTime)) {
                        String filePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                        fileList.add(filePath);
                    }
                    break;
                case ONE_YEAR:
                    if (recentOneYear(fileTime, currentTime)) {
                        String filePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                        fileList.add(filePath);
                    }
                    break;
            }
        }
        cursor.close();
        return fileList;
    }

    @Override
    public List<String> getAllImagePath(Context context) {
        getImageList(context);
        return mImagePathList;
    }

    @Override
    public List<String> getAllVideoPath(Context context) {
        getVideoList(context);
        return mVideoPathList;
    }

    @Override
    public List<String> getAllAudioPath(Context context) {
        getAudioList(context);
        return mAudioPathList;
    }

    @Override
    public List<String> getAllDocumentAbsolutePath() {
        return mDocumentPathList;
    }

    @Override
    public List<String> getAllDocumentName() {
        return mDocumentNameList;
    }

    @Override
    public List<String> getAllDownloadAbsolutePath() {
        return mDownloadPathList;
    }

    @Override
    public List<String> getAllDownloadName() {
        return mDownloadNameList;
    }

    @Override
    public List<String> getAllApkAbsolutePath() {
        return mApkPathList;
    }

    @Override
    public List<String> getAllApkName() {
        return mApkNameList;
    }

    //对图片进行查找并添加到对应的List列表中
    public void getImageList(Context context) {
        Cursor cursorImage = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, null, null, null);
        while(cursorImage.moveToNext()) {
            String imagePath = cursorImage.getString(cursorImage.getColumnIndex(MediaStore.Images.Media.DATA));
            String imageName = cursorImage.getString(cursorImage.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));
            mImagePathList.add(imagePath);
            mImageNameList.add(imageName);
        }
        cursorImage.close();
    }

    //对视频文件进行查找并添加到对应的List列表中
    public void getVideoList(Context context) {
        Cursor cursorVideo= context.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                null, null, null, null);
        while(cursorVideo.moveToNext()) {
            String videoPath=cursorVideo.getString(cursorVideo.getColumnIndex(MediaStore.Video.Media.DATA));
            String videoName=cursorVideo.getString(cursorVideo.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME));
            mVideoPathList.add(videoPath);
            mVideoNameList.add(videoName);
        }
        cursorVideo.close();
    }

    //对音频文件进行查找并添加到对应的List列表中
    public void getAudioList(Context context) {
        Cursor cursorAudio = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null, null, null, null);
        while(cursorAudio.moveToNext()) {
            String audioPath = cursorAudio.getString(cursorAudio.getColumnIndex(MediaStore.Audio.Media.DATA));
            String audioName = cursorAudio.getString(cursorAudio.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
            mAudioPathList.add(audioPath);
            mAudioNameList.add(audioName);
        }
        cursorAudio.close();
    }

    //对文档文件，下载文件，apk文件进行查找并添加到相应的List列表中
    public void getAllSystemFileList(String path, Context context)
    {
        if (path == null) return;
        File file = new File(path);
        if(file.listFiles()!= null) {
            for(File files : file.listFiles()) {
                if(files.isHidden()) {
                    continue;
                }
                else if(files.getAbsolutePath().startsWith(context.getExternalFilesDir(null)
                        .getParentFile().getParent())) {
                    continue;
                }
                else if(files.isDirectory()) {
                    getAllSystemFileList(files.getAbsolutePath(),context);
                }
                else {
                    String type = MimeTypeMatch.getCategoryMimeType(files.getAbsolutePath());
                    if(type != null) {
                        if(type.equals("apk")) {
                            mApkNameList.add(files.getName());
                            mApkPathList.add(files.getAbsolutePath());
                        }
                        else if(type.equals("compression")) {
                            mDownloadNameList.add(file.getName());
                            mDownloadPathList.add(files.getAbsolutePath());
                        }
                        else if(type.equals("document")) {
                            mDocumentNameList.add(file.getName());
                            mDocumentPathList.add(files.getAbsolutePath());
                        }
                    }
                }
            }
        }
    }

    public boolean recentOneWeek(long fileTime, long currentTime) {
        if (((currentTime / 1000) - fileTime) / 86400 <= ONE_WEEK) {
            return true;
        }
        return false;
    }

    public boolean recentOneMonth(long fileTime, long currentTime) {
        if (((currentTime / 1000) - fileTime) / 86400 <= ONE_MONTH) {
            return true;
        }
        return false;
    }

    public boolean recentThreeMonth(long fileTime, long currentTime) {
        if (((currentTime / 1000) - fileTime) / 86400 <= THREE_MONTH) {
            return true;
        }
        return false;
    }

    public boolean recentOneYear(long fileTime, long currentTime) {
        if (((currentTime / 1000) - fileTime) / 86400 <= ONE_YEAR) {
            return true;
        }
        return false;
    }


}
