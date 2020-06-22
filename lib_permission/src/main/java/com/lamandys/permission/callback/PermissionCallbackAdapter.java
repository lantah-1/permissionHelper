package com.lamandys.permission.callback;

import android.content.Context;
import android.text.TextUtils;

import com.lamandys.permission.R;

import java.util.List;

/**
 * Created by lamandys on 2019-08-07 10:47.
 */
public class PermissionCallbackAdapter implements PermissionCallback {

    private PermissionCallback mCallback;

    public PermissionCallbackAdapter() {

    }

    public PermissionCallbackAdapter(PermissionCallback callback) {
        this.mCallback = callback;
    }

    @Override
    public void onPermissionsGranted() {
        if (mCallback != null) {
            mCallback.onPermissionsGranted();
        }
    }

    @Override
    public void onPermissionsDenied(List<String> perms) {
        if (mCallback != null) {
            mCallback.onPermissionsDenied(perms);
        }
    }

    @Override
    public void onPermissionDeniedAndNotAskAgain() {
        if (mCallback != null) {
            mCallback.onPermissionDeniedAndNotAskAgain();
        }
    }

    @Override
    public boolean isShowRationaleDialogAfterDenied() {
        if (mCallback != null) {
            return mCallback.isShowRationaleDialogAfterDenied();
        }
        return true;
    }


    @Override
    public void onRationaleDenied() {
        if (mCallback != null) {
            mCallback.onRationaleDenied();
        }
    }

    @Override
    public void onRationaleAccepted() {
        if (mCallback != null) {
            mCallback.onRationaleAccepted();
        }
    }

    @Override
    public boolean isShowGoSettingDialogAfterClickNotShowAgain() {
        if (mCallback != null) {
            return mCallback.isShowGoSettingDialogAfterClickNotShowAgain();
        }
        return true;
    }

    @Override
    public void onReturnFromSettingPage() {
        if (mCallback != null) {
            mCallback.onReturnFromSettingPage();
        }
    }

    @Override
    public String customRationaleDialogContent(Context context) {
        if (mCallback != null && !TextUtils.isEmpty(mCallback.customRationaleDialogContent(context))) {
            return mCallback.customRationaleDialogContent(context);
        }
        return context.getResources().getString(R.string.permission_rejected_hint);
    }

    @Override
    public String customAppSettingDialogContent(Context context) {
        if (mCallback != null && !TextUtils.isEmpty(mCallback.customAppSettingDialogContent(context))) {
            return mCallback.customAppSettingDialogContent(context);
        }
        return context.getResources().getString(R.string.permission_rationale_ask_again);
    }

}
