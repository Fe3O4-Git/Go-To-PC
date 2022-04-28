package org.element.gotopc.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.element.gotopc.R;
import org.element.gotopc.utils.DialogUtils;
import org.element.gotopc.utils.WifiUtils;

import java.util.Set;

public class ChooseWifiActivity extends BaseActivity {

    LinearLayout linearLayoutWifi;
    WifiUtils wifiUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_wifi);

        linearLayoutWifi = findViewById(R.id.layout_liner_wifi);
        wifiUtils = new WifiUtils(this);

        findViewById(R.id.button_wifi_add).setOnClickListener(view -> {
            EditText editText = findViewById(R.id.edittext_wifi);
            String wifiSsid = editText.getText().toString();
            Set<String> wifiList = wifiUtils.getDataSet();
            if(wifiSsid.isEmpty())
                DialogUtils.showAlertDialog(this, R.string.error, R.string.wifi_ssid_cant_be_empty, null);
            else if(wifiList.contains(wifiSsid))
                DialogUtils.showAlertDialog(this, R.string.error, R.string.wifi_ssid_exist, null);
            else {
                addWifi(wifiSsid);
                editText.setText(null);
            }
        });
        findViewById(R.id.button_wifi_add_current).setOnClickListener(view -> {
            String wifiSsid = wifiUtils.getCurrentSSID();
            Set<String> wifiList = wifiUtils.getDataSet();
            if("<unknown ssid>".equals(wifiSsid))
                DialogUtils.showAlertDialog(this, R.string.error, R.string.cant_get_wifi_ssid, null);
            else if(wifiList.contains(wifiSsid))
                DialogUtils.showAlertDialog(this, R.string.error, R.string.wifi_ssid_exist, null);
            else
                addWifi(wifiSsid);
        });

        refreshUI();
    }

    private void addWifi(String wifiSsid){
        wifiUtils.updateDataSet(wifiSsid, WifiUtils.ADD);
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
            wifiUtils.updateDataSet((String) textView.getText(), WifiUtils.REMOVE);
        });

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.addView(textView);
        linearLayout.addView(button);
        linearLayoutWifi.addView(linearLayout);
    }

    public void refreshUI(){
        linearLayoutWifi.removeAllViews();
        for (String wifiSsid : wifiUtils.getDataSet())
            appendUI(wifiSsid);
    }
}