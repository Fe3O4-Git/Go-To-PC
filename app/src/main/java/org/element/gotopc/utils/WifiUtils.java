package org.element.gotopc.utils;

import android.content.Context;
import android.net.wifi.WifiManager;

public class WifiUtils extends SharedPrefUtils {
    final WifiManager wifiManager;

    public WifiUtils(Context context) {
        super(context, "wifi_list");
        wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
    }

    public String getCurrentSSID() {
        return wifiManager.getConnectionInfo().getSSID().replace("\"", "");
    }
}
