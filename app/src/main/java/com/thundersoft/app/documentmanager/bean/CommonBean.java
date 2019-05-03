package com.thundersoft.app.documentmanager.bean;


import android.graphics.Bitmap;

//作为RecyclerView的适配器对象
public class CommonBean {

    private Bitmap mIcon;
    private String mFileName;
    private String mAbsolutePath;

    public CommonBean(Bitmap icon, String fileName, String absolutePath) {
        this.mIcon = icon;
        this.mFileName = fileName;
        this.mAbsolutePath = absolutePath;
    }

    public Bitmap getmIcon() {
        return mIcon;
    }

    public String getmFileName() {
        return mFileName;
    }

    public String getmAbsolutePath() {
        return mAbsolutePath;
    }

    public void setmIcon(Bitmap mIcon) {
        this.mIcon = mIcon;
    }

    public void setmFileName(String mFileName) {
        this.mFileName = mFileName;
    }

    public void setmAbsolutePath(String mAbsolutePath) {
        this.mAbsolutePath = mAbsolutePath;
    }

}
