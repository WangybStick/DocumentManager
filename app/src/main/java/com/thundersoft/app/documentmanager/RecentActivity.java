package com.thundersoft.app.documentmanager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.thundersoft.app.documentmanager.adapter.FileListAdapter;
import com.thundersoft.app.documentmanager.bean.CommonBean;
import com.thundersoft.app.documentmanager.util.filePathHelper.FilePathHelper;
import com.thundersoft.app.documentmanager.util.filePathHelper.FilePathHelperImpl;
import com.thundersoft.app.documentmanager.util.filebitmaphelper.FileBitmapHelper;
import com.thundersoft.app.documentmanager.util.filebitmaphelper.FileBitmapHelperImpl;
import com.thundersoft.app.documentmanager.util.fileopen.FileOpen;
import com.thundersoft.app.documentmanager.util.fileopen.FileOpenImpl;
import com.thundersoft.app.documentmanager.util.permission.RuntimePermissonVerify;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RecentActivity extends AppCompatActivity {

    private final static int ONE_WEEK = 7;
    private final static int ONE_MONTH = 30;
    private final static int THREE_MONTH = 90;
    private final static int ONE_YEAR = 365;
    private Toolbar mRecentToolbar;
    private TextView mRecentTextView;
    private RecyclerView mRecentImageRecyclerView;
    private FilePathHelper mFilePathHelper;
    private FileBitmapHelper mFileBitmapHelper;
    private List<CommonBean> mFileListRecyclerViewDataList;
    private RecyclerView.LayoutManager mRecyclerViewLayoutManager;
    private FileListAdapter mFileListAdapter;
    private FileOpen mFileOpen;

    //创建异步Handler处理数据更新操作
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    mRecentTextView.setText(getString(R.string.recent_week));
                    break;

                case 2:
                    mRecentTextView.setText(getString(R.string.recent_oneMonth));
                    break;

                case 3:
                    mRecentTextView.setText(getString(R.string.recent_threeMonth));
                    break;

                case 4:
                    mRecentTextView.setText(getString(R.string.recent_oneYear));
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent);

        //初始化工具栏
        initToolBar();

        //初始化视图
        initView();

        //获取运行时权限：
        RuntimePermissonVerify.verifyStoragePermission(this);

        //初始化当前视图,默认为一年
        initFileListRecyclerViewData(ONE_YEAR);
        initFileListRecyclerViewClick();
        mRecentTextView.setText(getString(R.string.recent_oneYear));

    }

    //初始化工具栏
    public void initToolBar() {
        mRecentToolbar = findViewById(R.id.recent_toolBar);

        //设置返回图标
        mRecentToolbar.setNavigationIcon(R.drawable.icon_back);

        //设置标题
        mRecentToolbar.setTitle("最近图片");

        //设置返回图片的点击事件
        mRecentToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //初始化menu菜单
        mRecentToolbar.inflateMenu(R.menu.menu_recent);

        //设置菜单点击事件
        mRecentToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(final MenuItem menuItem) {
                int menuItemId = menuItem.getItemId();
                Message message = Message.obtain();
                switch(menuItemId) {
                    case R.id.menu_recent_one_week:
                        initFileListRecyclerViewData(ONE_WEEK);
                        initFileListRecyclerViewClick();
                        message.what = 1;
                        mHandler.sendMessage(message);
                        break;
                    case R.id.menu_recent_one_month:
                        initFileListRecyclerViewData(ONE_MONTH);
                        initFileListRecyclerViewClick();
                        message.what = 2;
                        mHandler.sendMessage(message);
                        break;
                    case R.id.menu_recent_three_month:
                        initFileListRecyclerViewData(THREE_MONTH);
                        initFileListRecyclerViewClick();
                        message.what = 3;
                        mHandler.sendMessage(message);
                        break;
                    case R.id.menu_recent_one_year:
                        initFileListRecyclerViewData(ONE_YEAR);
                        initFileListRecyclerViewClick();
                        message.what = 4;
                        mHandler.sendMessage(message);
                        break;
                }
                return false;
            }
        });
    }


    //初始化视图
    public void initView() {
        mRecentTextView = findViewById(R.id.recent_time_textView);
        mRecentImageRecyclerView = findViewById(R.id.fileList_recyclerView);
    }

    //初始化FileListRecyclerView的数据
    public void initFileListRecyclerViewData(int recentTime) {
        List<String> dataPathList;
        mFilePathHelper = new FilePathHelperImpl();
        dataPathList = mFilePathHelper.getRecentImage(RecentActivity.this, recentTime);
        if (dataPathList.size() != 0) {
            String filePath = dataPathList.get(0);
            mFileBitmapHelper = new FileBitmapHelperImpl();
            mFileListRecyclerViewDataList = new ArrayList<>();
            Bitmap icon = mFileBitmapHelper.getFileBitmap(RecentActivity.this,
                    filePath, true);
            for (String dataPath : dataPathList) {
                File file = new File(dataPath);
                String fileName = file.getName();
                String absolutePath = file.getAbsolutePath();
                mFileListRecyclerViewDataList.add(new CommonBean(icon, fileName, absolutePath));
            }
        }
    }

    //初始化FileListRecyclerView以及点击事件
    public void initFileListRecyclerViewClick() {
        mRecyclerViewLayoutManager = new LinearLayoutManager(this);
        mFileListAdapter = new FileListAdapter(RecentActivity.this,
                mFileListRecyclerViewDataList, true);
        mFileListAdapter.setOnItemClickListener(new FileListAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                mFileOpen = new FileOpenImpl(RecentActivity.this);
                String filePath = mFileListRecyclerViewDataList.get(position).getmAbsolutePath();
                Intent intent = mFileOpen.getOpenFileIntent(filePath);
                startActivity(intent);
            }
        });
        mRecentImageRecyclerView.setAdapter(mFileListAdapter);
    }
}
