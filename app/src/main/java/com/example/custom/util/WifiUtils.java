package com.example.custom.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import java.util.List;

//Manifest.permission.ACCESS_WIFI_STATE,
//Manifest.permission.CHANGE_WIFI_STATE,

public class WifiUtils {
    private final static String TAG = "WifiUtils";
    private WifiManager mWifiManager;
    private WifiInfo mWifiInfo;
    private List<ScanResult> mWifiList;
    private List<WifiConfiguration> mWifiConfiguration;
    private WifiManager.WifiLock mWifiLock;

    public WifiUtils(Context context) {
        mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        mWifiInfo = mWifiManager.getConnectionInfo();
    }

    public void openWifi() {
        Log.e(TAG, "openWifi state: " + mWifiManager.getWifiState());
        if (!mWifiManager.isWifiEnabled()) {
            mWifiManager.setWifiEnabled(true);
        } else {
            Log.e(TAG, "openWifi state: already open");
        }
    }

    public void closeWifi() {
        Log.e(TAG, "close Wifi state: " + mWifiManager.getWifiState());
        if (mWifiManager.isWifiEnabled()) {
            mWifiManager.setWifiEnabled(true);
        } else {
            Log.e(TAG, "closeWifi state: already close");
        }
    }

    public void checkWifiState(Context context) {
        switch (mWifiManager.getWifiState()) {
            case 0:
                Log.e(TAG, "checkWifiState: Wifi is closing");
                break;
            case 1:
                Log.e(TAG, "checkWifiState: Wifi is already closed");
                break;
            case 2:
                Log.e(TAG, "checkWifiState: Wifi is openning");
                break;
            case 3:
                Log.e(TAG, "checkWifiState: Wifi is already opened");
                break;
            default:
                Log.e(TAG, "checkWifiState: have not got Wifi state");
                break;
        }
    }

    public void createWifiLock() {
        mWifiLock = mWifiManager.createWifiLock("WifiUtils");
    }

    // 锁定WifiLock
    public void acquireWifiLock() {
        mWifiLock.acquire();
    }

    public void releaseWifiLock() {
        if (mWifiLock.isHeld()) {
            mWifiLock.release();
        }
    }

    public List<WifiConfiguration> getConfiguration() {
        return mWifiConfiguration;
    }

    // 连接至列表中的Wifi，index是其在mWifiConfiguration中的索引
    public boolean connectToWifiConfigurationList(int index) {
        if (index > mWifiConfiguration.size()) {
            Log.e(TAG, "connectToWifiConfigurationList: index is out of boundary");
            return false;
        }

        return mWifiManager.enableNetwork(mWifiConfiguration.get(index).networkId,
                true);
    }

    public void startScan(Context context) {
        mWifiManager.startScan();
        // 得到扫描结果,隐藏SSID将为“”,可能有重复SSID。
        mWifiList = mWifiManager.getScanResults();
        // 得到配置好的网络连接
        mWifiConfiguration = mWifiManager.getConfiguredNetworks();
        if (mWifiList == null) {
            switch (mWifiManager.getWifiState()) {
                case 3:
                    Log.e(TAG, "startScan: current area has no wifi");
                    break;
                case 2:
                    Log.e(TAG, "startScan: Wifi is openning, please rescan later");
                    break;
                default :
                    Log.e(TAG, "startScan: Wifi is closed, can't scan");
                    break;
            }
        }
    }

    // 得到Wifi列表
    public List<ScanResult> getmWifiList() {
        return mWifiList;
    }

    public String getMacAddress() {
        return (mWifiInfo == null) ? "NULL" : mWifiInfo.getMacAddress();
    }

    public String getBSSID() {
        return (mWifiInfo == null) ? "NULL" : mWifiInfo.getBSSID();
    }

    public int getIPAddress() {
        return (mWifiInfo == null) ? 0 : mWifiInfo.getIpAddress();
    }

    public int getNetworkId() {
        return (mWifiInfo == null) ? 0 : mWifiInfo.getNetworkId();
    }

    // 获取WifiInfo的所有信息包
    public String getWifiInfo() {
        return (mWifiInfo == null) ? "NULL" : mWifiInfo.toString();
    }

    // 连接到指定网络
    public boolean addNetwork(WifiConfiguration wcf) {
        int wcfID = mWifiManager.addNetwork(wcf);
        Log.e(TAG, "connectToWifi ID: " + wcfID);
        return addNetwork(wcfID);
    }
    public boolean addNetwork(int netID) {
        return mWifiManager.enableNetwork(netID, true);
    }

    public void disconnectFromWifi(int netID) {
        mWifiManager.disableNetwork(netID);
        mWifiManager.disconnect();
    }

    public void removeWifi(int netID) {
        disconnectFromWifi(netID);
        mWifiManager.removeNetwork(netID);
    }

    /**
     * 需要权限 android.permission.ACCESS_NETWORK_STATE
     * M:Check if device has a network connection (wifi or data)
     * @param context
     * @return true if network connected
     */
    public static boolean hasConnectivity(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            NetworkInfo.DetailedState state = info.getDetailedState();
            if (state == NetworkInfo.DetailedState.CONNECTED) {
                return true;
            }
        }
        return false;
    }
}