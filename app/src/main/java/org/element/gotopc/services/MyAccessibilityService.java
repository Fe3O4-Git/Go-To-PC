package org.element.gotopc.services;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.Activity;
import android.content.Intent;
import android.view.accessibility.AccessibilityEvent;

import androidx.annotation.Nullable;

import org.element.gotopc.activities.BlockerActivity;
import org.element.gotopc.utils.WifiUtils;

import java.util.HashSet;
import java.util.Set;

public class MyAccessibilityService extends AccessibilityService {

    private static MyAccessibilityService myAccessibilityServiceInstance;
    private WifiUtils wifiUtils;

    @Override
    protected void onServiceConnected(){
        super.onServiceConnected();
        myAccessibilityServiceInstance = this;
        wifiUtils = new WifiUtils(this);
        setActionAppList();
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        String currentSSID = wifiUtils.getCurrentSSID();
        Set<String> wifiList = wifiUtils.getWifiList();
        if(wifiList.contains(currentSSID)) {
            Intent intent = new Intent(this, BlockerActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    @Override
    public void onInterrupt() {

    }

    @Override
    public boolean onUnbind(Intent intent) {
        myAccessibilityServiceInstance = null;
        return super.onUnbind(intent);
    }

    @Nullable
    public static MyAccessibilityService getInstance(){
        return myAccessibilityServiceInstance;
    }

    public void setActionAppList() {
        Set<String> appList;
        appList = new HashSet<>(getSharedPreferences("app_list", Activity.MODE_PRIVATE).getStringSet("default", new HashSet<>()));
        if(appList.size() == 0)
            appList.add("org.element.gotopc.never.allow");
        AccessibilityServiceInfo accessibilityServiceInfo = getServiceInfo();
        accessibilityServiceInfo.packageNames = appList.toArray(new String[0]);
        setServiceInfo(accessibilityServiceInfo);
    }
}
