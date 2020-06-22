package com.lamandys.permission.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.lamandys.permission.R;
import com.lamandys.permission.callback.PermissionCallback;

/**
 * Created by lamandys on 2019-08-06 18:53.
 */
public class PermissionDeniedDialog extends Dialog {

    public PermissionDeniedDialog(Context context, String content, PermissionCallback callback) {
        super(context);
        init(content, callback);
    }

    private void init(String content, final PermissionCallback callback) {
        if (getWindow() != null) {
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        setContentView(R.layout.permission_layout_denied);

        setCancelable(false);
        setCanceledOnTouchOutside(false);

        findViewById(R.id.permission_action_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onRationaleDenied();
                dismiss();
            }
        });

        findViewById(R.id.permission_action_sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onRationaleAccepted();
                dismiss();
            }
        });

        if (content != null) {
            ((TextView) findViewById(R.id.permission_dialog_content)).setText(content);
        }
    }

    private void setupWindowParams() {
        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.width = 100;
        }
    }

}
