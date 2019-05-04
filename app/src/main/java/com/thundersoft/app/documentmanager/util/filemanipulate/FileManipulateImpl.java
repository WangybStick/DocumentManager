package com.thundersoft.app.documentmanager.util.filemanipulate;

import android.app.AlertDialog;
import android.content.Context;
import android.widget.Toast;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FileManipulateImpl implements FileManipulate{

    public FileManipulateImpl() {}

    @Override
    public boolean deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public long getFileTime(String filePath) {
        long time = 0;
        File file = new File(filePath);
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            try {
                if (file.lastModified() > time) {
                    time = file.lastModified();
                }
                for (File tempFile : files) {
                    long tempTime = getFileTime(tempFile.getAbsolutePath());
                    if (tempTime > time) {
                        time = tempTime;
                    }
                }
            } catch (Exception e) {
            }
        } else {
            try {
                time = file.lastModified();
            } catch (Exception e) {
            }
        }
        return time;
    }

    public long getFileSize(String filePath) {
        long size = 0;
        File file = new File(filePath);
        if (file.isDirectory()) {
            try {
                size += file.length();
                File[] files = file.listFiles();
                for (File tempFile : files) {
                    size = size+getFileSize(tempFile.getAbsolutePath());
                }
            } catch (Exception e) {
            }
        } else {
            try {
                size = file.length();
            } catch (Exception e) {
            }
        }
        return size;
    }

    public List<String> getFileAttribute(String filePath) {
        List<String> resultList = new ArrayList<String>();
        File file = new File(filePath);
        String name = file.getName();
        String path = file.getAbsolutePath();
        float fSize = getFileSize(filePath);
        if(fSize!=0)
        {
            fSize =fSize/ 1024;
        }
        long time = getFileTime(filePath);
        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DecimalFormat df = new DecimalFormat("##0.00");
        String dateTime = sdf.format(date);
        resultList.add(name);
        resultList.add(path);
        if (fSize < 1000) {
            String size = df.format(fSize);
            resultList.add(size + "KB");
        } else {
            fSize = fSize / 1024;
            if (fSize < 1000) {
                String size = df.format(fSize);
                resultList.add(size + "MB");
            } else {
                fSize = fSize / 1024;
                String size = df.format(fSize);
                resultList.add(size + "GB");
            }
        }
        resultList.add(dateTime + "");
        return resultList;
    }

    public void showFileAttribute(List<String> list,Context context) {
        if (list != null) {
            String name = list.get(0);
            String path = list.get(1);
            String size = list.get(2);
            String time = list.get(3);
            new AlertDialog.Builder(context)
                    .setTitle("文件属性")
                    .setMessage(
                            "名称" + name + "\r\n" + "路径" + path + "\r\n"
                                    + "大小" + size + "\r\n" + "时间" + time)
                    .setPositiveButton("确定", null).show();
        }
        else
        {
            Toast.makeText(context, "属性获取失败", Toast.LENGTH_SHORT).show();
        }
    }
}
