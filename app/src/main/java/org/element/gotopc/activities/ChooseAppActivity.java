package org.element.gotopc.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import org.element.gotopc.R;
import org.element.gotopc.services.MyAccessibilityService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class ChooseAppActivity extends BaseActivity {

    ConstraintLayout rootLayoutApp;
    LinearLayout linearLayoutApp;
    SharedPreferences sharedPreferences;

    private static class AppInfo{
        private String name;
        private String packageName;
        private Drawable icon;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_app);

        sharedPreferences = getSharedPreferences("app_list", Activity.MODE_PRIVATE);
        rootLayoutApp = findViewById(R.id.layout_root_app);
        linearLayoutApp = findViewById(R.id.layout_liner_app);

        TextView textView = buildCenterText(getText(R.string.getting_app_list) + "\n" + getText(R.string.please_wait));
        rootLayoutApp.addView(textView);
        new Thread(() -> {
            Set<AppInfo> appInfoSet = getAppList();
            runOnUiThread(() -> {
                rootLayoutApp.removeView(textView);
                refreshAppListUI(appInfoSet);
            });
        }).start();
    }

    @Override
    public void finish() {
        saveEnabledApp();
        MyAccessibilityService myAccessibilityService = MyAccessibilityService.getInstance();
        if(myAccessibilityService != null)
            myAccessibilityService.setActionAppList();
        super.finish();
    }

    private TextView buildCenterText(String string){
        TextView textView = new TextView(this);
        textView.setText(string);
        textView.setTextSize(18);
        textView.setGravity(Gravity.CENTER);
        textView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        return textView;
    }

    private Set<AppInfo> getAppList(){
        PackageManager packageManager = getPackageManager();
        @SuppressLint("QueryPermissionsNeeded")
        List<ApplicationInfo> installedApplications = packageManager.getInstalledApplications(0);
        //noinspection ComparatorCombinators
        Set<AppInfo> appInfoSet = new TreeSet<>((appInfo, appInfo1) -> appInfo.name.compareTo(appInfo1.name));
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

    private Set<String> getEnabledApp(){
        HashSet<String> apps = new HashSet<>();
        for(int i = 0; i < linearLayoutApp.getChildCount(); i++){
            LinearLayout linearLayoutAppH = (LinearLayout) linearLayoutApp.getChildAt(i);
            CheckBox checkBox = (CheckBox) linearLayoutAppH.getChildAt(0);
            LinearLayout linearLayoutV = (LinearLayout) linearLayoutAppH.getChildAt(2);
            TextView textView = (TextView) linearLayoutV.getChildAt(1);
            if(checkBox.isChecked())
                apps.add(textView.getText().toString());
        }
        return apps;
    }

    private void saveEnabledApp(){
        Set<String> enabledApps = getEnabledApp();
        TextView textView = buildCenterText(getText(R.string.saving_app_list) + "\n" + getText(R.string.please_wait));
        linearLayoutApp.removeAllViews();
        rootLayoutApp.addView(textView);
        sharedPreferences.edit()
                .putStringSet("default", enabledApps)
                .apply();
        rootLayoutApp.removeView(textView);
        textView = buildCenterText(getText(R.string.really_nothing).toString());
        rootLayoutApp.addView(textView);
    }

    private void refreshAppListUI(Set<AppInfo> appInfoSet){
        Set<String> enabledApps = sharedPreferences.getStringSet("default",new HashSet<>());
        for(AppInfo appInfo: appInfoSet) {
            CheckBox checkBox = new CheckBox(this);
            checkBox.setChecked(enabledApps.contains(appInfo.packageName));
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.gravity = Gravity.CENTER_VERTICAL;
            checkBox.setLayoutParams(layoutParams);

            ImageView imageView = new ImageView(this);
            imageView.setImageDrawable(appInfo.icon);
            imageView.setLayoutParams(new RelativeLayout.LayoutParams(180, 180));

            TextView textView = new TextView(this);
            textView.setText(appInfo.name);

            TextView textView1 = new TextView(this);
            textView1.setText(appInfo.packageName);

            LinearLayout linearLayoutV = new LinearLayout(this);
            linearLayoutV.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams1.gravity = Gravity.CENTER_VERTICAL;
            linearLayoutV.setLayoutParams(layoutParams1);
            linearLayoutV.addView(textView);
            linearLayoutV.addView(textView1);

            LinearLayout linearLayoutH = new LinearLayout(this);
            linearLayoutH.addView(checkBox);
            linearLayoutH.addView(imageView);
            linearLayoutH.addView(linearLayoutV);
            linearLayoutH.setOnClickListener(view -> checkBox.setChecked(!checkBox.isChecked()));
            linearLayoutApp.addView(linearLayoutH);
        }
    }
}