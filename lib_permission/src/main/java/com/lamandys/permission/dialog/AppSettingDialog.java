package com.lamandys.permission.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.Settings;
import androidx.fragment.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;

import com.lamandys.permission.PermissionHelper;
import com.lamandys.permission.R;

/**
 * Created by lamandys on 2019-08-07 12:01.
 */
public class AppSettingDialog extends Dialog {

    public AppSettingDialog(Context context, String content,IOnSettingButtonClickListener listener) {
        super(context);
        init(context, content,listener);
    }

    private void init(final Context context, String content, final IOnSettingButtonClickListener listener) {
        if (getWindow() != null) {
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        setContentView(R.layout.permission_layout_setting);

        setCancelable(false);
        setCanceledOnTouchOutside(false);

        findViewById(R.id.permission_action_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.clickCancel();
                dismiss();
            }
        });

        findViewById(R.id.permission_action_sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createSettingIntent(context);
                dismiss();
            }
        });

        if (content != null) {
            ((TextView) findViewById(R.id.permission_setting_content)).setText(content);
        }
    }

    private void createSettingIntent(Context context) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.fromParts("package", context.getPackageName(), null));
        ((FragmentActivity) context).startActivityForResult(intent, PermissionHelper.PERMISSION_REQUEST_CODE);
    }

    public interface IOnSettingButtonClickListener {
        void clickCancel();
    }
}
