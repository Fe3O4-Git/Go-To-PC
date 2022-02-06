package org.element.gotopc.activities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.element.gotopc.R;
import org.element.gotopc.utils.DialogUtils;

import java.util.HashSet;
import java.util.Set;

public class ChooseWifiActivity extends BaseActivity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    LinearLayout linearLayoutWifi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_wifi);

        sharedPreferences = getSharedPreferences("wifi_list", Activity.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        linearLayoutWifi = findViewById(R.id.layout_liner_wifi);

        findViewById(R.id.button_wifi_add).setOnClickListener(view -> {
            EditText editText = findViewById(R.id.edittext_wifi);
            String wifiSsid = editText.getText().toString();
            if(wifiSsid.isEmpty())
                DialogUtils.showAlertDialog(this, R.string.error, R.string.wifi_ssid_cant_be_empty, null);
            else if(readWifiList().contains(wifiSsid))
                DialogUtils.showAlertDialog(this, R.string.error, R.string.wifi_ssid_exist, null);
            else {
                addWifi(wifiSsid, readWifiList());
                editText.setText(null);
            }
        });
        findViewById(R.id.button_wifi_add_current).setOnClickListener(view -> {
            WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            String wifiSsid = wifiInfo.getSSID().replace("\"", "");
            if("<unknown ssid>".equals(wifiSsid))
                DialogUtils.showAlertDialog(this, R.string.error, R.string.cant_get_wifi_ssid, null);
            else if(readWifiList().contains(wifiSsid))
                DialogUtils.showAlertDialog(this, R.string.error, R.string.wifi_ssid_exist, null);
            else
                addWifi(wifiSsid, readWifiList());
        });

        refreshUI();
    }

    private Set<String> readWifiList() {
        Set<String> wifiList = sharedPreferences.getStringSet("default", null);
        if(wifiList != null)
            return wifiList;
        else
            return new HashSet<>();
    }

    private void writeWifiList(Set <String> wifiList){
        editor.putStringSet("default", wifiList);
        editor.apply();
    }

    private void addWifi(String wifiSsid, Set<String> wifiList){
        wifiList.add(wifiSsid);
        writeWifiList(wifiList);
        appendUI(wifiSsid);
    }

    public void appendUI(String wifiSsid) {
        LinearLayout linearLayoutWifi = findViewById(R.id.layout_liner_wifi);

        TextView textView = new TextView(this);
        textView.setText(wifiSsid);
        textView.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

        Button button = new Button(this);
        button.setText(R.string.remove);
        button.setOnClickListener(view -> {
            LinearLayout linearLayout = (LinearLayout) view.getParent();
            linearLayoutWifi.removeView(linearLayout);
            Set<String> wifiList = readWifiList();
            //noinspection SuspiciousMethodCalls
            wifiList.remove(textView.getText());
            writeWifiList(wifiList);
        });

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.addView(textView);
        linearLayout.addView(button);
        linearLayoutWifi.addView(linearLayout);
    }

    public void refreshUI(){
        linearLayoutWifi.removeAllViews();
        for (String wifiSsid : readWifiList())
            appendUI(wifiSsid);
    }
}