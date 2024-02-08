package com.emp.singlefpdemo.managers;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.emp.xdcommon.android.log.Log;
import com.emp.xdcommon.android.log.LogUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BluetoothManager {

    private Context mContext;
    private String TAG = getClass().getSimpleName();
    private BluetoothReceiver bluetoothReceiver;
    private BlueCallBack bluetoothCallback;
    private BluetoothAdapter bluetoothAdapter;

    public BluetoothManager(Context context) {
        this.mContext = context;
    }

    public void setBlueCallback(BlueCallBack blueCallBack) {
        this.bluetoothCallback = blueCallBack;
    }

    public void start() {
        registerBluetoothReceiver();
        openBluetooth();
    }

    public void startDiscovery() {
        if (bluetoothAdapter != null && bluetoothAdapter.isEnabled()) {
            bluetoothAdapter.startDiscovery();
            bluetoothCallback.onMessage("正在搜索蓝牙设备");
        }
    }

    public Set<BluetoothDevice> getBondedDevices() {
        if (bluetoothAdapter != null && bluetoothAdapter.isEnabled()) {
            return bluetoothAdapter.getBondedDevices();
        }
        return null;
    }


    //检查已连接的蓝牙设备
//    public void getConnectBt() {
//        if (bluetoothAdapter == null)
//            return;
//        int a2dp = bluetoothAdapter.getProfileConnectionState(BluetoothProfile.A2DP);
//        int headset = bluetoothAdapter.getProfileConnectionState(BluetoothProfile.HEADSET);
//        int health = bluetoothAdapter.getProfileConnectionState(BluetoothProfile.HEALTH);
//        int flag = -1;
//        if (a2dp == BluetoothProfile.STATE_CONNECTED) {
//            flag = a2dp;
//        } else if (headset == BluetoothProfile.STATE_CONNECTED) {
//            flag = headset;
//        } else if (health == BluetoothProfile.STATE_CONNECTED) {
//            flag = health;
//        }
//        if (flag != -1) {
//            bluetoothAdapter.getProfileProxy(mContext, new BluetoothProfile.ServiceListener() {
//                @Override
//                public void onServiceDisconnected(int profile) {
//                }
//
//                @Override
//                public void onServiceConnected(int profile, BluetoothProfile proxy) {
//                    List<BluetoothDevice> mDevices = proxy.getConnectedDevices();
//
//                }
//            }, flag);
//        }
//    }

    //检查已连接的蓝牙设备
    public void getConnectedBt() {
        if (bluetoothAdapter == null)
            return;
        List<BluetoothDevice> mDevices = new ArrayList<>();
        Set<BluetoothDevice> bondedDevices = bluetoothAdapter.getBondedDevices();
        try {
            Method isConnected = BluetoothDevice.class.getDeclaredMethod("isConnected", (Class[]) null);
            isConnected.setAccessible(true);
            for (BluetoothDevice device : bondedDevices) {
                boolean connect = (boolean) isConnected.invoke(device, (Object[]) null);
                if (connect) {
                    mDevices.add(device);
                }
            }
            if (bluetoothCallback != null) {
                bluetoothCallback.onFoundConnectDevices(mDevices);
            }

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private void openBluetooth() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter != null) {
            if (!bluetoothAdapter.isEnabled()) {
                boolean enable = bluetoothAdapter.enable();
                if (bluetoothCallback != null) {
                    bluetoothCallback.onTurnOn(enable);
                }
            } else {
                if (bluetoothCallback != null) {
                    bluetoothCallback.onTurnOn(true);
                }
            }
        } else {
            LogUtils.e(TAG, "不支持蓝牙设备");
            if (bluetoothCallback != null) {
                bluetoothCallback.onMessage("不支持蓝牙设备");
            }
        }
    }

    public void stop() {
        if (bluetoothAdapter != null) {
            bluetoothAdapter.cancelDiscovery();
            bluetoothAdapter = null;
        }
        bluetoothCallback = null;
        unRegisterBluetoothReceiver();
    }

    private IntentFilter makeFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);//蓝牙状态改变的广播
        filter.addAction(BluetoothDevice.ACTION_FOUND);//找到设备的广播
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);//搜索完成的广播
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);//开始扫描的广播
        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);//绑定状态改变的广播
        return filter;
    }

    public void registerBluetoothReceiver() {
        if (bluetoothReceiver == null) {
            bluetoothReceiver = new BluetoothReceiver();
        }
        addLog("registerBluetoothReceiver", false);
        mContext.registerReceiver(bluetoothReceiver, makeFilter());
    }

    public void unRegisterBluetoothReceiver() {
        if (bluetoothReceiver != null) {
            addLog("unRegisterBluetoothReceiver", false);
            mContext.unregisterReceiver(bluetoothReceiver);
            bluetoothReceiver = null;
        }
    }

    class BluetoothReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.e(TAG, "action：" + action);
            switch (action) {
                case BluetoothAdapter.ACTION_STATE_CHANGED:
                case BluetoothDevice.ACTION_BOND_STATE_CHANGED:
                    getConnectedBt();
                    break;
                case BluetoothDevice.ACTION_FOUND:
                    Log.e(TAG, "ACTION_FOUND");
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    if (bluetoothCallback != null) {
                        bluetoothCallback.onDeviceFound(device);
                    }
                    break;
                case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
                    if (bluetoothCallback != null) {
                        bluetoothCallback.onFondFinish();
                        addLog("搜索蓝牙完成", false);
                    }
                    break;
                case BluetoothAdapter.ACTION_DISCOVERY_STARTED:
                    addLog("接收到发现开始的广播", true);
                    break;
            }
        }
    }

    private void addLog(String log, boolean file) {
        LogUtils.e(TAG, log);
    }


    public interface BlueCallBack {
        void onTurnOn(boolean status);

        void onTurnOff(int status);

        void onDeviceFound(BluetoothDevice bluetoothDevice);

        void onFondFinish();

        void onMessage(String msg);

        void onFoundConnectDevices(List<BluetoothDevice> devices);
    }
}
