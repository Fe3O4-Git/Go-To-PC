package org.element.gotopc.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;

import java.util.HashSet;
import java.util.Set;

public class WifiUtils {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    WifiManager wifiManager;
    public static final int ADD = 1, REMOVE =2;

    public WifiUtils(Context context) {
        sharedPreferences = context.getSharedPreferences("wifi_list", Activity.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
    }

    public Set<String> getWifiList() {
        Set<String> wifiList = sharedPreferences.getStringSet("default", null);
        if(wifiList != null)
            return wifiList;
        else
            return new HashSet<>();
    }

    public void updateWifiList(String wifiSSID, int operation){
        Set<String> wifiList = getWifiList();
        if(operation == ADD)
            wifiList.add(wifiSSID);
        else if(operation == REMOVE)
            wifiList.remove(wifiSSID);
        editor.putStringSet("default", wifiList);
        editor.apply();
    }

    public String getCurrentSSID() {
        return wifiManager.getConnectionInfo().getSSID().replace("\"", "");
    }
}
