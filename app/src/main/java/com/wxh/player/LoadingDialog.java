package com.wxh.player;

import android.app.Dialog;
import android.content.Context;

/**
 * Created by xukai on 2017/10/09.
 */

public class LoadingDialog {
    private Dialog progressDialog;
    public LoadingDialog(Context context){
        init(context);
    }

    public LoadingDialog init(Context context){
        progressDialog = new Dialog(context,R.style.progress_dialog);
        progressDialog.setContentView(R.layout.dialog_loading);
        progressDialog.setCancelable(true);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.setCanceledOnTouchOutside(false);
        return this;
    }
    public void show(){
        if(!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }
    public void dismiss(){
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
