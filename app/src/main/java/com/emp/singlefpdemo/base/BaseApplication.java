package com.emp.singlefpdemo.base;

import android.app.Application;
import android.util.Log;

import com.emp.xdcommon.common.utils.ToastUtil;
import com.szsicod.print.escpos.PrinterAPI;
import com.szsicod.print.log.AndroidLogCatStrategy;
import com.szsicod.print.log.Logger;
import com.szsicod.print.log.Utils;

import io.reactivex.plugins.RxJavaPlugins;

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
        ToastUtil.init(this);
        // 增加日志文件，true开启日志输出
        PrinterAPI.getInstance().setOutput(true);
        // 添加终端日志
        Logger.addLogStrategy(new AndroidLogCatStrategy());
        RxJavaPlugins.setErrorHandler(throwable -> Log.e("APP","Rxjava2 error:"+throwable.getMessage()));
    }



}
