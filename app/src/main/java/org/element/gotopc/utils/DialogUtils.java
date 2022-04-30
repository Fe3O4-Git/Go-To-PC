package org.element.gotopc.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import org.element.gotopc.R;

public class DialogUtils {

    private static AlertDialog.Builder buildAlertDialog(Context context, int title, int message, int posText,DialogInterface.OnClickListener onPosClickListener){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(posText, onPosClickListener);
        return builder;
    }

    private static AlertDialog.Builder buildQuestionDialog(Context context, int title, int message, int posText, DialogInterface.OnClickListener onPosClickListener, int negText, DialogInterface.OnClickListener onNegClickListener) {
        AlertDialog.Builder builder = buildAlertDialog(context, title, message, posText, onPosClickListener);
        builder.setNegativeButton(negText, onNegClickListener);
        return builder;
    }

    public static void showOKOnlyDialog(Context context, int title, int message, DialogInterface.OnClickListener onClickOKListener){
        buildAlertDialog(context, title, message, R.string.ok, onClickOKListener).show();
    }

    public static void showOKCancelDialog(Context context, int title, int message, DialogInterface.OnClickListener onClickOKListener, DialogInterface.OnClickListener onClickCancelListener){
        buildQuestionDialog(context, title, message, R.string.ok, onClickOKListener, R.string.cancel, onClickCancelListener).show();
    }

    public static void showYesNoDialog(Context context, int title, int message, DialogInterface.OnClickListener onClickYesListener, DialogInterface.OnClickListener onClickNoListener) {
        buildQuestionDialog(context, title, message, R.string.yes, onClickYesListener, R.string.no, onClickNoListener).show();
    }

    public static void showComingsoonDialog(Context context){
        showOKOnlyDialog(context, R.string.info, R.string.coming_soon, null);
    }

    public static void showWithoutPermissionDialog(Context context){
        DialogUtils.showOKOnlyDialog(context, R.string.error, R.string.cant_work_without_perms, (dialogInterface, i) -> {
            Activity activity = (Activity) context;
            activity.finish();
        });
    }
}
