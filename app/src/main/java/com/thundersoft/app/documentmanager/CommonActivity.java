package com.thundersoft.app.documentmanager;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.thundersoft.app.documentmanager.adapter.FileListAdapter;
import com.thundersoft.app.documentmanager.bean.CommonBean;
import com.thundersoft.app.documentmanager.database.MyDataBaseManager;
import com.thundersoft.app.documentmanager.util.datamanipulate.DataManipulate;
import com.thundersoft.app.documentmanager.util.datamanipulate.DataManipulateImpl;
import com.thundersoft.app.documentmanager.util.filePathHelper.FilePathHelper;
import com.thundersoft.app.documentmanager.util.filePathHelper.FilePathHelperImpl;
import com.thundersoft.app.documentmanager.util.filebitmaphelper.FileBitmapHelper;
import com.thundersoft.app.documentmanager.util.filebitmaphelper.FileBitmapHelperImpl;
import com.thundersoft.app.documentmanager.util.filemanipulate.FileManipulate;
import com.thundersoft.app.documentmanager.util.filemanipulate.FileManipulateImpl;
import com.thundersoft.app.documentmanager.util.fileopen.FileOpen;
import com.thundersoft.app.documentmanager.util.fileopen.FileOpenImpl;
import com.thundersoft.app.documentmanager.util.permission.RuntimePermissonVerify;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CommonActivity extends AppCompatActivity {

    private Toolbar mCommonToolbar;
    private boolean mArrangementFlag = true;
    private TextView mCommonTextView;
    private RecyclerView mFileListRecyclerView;
    private List<CommonBean> mFileListRecyclerViewDataList;
    private MyDataBaseManager mMyDataBaseManager;
    private SQLiteDatabase mSQLiteDatabase;
    private FilePathHelper mFilePathHelper;
    private DataManipulate mDataManipulate;
    private FileBitmapHelper mFileBitmapHelper;
    private RecyclerView.LayoutManager mRecyclerViewLayoutManager;
    private FileListAdapter mFileListAdapter;
    private FileOpen mFileOpen;
    private String mIntentType;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    //initFileListRecyclerViewClick();
                    onStart();
                    break;

                case 2:
                    //initFileListRecyclerViewClick();
                    onStart();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common);

        //初始化工具栏
        initToolBar();

        //初始化视图
        initView();

        //获取运行时权限
        RuntimePermissonVerify.verifyStoragePermission(this);

        //初始化FileListRecyclerView的数据
        initFileListRecyclerViewData();

        //初始化FileListRecyclerView以及点击事件
        initFileListRecyclerViewClick();

    }

    //初始化工具栏
    public void initToolBar() {
        mCommonToolbar = findViewById(R.id.common_toolBar);

        //设置返回图标
        mCommonToolbar.setNavigationIcon(R.drawable.icon_back);

        //设置返回图片的点击事件
        mCommonToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //初始化menu菜单
        mCommonToolbar.inflateMenu(R.menu.menu_common);

        //设置菜单点击事件
        mCommonToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (mArrangementFlag) {
                    //垂直排列
                    mArrangementFlag = false;
                    menuItem.setIcon(R.drawable.icon_vertical);
                    Message message = Message.obtain();
                    message.what = 1;
                    mHandler.sendMessage(message);
                } else {
                    //网格排列
                    mArrangementFlag = true;
                    menuItem.setIcon(R.drawable.icon_grid);
                    Message message = Message.obtain();
                    message.what = 2;
                    mHandler.sendMessage(message);
                }
                return false;
            }
        });
    }

    //初始化视图
    public void initView() {
        mCommonTextView = findViewById(R.id.common_path_textView);
        mFileListRecyclerView = findViewById(R.id.fileList_recyclerView);
    }

    //初始化FileListRecyclerView的数据
    public void initFileListRecyclerViewData() {
        Intent intent = getIntent();
        mIntentType = intent.getType();
        List<String> dataPathList = new ArrayList<>();
        mMyDataBaseManager = new MyDataBaseManager(CommonActivity.this);
        mSQLiteDatabase = mMyDataBaseManager.getmSQLiteDatabase();
        mFilePathHelper = new FilePathHelperImpl();
        mDataManipulate = new DataManipulateImpl(mFilePathHelper, mSQLiteDatabase, CommonActivity.this);
        if (mIntentType.equals(getString(R.string.classify_picture))) {
            mCommonTextView.setText("我的文件 > 图片");
            dataPathList = mDataManipulate.queryImagePath();
        } else if (mIntentType.equals(getString(R.string.classify_video))) {
            mCommonTextView.setText("我的文件 > 音频");
            dataPathList = mDataManipulate.queryAudioPath();
        } else if (mIntentType.equals(getString(R.string.classify_audio))) {
            mCommonTextView.setText("我的文件 > 视频");
            dataPathList = mDataManipulate.queryVideoPath();
        } else if (mIntentType.equals(getString(R.string.classify_document))) {
            mCommonTextView.setText("我的文件 > 文件");
            dataPathList=  mDataManipulate.queryDocumentPath();
        } else if (mIntentType.equals(getString(R.string.classify_download))) {
            mCommonTextView.setText("我的文件 > 下载");
            dataPathList=  mDataManipulate.queryDownloadPath();
        } else if (mIntentType.equals(getString(R.string.classify_android))) {
            mCommonTextView.setText("我的文件 > apk");
            dataPathList =  mDataManipulate.queryApkPath();
        }
        if (dataPathList.size() != 0) {
            String filePath = dataPathList.get(0);
            mFileBitmapHelper = new FileBitmapHelperImpl();
            mFileListRecyclerViewDataList = new ArrayList<>();
            Bitmap icon = mFileBitmapHelper.getFileBitmap(CommonActivity.this,
                    filePath, true);
            for (String dataPath : dataPathList) {
                File file = new File(dataPath);
                String fileName = file.getName();
                String absolutePath = file.getAbsolutePath();
                Log.d("fileName", fileName);
                mFileListRecyclerViewDataList.add(new CommonBean(icon, fileName, absolutePath));
            }
        }
    }

    //初始化FileListRecyclerView以及点击事件
    public void initFileListRecyclerViewClick() {
        if (!mArrangementFlag) {
            mRecyclerViewLayoutManager = new LinearLayoutManager(this);
            mFileListRecyclerView.setLayoutManager(mRecyclerViewLayoutManager);
        } else {
            mRecyclerViewLayoutManager = new
                    StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL);
            mFileListRecyclerView.setLayoutManager(mRecyclerViewLayoutManager);
        }
        mFileListAdapter = new FileListAdapter(CommonActivity.this,
                mFileListRecyclerViewDataList, mArrangementFlag);
        mFileListAdapter.setOnItemClickListener(new FileListAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                mFileOpen = new FileOpenImpl(CommonActivity.this);
                String filePath = mFileListRecyclerViewDataList.get(position).getmAbsolutePath();
                Intent intent = mFileOpen.getOpenFileIntent(filePath);
                startActivity(intent);
            }
        });
        mFileListAdapter.setOnItemLongListener(new FileListAdapter.OnRecyclerViewItemLongListener() {
            String[] fileOpItemStr = {"重命名","删除","查看文件属性"};
            List<String> stringList = new ArrayList<>();
            FileManipulate mFileManipulate = new FileManipulateImpl();
            @Override
            public void onItemLongClick(View view, final int position) {
                final String filePath = mFileListRecyclerViewDataList.get(position).getmAbsolutePath();
                stringList.add(filePath);
                final DialogInterface.OnClickListener fileOpDialogOnClickListener = new DialogInterface
                        .OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:
                                rename(filePath,position);
                                break;
                            case 1:
                                if (mIntentType.equals(getString(R.string.classify_picture))) {
                                    mDataManipulate.deleteImageItemFromMediaStore(filePath);
                                } else if (mIntentType.equals(getString(R.string.classify_audio))) {
                                    mDataManipulate.deleteAudioItemFromMediaStore(filePath);
                                } else if (mIntentType.equals(getString(R.string.classify_video))) {
                                    mDataManipulate.deleteVideoItemFromMediaStore(filePath);
                                } else if (mIntentType.equals(getString(R.string.classify_document))) {
                                    mDataManipulate.deleteDocumentItemFromMyDataBase(filePath);
                                } else if (mIntentType.equals(getString(R.string.classify_download))) {
                                    mDataManipulate.deleteDownloadItemFromMyDataBase(filePath);
                                } else if (mIntentType.equals(getString(R.string.classify_android))) {
                                    mDataManipulate.deleteApkItemFromMyDataBase(filePath);
                                }
                                mFileListAdapter.removeItem(position);
                                mFileManipulate.deleteFile(filePath);
                                Toast.makeText(CommonActivity.this, "删除成功",
                                        Toast.LENGTH_LONG).show();
                                break;
                            case 2:
                                List<String> resultList;
                                resultList = mFileManipulate.getFileAttribute(filePath);
                                mFileManipulate.showFileAttribute(resultList,CommonActivity.this);
                                break;
                        }
                    }
                };
                DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub

                    }
                };
                new AlertDialog.Builder(CommonActivity.this)
                        .setTitle("文件操作")
                        .setItems(fileOpItemStr, fileOpDialogOnClickListener).show();
            }
        });
        mFileListRecyclerView.setAdapter(mFileListAdapter);
    }

    //重命名
    private void rename(final String filePath, final int position) {
        final AlertDialog alertDialog = new AlertDialog.Builder(CommonActivity.this).create();
        View renameDialog = View.inflate(CommonActivity.this, R.layout.dialog_common_rename,null);
        File file = new File(filePath);
        final String name = file.getName();
        alertDialog.setView(renameDialog);
        alertDialog.show();
        final EditText newEditText = alertDialog.findViewById(R.id.editText_dialog_common_newName);
        Button confirmButton = alertDialog.findViewById(R.id.button_common_rename_dialog_confirm);
        Button cancelButton = alertDialog.findViewById(R.id.button_common_rename_dialog_cancel);
        //显示文件原名称
        newEditText.setText(name);
        //重命名弹出框确定按钮
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String newName = newEditText.getText().toString().trim();
                mFileListAdapter.renameItem(position, newName);
                if (mIntentType.equals(getString(R.string.classify_picture))) {
                    mDataManipulate.renameImageItemFromMediaStore(filePath, newName);
                } else if (mIntentType.equals(getString(R.string.classify_audio))) {
                    mDataManipulate.renameAudioItemFromMediaStore(filePath, newName);
                } else if (mIntentType.equals(getString(R.string.classify_video))) {
                    mDataManipulate.renameVideoItemFromMediaStore(filePath, newName);
                } else if (mIntentType.equals(getString(R.string.classify_document))) {
                    mDataManipulate.renameDocumentItemFromMediaStore(filePath, newName);
                } else if (mIntentType.equals(getString(R.string.classify_download))) {
                    mDataManipulate.renameDownloadItemFromMediaStore(filePath, newName);
                } else if (mIntentType.equals(getString(R.string.classify_android))) {
                    mDataManipulate.renameApkItemFromMediaStore(filePath, newName);
                }
            }
        });
        //重命名弹出框取消按钮
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        initFileListRecyclerViewClick();
    }
}
