package com.emp.singlefpdemo;

import android.os.Environment;

import java.io.File;

public class AppConfig {

    public static final String WORK_PATH = Environment.getExternalStorageDirectory() + File.separator + "com.emp.singlefp";
    public static final String LOG_PATH = WORK_PATH + File.separator + "Log";
    public static final String SCAN_PATH = WORK_PATH + File.separator + "Scanner";
    public static final String FINGER_PATH = WORK_PATH + File.separator + "Finger";
    public static final String DATA_PATH = FINGER_PATH + File.separator + "data";
}
