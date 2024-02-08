package com.emp.printerdemo.ui;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.emp.printerdemo.R;
import com.emp.printerdemo.base.BaseActivity;
import com.emp.printerdemo.managers.BluetoothManager;
//import com.emp.singlefpdemo.SFingerActivity;
import com.emp.xdcommon.android.base.CommonAdapter;
import com.emp.xdcommon.android.base.ViewHolder;
import com.emp.xdcommon.common.utils.ToastUtil;
import com.szsicod.print.escpos.PrinterAPI;
import com.szsicod.print.io.BluetoothAPI;
import com.szsicod.print.utils.BitmapUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class PrintActivity extends BaseActivity implements BluetoothManager.BlueCallBack {

    private static final int MSG_CONNECT = 0x0100;
//    @BindView(R.id.blutoothLv)
    ListView lv;
//    @BindView(R.id.deviceTv)
    TextView deviceTv;
//    @BindView(R.id.print)
    Button printBtn;
//    @BindView(R.id.bar)
    Button bar;
//    @BindView(R.id.qr)
    Button qr;
//    @BindView(R.id.printCircle)
    Button printCircleBtn;
//    @BindView(R.id.connectLv)
    ListView connectLv;
//    @BindView(R.id.search)
    Button searchBtn;
//    @BindView(R.id.probar)
    ProgressBar progressBar;
//    @BindView(R.id.resTv)
    TextView resTv;
//    @BindView(R.id.pt_et)
    EditText ptEt;

    private Bitmap test_bitmap;
    private BluetoothManager bluetoothManager;
    private List<BluetoothDevice> devices = new ArrayList<>();
    private CommonAdapter<BluetoothDevice> commonAdapter;
    private ProgressDialog mDialog;
    private CompositeDisposable compositeDisposable;
    private CommonAdapter<BluetoothDevice> connectAdapter;
    private List<BluetoothDevice> connectDevices = new ArrayList<>();
    private boolean isRunning;
    private PrinterAPI mPrinter;
    private String TAG=getClass().getSimpleName();

    String firstname, surname, school, tesis, uid;

    @Override
    protected void initViews(Bundle savedInstanceState) {

        Intent i = getIntent();
        firstname = i.getStringExtra("Firstname");
        surname = i.getStringExtra("Surname");
        school = i.getStringExtra("School");
        tesis = i.getStringExtra("Tesisnumber");
        uid = i.getStringExtra("userid");

        lv = findViewById(R.id.blutoothLv);
        deviceTv = findViewById(R.id.deviceTv);
        printBtn = findViewById(R.id.print);
        bar = findViewById(R.id.bar);
        qr = findViewById(R.id.qr);
        printCircleBtn = findViewById(R.id.printCircle);
        connectLv = findViewById(R.id.connectLv);
        searchBtn = findViewById(R.id.search);
        progressBar = findViewById(R.id.probar);
        resTv = findViewById(R.id.resTv);
        ptEt = findViewById(R.id.pt_et);

        printBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                printBTN();
            }
        });
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchBTN();
            }
        });
        printCircleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                printCircleBTN();
            }
        });
        bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                barBTN();
            }
        });
        qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qrBTN();
            }
        });


        bluetoothManager = new BluetoothManager(this);
        bluetoothManager.setBlueCallback(this);

        compositeDisposable = new CompositeDisposable();
        commonAdapter = new CommonAdapter<BluetoothDevice>(this, devices, R.layout.deviceitem) {
            @RequiresApi(api = Build.VERSION_CODES.ECLAIR)
            @Override
            public void convert(ViewHolder helper, int position, BluetoothDevice item) {
                helper.setText(R.id.devicename, TextUtils.isEmpty(item.getName()) ? "unknown device" : item.getName());
                helper.setText(R.id.mac, item.getAddress());
                helper.getView(R.id.connect).setOnClickListener(v -> {
                    startLoadding("connecting...");
                    String address = item.getAddress();
                    deviceTv.setText(address + " is connecting...");
                    connectDevice(address);
                });
            }
        };
        lv.setAdapter(commonAdapter);

        connectAdapter = new CommonAdapter<BluetoothDevice>(this, connectDevices, R.layout.deviceitem) {
            @RequiresApi(api = Build.VERSION_CODES.ECLAIR)
            @Override
            public void convert(ViewHolder helper, int position, BluetoothDevice item) {
                helper.setText(R.id.devicename, TextUtils.isEmpty(item.getName()) ? "unknown device" : item.getName());
                helper.setText(R.id.mac, item.getAddress());
                helper.getView(R.id.connect).setOnClickListener(v -> {
                    startLoadding("connecting...");
                    String address = item.getAddress();
                    deviceTv.setText(address + " is connecting...");
                    connectDevice(address);
                });
            }
        };
        TextView emptyView = new TextView(this);
        emptyView.setText("未连接任何设备");
        connectLv.setEmptyView(emptyView);
        connectLv.setAdapter(connectAdapter);

        mPrinter = PrinterAPI.getInstance();
        int printWidth = (58 - 10) * 8;
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.printer_test);
        test_bitmap = BitmapUtils.reSize(bitmap, printWidth, bitmap.getHeight() * printWidth / bitmap.getWidth());
    }

    private void connectDevice(String address) {
        Disposable disposable = Observable.create((ObservableOnSubscribe<Boolean>) emitter -> {

            if (mPrinter.isConnect()) {
                mPrinter.disconnect();
            }
            BluetoothAPI mBluetoothApi = new BluetoothAPI(PrintActivity.this);
            mBluetoothApi.setPin("0000");//我厂自动配对的;
            mBluetoothApi.setPairStrings(new String[]{"BTPrinter"});//设置自动匹配的设备名
            mBluetoothApi.checkDevice(address);
            int ret = mPrinter.connect(mBluetoothApi);
            emitter.onNext(ret == PrinterAPI.SUCCESS);
            emitter.onComplete();
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aBoolean -> {
                    if (aBoolean) {
                        deviceTv.setText(address + " has connected");
                        deviceTv.setTextColor(getResources().getColor(R.color.green));
                        printBtn.setEnabled(true);
                        bar.setEnabled(true);
                        qr.setEnabled(true);
                        printCircleBtn.setEnabled(true);
                        lv.setVisibility(View.GONE);
                    } else {
                        deviceTv.setText(address + " connect failed");
                        deviceTv.setTextColor(getResources().getColor(R.color.red));
                    }
                    stopLoadding();
                });
        compositeDisposable.add(disposable);
    }


    public void startLoadding(String content) {
        if (mDialog == null) {
            mDialog = new ProgressDialog(this);
            mDialog.setCancelable(false);
        }
        mDialog.setMessage(content);
        if (!mDialog.isShowing()) {
            mDialog.show();
        }
    }

    public void stopLoadding() {
        if (mDialog != null && !isFinishing()) {
            mDialog.dismiss();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        bluetoothManager.getConnectedBt();
    }

    private void printBTN(){
        printBtn.setEnabled(false);
        printPicture();
    }
    private void searchBTN(){
        searchBtn.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);
        lv.setVisibility(View.VISIBLE);
        bluetoothManager.startDiscovery();
    }
    private void printCircleBTN(){
        isRunning = !isRunning;
        if(isRunning){
            funcPrintText();
//            showEndDialog();
//            finish();
//            onBackPressed();
        }else {
            compositeDisposable.clear();
        }
    }
    private void barBTN(){
        funcPrintBarcode();
    }
    private void qrBTN(){
        funcPrintQrcode();
    }

//    private void showEndDialog() {
//        Dialog myDialog = new Dialog(PrintActivity.this);
//        myDialog.setContentView(R.layout.custom_popup_logout);
//        Button addStudent = myDialog.findViewById(R.id.btn_addstudent);
//        Button end = myDialog.findViewById(R.id.btn_end);
//
//        addStudent.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                myDialog.dismiss();
//                reloadActivity();
//            }
//        });
//        end.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                myDialog.dismiss();
//                reloadActivity();
//            }
//        });
//
//        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        myDialog.setCanceledOnTouchOutside(false);
//        myDialog.show();
//    }

    private void funcPrintText() {
        String text = "Name: "+firstname+" "+surname+"\nSchool: "+school+"\nTESIS No: "+tesis;

        if(TextUtils.isEmpty(text)){
            ToastUtil.shortTips("please input print text");
            return;
        }
        Disposable disposable = Observable.create((ObservableOnSubscribe<Boolean>) emitter -> {
                    int m = 73;
                    mPrinter.sendOrder(new byte[]{0x0a, 0x1d, 0x48, 0x00});     // 条码内容在下方
                    mPrinter.setAlignMode(0);                                   // 居左
                    mPrinter.printString(text);
                    mPrinter.printFeed();
                    int ret = mPrinter.cutPaper(66, 0);
                    if (ret == PrinterAPI.SUCCESS)
                        emitter.onNext(true);
                    else emitter.onNext(false);
                    emitter.onComplete();
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aBoolean -> {
                    if (aBoolean) {
                        ToastUtil.shortTips("打印成功");
                    } else ToastUtil.shortTips("打印失败");
                    bar.setEnabled(true);
                });
        compositeDisposable.add(disposable);

        Intent intent = new Intent();
        intent.putExtra("from", "printer");
        setResult(1, intent);
        finish();
    }

//    @Override
//    public void onBackPressed() {
////        super.onBackPressed();
//        Intent intent = new Intent();
//        intent.putExtra("from", "printer");
//        setResult(1, intent);
//        finish();
//    }

    // 打印一维码
    private void funcPrintBarcode() {

        String text = firstname+" "+surname+"\n"+school+"\n"+tesis;

        if(TextUtils.isEmpty(text)){
            ToastUtil.shortTips("please input print text");
            return;
        }
        bar.setEnabled(false);
        Disposable disposable = Observable.create((ObservableOnSubscribe<Boolean>) emitter -> {
            int m = 73;
            mPrinter.sendOrder(new byte[]{0x0a, 0x1d, 0x48, 0x00});     // 条码内容在下方
            mPrinter.setAlignMode(0);                                   // 居左
            mPrinter.printBarCode(m, text.length(), text);     // 打印条码
//                    mPrinter.setAlignMode(1);                                   // 居中
//                    mPrinter.printBarCode(m, barStr.length(), barStr);      // 打印条码
//                    mPrinter.setAlignMode(2);                                   // 居右
//                    mPrinter.printBarCode(m, barStr.length(), barStr);      // 打印条码
//                    mPrinter.setAlignMode(0);
            mPrinter.printFeed();
            int ret = mPrinter.cutPaper(66, 0);
            if (ret == PrinterAPI.SUCCESS)
                emitter.onNext(true);
            else emitter.onNext(false);
            emitter.onComplete();
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aBoolean -> {
                    if (aBoolean) {
                        ToastUtil.shortTips("打印成功");
                    } else ToastUtil.shortTips("打印失败");
                    bar.setEnabled(true);
                });
        compositeDisposable.add(disposable);
    }

    // 打印二维码
    private void funcPrintQrcode() {

        String text = firstname+" "+surname+"\n"+school+"\n"+tesis;

        if(TextUtils.isEmpty(text)){
            ToastUtil.shortTips("please input print text");
            return;
        }
        qr.setEnabled(false);
        Disposable disposable = Observable.create((ObservableOnSubscribe<Boolean>) emitter -> {
            mPrinter.setAlignMode(1);                                 // 居中
            mPrinter.printQRCode(text, 5, false);
            mPrinter.setAlignMode(0);                                   // 居左
            int ret = mPrinter.cutPaper(66, 0);
            if (ret == PrinterAPI.SUCCESS)
                emitter.onNext(true);
            else emitter.onNext(false);
            emitter.onComplete();
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aBoolean -> {
                    if (aBoolean) {
                        ToastUtil.shortTips("打印成功");
                    } else ToastUtil.shortTips("打印失败");
                    qr.setEnabled(true);
                });
        compositeDisposable.add(disposable);
    }

    // 打印图片
    public void printPicture() {
        Disposable disposable = Observable.create((ObservableOnSubscribe<Boolean>) emitter -> {
            mPrinter.printRasterBitmap(test_bitmap);
            int ret = mPrinter.cutPaper(66, 0);
            if (ret == PrinterAPI.SUCCESS)
                emitter.onNext(true);
            else emitter.onNext(false);
            emitter.onComplete();
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aBoolean -> {
                    if (aBoolean) {
                        ToastUtil.shortTips("打印成功");
                    } else ToastUtil.shortTips("打印失败");
                    printBtn.setEnabled(true);
                });
        compositeDisposable.add(disposable);
    }

    public void printPictureAlways() {
        final int[] resCount = {0, 0};
        Disposable disposable = Observable.create((ObservableOnSubscribe<Boolean>) emitter -> {
            while (isRunning) {
                Log.e(TAG, "打印指令发送完成");
                mPrinter.printRasterBitmap(test_bitmap);
                int ret = mPrinter.cutPaper(66, 0);
                if (ret == PrinterAPI.SUCCESS)
                    emitter.onNext(true);
                else emitter.onNext(false);
                Thread.sleep(6000);
            }
            emitter.onComplete();
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aBoolean -> {
                    if (aBoolean) {
                        resCount[0]++;
                        ToastUtil.shortTips("打印成功");
                    } else {
                        resCount[1]++;
                        ToastUtil.shortTips("打印失败");
                    }
                    resTv.setText("times of success:" + resCount[0] + "  times of fail:" + resCount[1]);
                });
        compositeDisposable.add(disposable);
    }

    @Override
    public void onTurnOn(boolean status) {
        ToastUtil.shortTips("bluetooth open:" + status);
    }

    @Override
    public void onTurnOff(int status) {

    }

    @RequiresApi(api = Build.VERSION_CODES.ECLAIR)
    @Override
    public void onDeviceFound(BluetoothDevice bluetoothDevice) {
        if ("40680".equals(bluetoothDevice.getBluetoothClass().toString())) {
            boolean hasDevice = false;
            for (BluetoothDevice device : devices) {
                if (device.getAddress().equals(bluetoothDevice.getAddress())) {
                    hasDevice = true;
                    break;
                }
            }
            if (!hasDevice)
                devices.add(bluetoothDevice);
            commonAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onFondFinish() {
        ToastUtil.shortTips("search finish!");
        if (devices.size() <= 0) {
            ToastUtil.shortTips("no found bluetooth printer");
        }
        searchBtn.setEnabled(true);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onMessage(String msg) {
        ToastUtil.shortTips(msg);
    }

    @RequiresApi(api = Build.VERSION_CODES.ECLAIR)
    @Override
    public void onFoundConnectDevices(List<BluetoothDevice> devices) {
        connectDevices.clear();
        for (int i = 0; i < devices.size(); i++) {
            BluetoothDevice device = devices.get(i);
            if ("40680".equals(device.getBluetoothClass().toString())) {
                connectDevices.add(device);
            }
        }
        connectAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        if (compositeDisposable != null) {
            compositeDisposable.clear();
        }
        if (mPrinter.isConnect())
            mPrinter.disconnect();
        bluetoothManager.stop();
        super.onDestroy();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_print;
    }

    @Override
    protected void initData() {
        bluetoothManager.start();
    }
}