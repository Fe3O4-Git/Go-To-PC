package org.element.gotopc.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

public abstract class SharedPrefUtils {
    public static final int ADD = 1, REMOVE = 2;
    final SharedPreferences sharedPreferences;
    final SharedPreferences.Editor editor;

    public SharedPrefUtils(Context context, String sharedPrefName) {
        sharedPreferences = context.getSharedPreferences(sharedPrefName, Activity.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public Set<String> getDataSet() {
        Set<String> dataSet = sharedPreferences.getStringSet("default", null);
        if(dataSet != null)
            return dataSet;
        else
            return new HashSet<>();
    }

    public void updateDataSet(String item, int operation){
        Set<String> dataSet = getDataSet();
        if(operation == ADD)
            dataSet.add(item);
        else if(operation == REMOVE)
            dataSet.remove(item);
        editor.putStringSet("default", dataSet);
        editor.apply();
    }

    public void replaceDataSet(Set<String> dataSet) {
        editor.putStringSet("default", dataSet);
        editor.apply();
    }
}
