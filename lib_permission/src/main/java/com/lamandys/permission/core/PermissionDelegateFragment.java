package com.lamandys.permission.core;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import com.lamandys.permission.PermissionHelper;
import com.lamandys.permission.callback.PermissionCallback;
import com.lamandys.permission.callback.PermissionCallbackAdapter;
import com.lamandys.permission.dialog.PermissionDeniedDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by lamandys on 2019-08-06 10:48.
 */
public class PermissionDelegateFragment extends Fragment {

    private boolean isOnAttach = false;

    private String[] perms = null;
    private PermissionCallback callback = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        isOnAttach = true;
    }

    public void request(String[] perms, PermissionCallback callback) {
        this.perms = perms;
        this.callback = callback;
        if (isOnAttach) {
            doRequest();
        } else {
            getLifecycle().addObserver(new LifecycleObserver() {
                @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
                public void onCreate() {
                    isOnAttach = true;
                    doRequest();
                    getLifecycle().removeObserver(this);
                }
            });
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        callback = null;
        perms = null;
    }

    private void doRequest() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            if (callback != null) {
                callback.onPermissionsGranted();
            }
            return;
        }
        if (getActivity() == null) {
            if (callback != null) {
                callback.onPermissionsDenied(Arrays.asList(perms));
            }
            return;
        }
        //只申请用户未允许的权限
        List<String> unGrantedList = new ArrayList<>();
        for (String permission : perms) {
            if (ContextCompat.checkSelfPermission(getActivity(), permission) != PackageManager.PERMISSION_GRANTED) {
                unGrantedList.add(permission);
            }
        }
        if (unGrantedList.size() > 0) {
            PermissionDelegateFragment.this.requestPermissions(unGrantedList.toArray(new String[]{}), PermissionHelper.PERMISSION_REQUEST_CODE);
        } else {
            if (callback != null) {
                callback.onPermissionsGranted();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode != PermissionHelper.PERMISSION_REQUEST_CODE)
            return;
        if (grantResults.length > 0 && callback != null) {
            //找出拒绝的权限
            List<String> deniedList = new ArrayList<>();
            for (int j = 0; j < grantResults.length; j++) {
                int grantResult = grantResults[j];
                String permission = permissions[j];
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    deniedList.add(permission);
                }
            }
            //已全部允许
            if (deniedList.isEmpty()) {
                callback.onPermissionsGranted();
            } else {
                onPermissionDenied(deniedList);
            }
        }
    }

    private void onPermissionDenied(final List<String> deniedList) {
        // 能进来这个方法的，callback都不为null
        if (getActivity() == null) {
            callback.onPermissionsDenied(deniedList);
            return;
        }
        boolean shouldShowRationale = false;
        for (String permission : deniedList) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permission)) {
                shouldShowRationale = true;
                break;
            }
        }
        if (shouldShowRationale) {
            if (callback.isShowGoSettingDialogAfterClickNotShowAgain()) {
                showGoAppSettingDialog();
            }
            callback.onPermissionDeniedAndNotAskAgain();
        } else if (callback.isShowRationaleDialogAfterDenied()) {
            requestPermissionAgain(deniedList);
        } else {
            callback.onPermissionsDenied(deniedList);
        }
    }

    private void showGoAppSettingDialog() {
        String content = callback.customAppSettingDialogContent(getActivity());
        PermissionHelper.showGoSettingDialog(getActivity(), content, callback);
    }

    private void requestPermissionAgain(final List<String> deniedList) {
        String content = callback.customRationaleDialogContent(getActivity());
        // new一个callback的目的是重写isShowRationaleDialog返回false，防止循环弹窗询问
        PermissionDeniedDialog dialog = new PermissionDeniedDialog(getActivity(), content,  new PermissionCallbackAdapter(callback) {
            @Override
            public void onRationaleAccepted() {
                request(deniedList.toArray(new String[]{}), this);
            }

            @Override
            public boolean isShowRationaleDialogAfterDenied() {
                return false;
            }

            @Override
            public void onRationaleDenied() {
                // 当弹窗后，是否要把申请拒绝的回调用上？不用的话，只监听成功/失败的话就没回调了
                callback.onPermissionsDenied(deniedList);
                super.onRationaleDenied();
            }
        });
        dialog.show();
    }
}
