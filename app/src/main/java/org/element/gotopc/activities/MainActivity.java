package org.element.gotopc.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;

import org.element.gotopc.R;
import org.element.gotopc.utils.DialogUtils;
import org.element.gotopc.utils.PermUtils;

import java.util.List;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks, EasyPermissions.RationaleCallbacks{

    public static final String LOG_TAG = "GTP";
    int aboutClickTimes = 0;
    boolean deniedPermsReq = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button_main_switch).setOnClickListener(view -> serviceSwitch());
        findViewById(R.id.button_main_choose_wifi).setOnClickListener(view -> startActivity(new Intent(this, ChooseWifiActivity.class)));
        findViewById(R.id.button_main_choose_app).setOnClickListener(view -> startActivity(new Intent(this, ChooseAppActivity.class)));
        findViewById(R.id.button_main_settings).setOnClickListener(view -> DialogUtils.showComingsoonDialog(this));
        findViewById(R.id.button_main_help).setOnClickListener(view -> DialogUtils.showComingsoonDialog(this));
        findViewById(R.id.button_main_about).setOnClickListener(view -> aboutEgg());
        findViewById(R.id.button_main_about).setOnLongClickListener(view -> {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://mtf.wiki")));
            return true;
        });

        if(!PermUtils.hasPerms(this)) PermUtils.acquirePerms(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        Log.i(LOG_TAG,"Perms granted successful");
        if(PermUtils.hasPerms(this)) Log.i(LOG_TAG, "All perms granted successful");
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if(!deniedPermsReq){
            if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
                new AppSettingsDialog.Builder(this).build().show();
            } else {
                PermUtils.acquirePerms(this);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) { super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE && !PermUtils.hasPerms(this)) {
            DialogUtils.showAlertDialog(this, R.string.error, R.string.cant_work_without_perms, null);
        }
    }

    @Override
    public void onRationaleAccepted(int requestCode) {
        Log.i(LOG_TAG,"start granting perms...");
    }

    @Override
    public void onRationaleDenied(int requestCode) {
        DialogUtils.showWithoutPermissionDialog(this);
        deniedPermsReq = true;
    }

    private void aboutEgg() {
        if (4 == aboutClickTimes) {
            DialogUtils.showAlertDialog(this, R.string.info,R.string.really_nothing, null);
            aboutClickTimes = 0;
        } else {
            aboutClickTimes++;
        }
    }

    private void serviceSwitch(){
        startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
    }
}