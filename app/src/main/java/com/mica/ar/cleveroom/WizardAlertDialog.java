package com.mica.ar.cleveroom;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.WindowManager;


public final class WizardAlertDialog {

    private ProgressDialog pdialog;
    private static WizardAlertDialog dialogs;

    private WizardAlertDialog() {

    }

    public static synchronized WizardAlertDialog getInstance() {
        if (dialogs == null) {
            dialogs = new WizardAlertDialog();
        }
        return dialogs;
    }

    public void showProgressDialog(int resID, Context ctx) {
        String message = ctx.getString(resID);
        pdialog = ProgressDialog.show(ctx, null, message, true, true);
        pdialog.setCancelable(false);

    }
    public void closeProgressDialog() {

        if (pdialog != null) {
            pdialog.dismiss();
            pdialog = null;
        }
    }


    public static void showErrorDialog(Context activityContext, String msg, int btnNameResId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activityContext);
        builder.setTitle(R.string.title_error).setMessage(msg).setPositiveButton(btnNameResId, null);
        AlertDialog alert = builder.create();
        alert.getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        if (! ((Activity) activityContext).isFinishing()) {
            alert.show();
        }

    }
}
