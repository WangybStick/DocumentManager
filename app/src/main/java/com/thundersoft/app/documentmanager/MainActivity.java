package com.thundersoft.app.documentmanager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.thundersoft.app.documentmanager.database.MyDataBaseManager;
import com.thundersoft.app.documentmanager.util.filePathHelper.FilePathHelper;
import com.thundersoft.app.documentmanager.util.filePathHelper.FilePathHelperImpl;
import com.thundersoft.app.documentmanager.util.permission.RuntimePermissonVerify;
import com.thundersoft.app.documentmanager.util.phoneStorageSD.PhoneStorageSD;
import com.thundersoft.app.documentmanager.util.phoneStorageSD.PhoneStorageSDImpl;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String AVAILABLE_SIZE = "availableSize";
    private static final String TOTAL_SIZE = "totalSize";
    private static final String RECENT_FIRST_IMAGE = "RecentFirstImage";
    private static final String RECENT_SECOND_IMAGE = "RecentSecondImage";
    private static final String RECENT_THIRD_IMAGE = "RecentThirdImage";
    private static final String RECENT_FOURTH_IMAGE = "RecentFourthImage";

    private Toolbar mMainToolBar;
    private ImageView mRecentFirstImageView;
    private ImageView mRecentSecondImageView;
    private ImageView mRecentThirdImageView;
    private ImageView mRecentFourthImageView;
    private LinearLayout mClassifyPictureLinearLayout;
    private LinearLayout mClassifyVideoLinearLayout;
    private LinearLayout mClassifyAudioLinearLayout;
    private LinearLayout mClassifyDocumentLinearLayout;
    private LinearLayout mClassifyDownloadLinearLayout;
    private LinearLayout mClassifyAndroidLinearLayout;
    private TextView mPhoneStorageTextView;
    private PhoneStorageSD mPhoneStorageSD;
    private MyDataBaseManager mMyDataBaseManager;
    private MyAsyncTask mMyAsyncTask;
    private FilePathHelper mFilePathHelper;
    private List<String> mRecentFourImagesList;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        mHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 1:
                        Bundle phoneStorageBundle = msg.getData();
                        String availableSize = phoneStorageBundle.getString(AVAILABLE_SIZE);
                        String totalSize = phoneStorageBundle.getString(TOTAL_SIZE);
                        mPhoneStorageTextView.setText(availableSize + " / " + totalSize);
                        break;

                    case 2:
                        Bundle recentImageBundle = msg.getData();
                        mRecentFirstImageView.setImageBitmap((Bitmap) recentImageBundle.getParcelable(
                                RECENT_FIRST_IMAGE));
                        mRecentSecondImageView.setImageBitmap((Bitmap) recentImageBundle.getParcelable(
                                RECENT_SECOND_IMAGE));
                        mRecentThirdImageView.setImageBitmap((Bitmap) recentImageBundle.getParcelable(
                                RECENT_THIRD_IMAGE));
                        mRecentFourthImageView.setImageBitmap((Bitmap) recentImageBundle.getParcelable(
                                RECENT_FOURTH_IMAGE));
                        break;
                }

            }
        };

        //启动子线程动态初始化主界面最近图片的四张展示
        new RecentFourImagesThread().start();

        //ToolBar的初始化操作
        initToolBar();

        //初始化静态视图
        initViewAndLayout();

        //启动子线程动态初始化phoneStorage视图
        new PhoneStorageThread().start();

        //获取运行时权限
        RuntimePermissonVerify.verifyStoragePermission(this);

        //初始化视图的点击监听
        setOnclickListener();

        //（后台AsyncTask线程）初始化图片，音频，视频，文档文件，下载文件，APK文件到自定义数据库
        mMyAsyncTask = new MyAsyncTask();
        mMyAsyncTask.execute();

    }

    //ToolBar的初始化操作
    public void initToolBar() {
        mMainToolBar = findViewById(R.id.main_toolbar);
        mMainToolBar.setTitle(R.string.toolBarTitle);
        mMainToolBar.setNavigationIcon(R.drawable.main_toolbar_navigation);
    }

    //获取视图，初始化静态视图
    public void initViewAndLayout() {
        mPhoneStorageTextView = findViewById(R.id.phoneStorage_text_view);
        mRecentFirstImageView = findViewById(R.id.recent_first_image_view);
        mRecentSecondImageView = findViewById(R.id.recent_second_image_view);
        mRecentThirdImageView = findViewById(R.id.recent_third_image_view);
        mRecentFourthImageView = findViewById(R.id.recent_fourth_image_view);
        mClassifyPictureLinearLayout = findViewById(R.id.classify_picture_layout);
        mClassifyVideoLinearLayout = findViewById(R.id.classify_video_layout);
        mClassifyAudioLinearLayout = findViewById(R.id.classify_audio_layout);
        mClassifyDocumentLinearLayout = findViewById(R.id.classify_document_layout);
        mClassifyDownloadLinearLayout = findViewById(R.id.classify_download_layout);
        mClassifyAndroidLinearLayout = findViewById(R.id.classify_android_layout);
    }

    //动态初始化主界面最近图片的四张展示的子线程
    private class RecentFourImagesThread extends Thread {
        @Override
        public void run() {
            super.run();
            List<Bitmap> recentBitmapList = new ArrayList<>();
            Bundle recentImageBundle = new Bundle();
            Message recentImageMessage = Message.obtain();
            recentImageMessage.what = 2;
            mFilePathHelper = new FilePathHelperImpl();
            mRecentFourImagesList = mFilePathHelper.getRecentFourImages(MainActivity.this);
            int recentSize = mRecentFourImagesList.size();
            if (recentSize != 0) {
                for (int i = 0; i < recentSize; i++) {
                    Bitmap bitmap = BitmapFactory.decodeFile(mRecentFourImagesList.get(i));
                    recentBitmapList.add(bitmap);
                }
            }
            recentImageBundle.putParcelable(RECENT_FIRST_IMAGE, recentBitmapList.get(0));
            recentImageBundle.putParcelable(RECENT_SECOND_IMAGE, recentBitmapList.get(1));
            recentImageBundle.putParcelable(RECENT_THIRD_IMAGE, recentBitmapList.get(2));
            recentImageBundle.putParcelable(RECENT_FOURTH_IMAGE, recentBitmapList.get(3));
            recentImageMessage.setData(recentImageBundle);
            mHandler.sendMessage(recentImageMessage);
        }
    }

    //动态初始化phoneStorage视图的子线程
    private class PhoneStorageThread extends Thread {
        @Override
        public void run() {
            super.run();
            Message phoneStorageMessage = Message.obtain();
            phoneStorageMessage.what = 1;
            mPhoneStorageSD = new PhoneStorageSDImpl(MainActivity.this);
            Bundle phoneStorageBundle = new Bundle();
            String availableSize = mPhoneStorageSD.getAvailableSize();
            String totalSize = mPhoneStorageSD.getTotalSize();
            phoneStorageBundle.putString(AVAILABLE_SIZE, availableSize);
            phoneStorageBundle.putString(TOTAL_SIZE, totalSize);
            phoneStorageMessage.setData(phoneStorageBundle);
            mHandler.sendMessage(phoneStorageMessage);
        }
    }

    //初始化视图的点击监听
    public void setOnclickListener() {
        mRecentFirstImageView.setOnClickListener(this);
        mRecentSecondImageView.setOnClickListener(this);
        mRecentThirdImageView.setOnClickListener(this);
        mRecentFourthImageView.setOnClickListener(this);
        mClassifyPictureLinearLayout.setOnClickListener(this);
        mClassifyVideoLinearLayout.setOnClickListener(this);
        mClassifyAudioLinearLayout.setOnClickListener(this);
        mClassifyDocumentLinearLayout.setOnClickListener(this);
        mClassifyDownloadLinearLayout.setOnClickListener(this);
        mClassifyAndroidLinearLayout.setOnClickListener(this);

    }

    //视图的点击事件
    @Override
    public void onClick(View view) {
        Intent commonIntent = new Intent(MainActivity.this, CommonActivity.class);
        Intent recentIntent = new Intent(MainActivity.this, RecentActivity.class);
        Intent phoneStorageIntent = new Intent(MainActivity.this, PhoneStorageActivity.class);
        switch(view.getId()) {
            case R.id.recent_first_image_view:
                startActivity(recentIntent);
                break;

            case R.id.recent_second_image_view:
                startActivity(recentIntent);
                break;

            case R.id.recent_third_image_view:
                startActivity(recentIntent);
                break;

            case R.id.recent_fourth_image_view:
                startActivity(recentIntent);
                break;

            case R.id.classify_picture_layout:
                commonIntent.setType(getString(R.string.classify_picture));
                startActivity(commonIntent);
                break;

            case R.id.classify_video_layout:
                commonIntent.setType(getString(R.string.classify_video));
                startActivity(commonIntent);
                break;

            case R.id.classify_audio_layout:
                commonIntent.setType(getString(R.string.classify_audio));
                startActivity(commonIntent);
                break;

            case R.id.classify_document_layout:
                commonIntent.setType(getString(R.string.classify_document));
                startActivity(commonIntent);
                break;

            case R.id.classify_download_layout:
                commonIntent.setType(getString(R.string.classify_download));
                startActivity(commonIntent);
                break;

            case R.id.classify_android_layout:
                commonIntent.setType(getString(R.string.classify_android));
                startActivity(commonIntent);
                break;

            case R.id.phoneStorage_layout:
                startActivity(phoneStorageIntent);
                break;

        }
    }

    //初始化文档文件，下载文件，APK文件到自定义数据库
    public void initMyDataBase() {
        mMyDataBaseManager = new MyDataBaseManager(MainActivity.this);
        mMyDataBaseManager.initMyDataBase();
    }

    private class MyAsyncTask extends AsyncTask<Void, Void, Boolean>{
        @Override
        protected Boolean doInBackground(Void... voids) {
            initMyDataBase();
            return true;
        }
    }
}