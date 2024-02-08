package com.emp.printerdemo.base;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import butterknife.ButterKnife;


/**
 * Create by 梦魇
 * Date: 2020/4/22
 */
public abstract class BaseActivity extends AppCompatActivity {
    private static final String TAG = "BaseActivity";

    public ProgressDialog progressDialog;

    @Override
    protected final void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        // 控件数据初始化
        initViews(savedInstanceState);
        initData();
    }

    /**
     * 获取布局id
     *
     * @return
     */
    protected abstract int getLayoutId();

    /**
     * 初始化控件
     */
    protected abstract void initViews(@Nullable Bundle savedInstanceState);

    /**
     * 初始化数据
     */
    protected abstract void initData();

    // 加载窗的弹出
    public void onProgressRun(String msg) {
        onProgressRun(msg, ProgressDialog.STYLE_SPINNER);
    }

    // 加载窗的弹出
    public void onProgressRun(String msg, int style) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("操作提示");
        progressDialog.setMessage(msg);
        progressDialog.setProgressStyle(style);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    // 关闭加载窗
    public void onDismissProgress() {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();// 销毁对话框
    }

    // 吐司弹出
    public void onToast(Object msg) {
//        Toast.makeText(getApplicationContext(), "" + msg, Toast.LENGTH_SHORT).show();
        Toast toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT);
        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
        v.setText("" + msg);
        v.setTextColor(Color.RED);
        toast.show();

    }


}
