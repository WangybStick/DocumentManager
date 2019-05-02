package com.thundersoft.app.documentmanager.util.filebitmaphelper;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import com.thundersoft.app.documentmanager.R;
import com.thundersoft.app.documentmanager.util.mimeTypeMatch.MimeTypeMatch;
import java.io.File;

public class FileBitmapHelperImpl implements FileBitmapHelper {

    @Override
    public Bitmap getFileBitmap(Context context, String filePath, final boolean isDefault) {
        Bitmap bitmap = null;
        File file = new File(filePath);
        if (file.isDirectory()) {
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.folder_blue);
        }
        else {
            String type = MimeTypeMatch.getCategoryMimeType(filePath);
            if (type!=null) {
                if (type.equals(MimeTypeMatch.MIME_TYPE_AUDIO)) {
                    bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.music);
                }
                else if (type.equals(MimeTypeMatch.MIME_TYPE_VIDEO)) {
                    bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.video);
                }
                else if (type.equals(MimeTypeMatch.MIME_TYPE_IMAGE)) {
                    if (isDefault) {
                        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.image);
                    }
                    else {
                        bitmap = getImageThumbnail(context,filePath);
                    }
                }
                else if (type.equals(MimeTypeMatch.MIME_TYPE_DOCUMENT)) {
                    if (isDefault) {
                        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.text);
                    }
                    else {
                        bitmap = getDocumentIcon(context,filePath);
                    }
                }
                else if (type.equals(MimeTypeMatch.MIME_TYPE_DOWNLOAD)) {
                    bitmap=BitmapFactory.decodeResource(context.getResources(), R.drawable.archive_yellow);
                }
                else if (type.equals(MimeTypeMatch.MIME_TYPE_APK)) {
                    if (isDefault) {
                        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.apk);
                    }
                    else {
                        bitmap = getApkIcon(context,filePath);
                    }
                }
            }
            else {
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.unknown);
            }
        }
        return bitmap;
    }

    private Bitmap getDocumentIcon(Context context, String filePath) {
        String mimeType = MimeTypeMatch.getMimeType(filePath);
        Bitmap bitmap;

        if (mimeType==null) {
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.unknown);
        }
        else if (mimeType.equals("text/plain")) {
            bitmap= BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.text);
        }
        else if (mimeType.startsWith("text")) {
            bitmap= BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.text);
        }
        else if (mimeType.equals("application/msword")
                || mimeType
                .equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document")
                || mimeType
                .equals("application/vnd.openxmlformats-officedocument.wordprocessingml.template")) {
            bitmap= BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.word);
        }
        else if (mimeType.equals("application/vnd.ms-excel")
                || mimeType
                .equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                || mimeType
                .equals("application/vnd.openxmlformats-officedocument.spreadsheetml.template")) {
            bitmap= BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.excel);
        }
        else if (mimeType.equals("application/vnd.ms-powerpoint")
                || mimeType
                .equals("application/vnd.openxmlformats-officedocument.presentationml.presentation")
                || mimeType
                .equals("application/vnd.openxmlformats-officedocument.presentationml.template")
                || mimeType
                .equals("application/vnd.openxmlformats-officedocument.presentationml.slideshow")) {
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.powerpoint);
        }
        else if (mimeType.equals("application/pdf")) {
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.pdf);
        }
        else {
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.unknown);
        }
        return bitmap;
    }

    private Bitmap getApkIcon(Context context,String filePath) {
        Drawable drawable = null;
        PackageManager pm = context.getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(filePath, PackageManager.GET_ACTIVITIES);
        if (info!=null) {
            ApplicationInfo appInfo = info.applicationInfo;
            appInfo.sourceDir = filePath;
            appInfo.publicSourceDir = filePath;
            try {
                drawable = appInfo.loadIcon(pm);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
        Bitmap bitmap = bitmapDrawable.getBitmap();
        if (bitmap==null) {
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.apk);
        }
        return bitmap;
    }

    private Bitmap getImageThumbnail(Context context,String filePath) {
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[] { MediaStore.Images.Media._ID },
                MediaStore.Images.Media.DATA + "=?",
                new String[] { filePath }, null);
        int id = 0;
        while (cursor.moveToNext()) {
            id = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media._ID));
        }
        cursor.close();
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap bitmap = MediaStore.Images.Thumbnails.getThumbnail(
                context.getContentResolver(), id,
                MediaStore.Images.Thumbnails.MINI_KIND, options);
        if (bitmap != null) {
            return bitmap;
        }
        options.inJustDecodeBounds = true;
        int height = options.outHeight/ 8;
        int width = options.outWidth/ 8;
        options.inSampleSize = 8;
        options.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(filePath, options);
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        if (bitmap!=null) {
            return bitmap;
        }
        return BitmapFactory.decodeResource(context.getResources(), R.drawable.image);
    }
}
