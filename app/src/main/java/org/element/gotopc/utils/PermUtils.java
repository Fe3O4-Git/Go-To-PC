package org.element.gotopc.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;

import org.element.gotopc.R;

import pub.devrel.easypermissions.EasyPermissions;

public class PermUtils {

    private static final int BASE_PERMS = 0;
    public static final String[] perms = {Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};

    public static boolean hasPerms(Context context){
        return EasyPermissions.hasPermissions(context, perms);
    }

    public static void acquirePerms(Context context) {
        String tip = String.format("%s%n%s%n%s%n(%s)",
                context.getString(R.string.require_perms),
                context.getString(R.string.click_ok_to_continue),
                context.getString(R.string.click_cancel_to_abort),
                context.getString(R.string.why_location_perms));
        EasyPermissions.requestPermissions((Activity) context, tip, BASE_PERMS, perms);
    }
}
