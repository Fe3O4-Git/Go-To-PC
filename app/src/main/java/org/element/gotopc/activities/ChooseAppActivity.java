package org.element.gotopc.activities;

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
import org.element.gotopc.utils.AppInfoUtils;

import java.util.HashSet;
import java.util.Set;

public class ChooseAppActivity extends BaseActivity {

    ConstraintLayout rootLayoutApp;
    LinearLayout linearLayoutApp;
    CheckBox showSystemAppsCheckbox;
    AppInfoUtils appInfoUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_app);

        rootLayoutApp = findViewById(R.id.layout_root_app);
        linearLayoutApp = findViewById(R.id.layout_liner_app);
        showSystemAppsCheckbox = findViewById(R.id.show_system_apps_checkbox);
        showSystemAppsCheckbox.setOnClickListener((view)-> refreshAppListUI());
        appInfoUtils = new AppInfoUtils(this);

        refreshAppListUI();
    }

    @Override
    public void finish() {
        saveCheckedApps();
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

    private void refreshAppListUI() {
        linearLayoutApp.removeAllViews();
        TextView textView = buildCenterText(getText(R.string.getting_app_list) + "\n" + getText(R.string.please_wait));
        rootLayoutApp.addView(textView);
        new Thread(() -> {
            Set<AppInfoUtils.AppInfo> appInfoSet = appInfoUtils.getAppList(showSystemAppsCheckbox.isChecked());
            runOnUiThread(() -> {
                rootLayoutApp.removeView(textView);
                refreshAppListItems(appInfoSet);
            });
        }).start();
    }

    private Set<String> getCheckedApps(){
        HashSet<String> checkedApps = new HashSet<>();
        for(int i = 0; i < linearLayoutApp.getChildCount(); i++){
            LinearLayout linearLayoutAppH = (LinearLayout) linearLayoutApp.getChildAt(i);
            CheckBox checkBox = (CheckBox) linearLayoutAppH.getChildAt(0);
            LinearLayout linearLayoutV = (LinearLayout) linearLayoutAppH.getChildAt(2);
            TextView textView = (TextView) linearLayoutV.getChildAt(1);
            if(checkBox.isChecked())
                checkedApps.add(textView.getText().toString());
        }
        return checkedApps;
    }

    private void saveCheckedApps(){
        Set<String> checkedApps = getCheckedApps();
        TextView textView = buildCenterText(getText(R.string.saving_app_list) + "\n" + getText(R.string.please_wait));
        linearLayoutApp.removeAllViews();
        rootLayoutApp.addView(textView);
        appInfoUtils.replaceDataSet(checkedApps);
        MyAccessibilityService myAccessibilityService = MyAccessibilityService.getInstance();
        if(myAccessibilityService != null)
            myAccessibilityService.setActionAppList(checkedApps);
    }

    private void refreshAppListItems(Set<AppInfoUtils.AppInfo> appInfoSet){
        Set<String> enabledApps = appInfoUtils.getDataSet();
        for(AppInfoUtils.AppInfo appInfo: appInfoSet) {
            CheckBox checkBox = new CheckBox(this);
            checkBox.setChecked(enabledApps.contains(appInfo.packageName));
            LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams1.gravity = Gravity.CENTER_VERTICAL;
            checkBox.setLayoutParams(layoutParams1);

            ImageView imageView = new ImageView(this);
            imageView.setImageDrawable(appInfo.icon);
            imageView.setLayoutParams(new RelativeLayout.LayoutParams(180, 180));

            TextView textView1 = new TextView(this);
            textView1.setText(appInfo.name);

            TextView textView2 = new TextView(this);
            textView2.setText(appInfo.packageName);

            LinearLayout linearLayoutV = new LinearLayout(this);
            linearLayoutV.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams2.gravity = Gravity.CENTER_VERTICAL;
            linearLayoutV.setLayoutParams(layoutParams2);
            linearLayoutV.addView(textView1);
            linearLayoutV.addView(textView2);

            LinearLayout linearLayoutH = new LinearLayout(this);
            linearLayoutH.addView(checkBox);
            linearLayoutH.addView(imageView);
            linearLayoutH.addView(linearLayoutV);
            linearLayoutH.setOnClickListener(view -> checkBox.setChecked(!checkBox.isChecked()));
            linearLayoutApp.addView(linearLayoutH);
        }
    }
}