package com.lamandys.permission.setting;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.fragment.app.FragmentActivity;
import androidx.appcompat.app.AppCompatActivity;

import com.lamandys.permission.callback.PermissionCallback;
import com.lamandys.permission.dialog.AppSettingDialog;

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
public class AppSettingsDialogHolderActivity extends AppCompatActivity implements AppSettingDialog.IOnSettingButtonClickListener {

    private AppSettingDialog settingDialog;
    private PermissionCallback mCallback;

    public static void showAppSettingDialogHolderActivity(FragmentActivity activity, String content) {
        Intent intent = new Intent(activity, AppSettingsDialogHolderActivity.class);
        intent.putExtra("content", content);
        activity.startActivity(intent);
        activity.overridePendingTransition(0, 0);
    }

    public void setCallback(PermissionCallback callback) {
        this.mCallback = callback;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            finish();
        } else {
            Intent intent = getIntent();
            String content;
            if (intent != null && (content = intent.getStringExtra("content")) != null) {
                settingDialog = new AppSettingDialog(this, content, this);
            } else {
                settingDialog = new AppSettingDialog(this, null, this);
            }
            settingDialog.show();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (settingDialog != null && settingDialog.isShowing()) {
            settingDialog.dismiss();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mCallback != null) {
            mCallback.onReturnFromSettingPage();
        }
        finish();
    }

    @Override
    public void clickCancel() {
        finish();
    }
}
