package org.element.gotopc.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import org.element.gotopc.R;

public class DialogUtils {

    public static void showAlertDialog(Context context, int title, int message, DialogInterface.OnClickListener onClickListener){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.ok, onClickListener)
                .show();
    }

    public static void showComingsoonDialog(Context context){
        showAlertDialog(context, R.string.info, R.string.coming_soon, null);
    }

    public static void showWithoutPermissionDialog(Context context){
        DialogUtils.showAlertDialog(context, R.string.error, R.string.cant_work_without_perms, (dialogInterface, i) -> {
            Activity activity = (Activity) context;
            activity.finish();
        });
    }
}
