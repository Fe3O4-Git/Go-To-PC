package org.element.gotopc.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class AppInfoUtils extends SharedPrefUtils {
    Context context;

    public static class AppInfo{
        public String name;
        public String packageName;
        public Drawable icon;
    }

    public AppInfoUtils(Context context) {
        super(context, "app_list");
        this.context = context;
    }

    public Set<AppInfo> getAppList(){
        PackageManager packageManager = context.getPackageManager();
        @SuppressLint("QueryPermissionsNeeded")
        List<ApplicationInfo> installedApplications = packageManager.getInstalledApplications(0);
        //noinspection ComparatorCombinators
        Set<AppInfo> appInfoSet = new TreeSet<>((appInfo1, appInfo2) -> appInfo1.name.compareTo(appInfo2.name));
        for(ApplicationInfo installedApplication: installedApplications){
            if((installedApplication.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                AppInfo appInfo = new AppInfo();
                appInfo.name = installedApplication.loadLabel(packageManager).toString();
                appInfo.packageName = installedApplication.packageName;
                appInfo.icon = installedApplication.loadIcon(packageManager);
                appInfoSet.add(appInfo);
            }
        }
        return appInfoSet;
    }
}
