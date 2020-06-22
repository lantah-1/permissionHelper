package com.lamandys.permission;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.util.Consumer;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.lamandys.permission.callback.PermissionCallback;
import com.lamandys.permission.callback.PermissionCallbackAdapter;
import com.lamandys.permission.core.PermissionDelegateFinder;
import com.lamandys.permission.core.PermissionDelegateFragment;
import com.lamandys.permission.other.PermissionFunctionInterface;
import com.lamandys.permission.setting.ActivityLifecycleCallbacksAdapter;
import com.lamandys.permission.setting.AppSettingsDialogHolderActivity;

import java.util.ArrayList;

/**
 * Created by @Author(lamandys) on 2020/5/11 10:34 AM.
 */
public class PermissionHelper implements PermissionFunctionInterface, PermissionFunctionInterface.PermissionFunctionCallback {

    private PermissionHelper() {
    }

    private PermissionHelper(Object context) {
        this.mContext = context;
    }

    private Object mContext;
    private PermissionCallback mCallback;

    public static final int PERMISSION_REQUEST_CODE = 233;

    public static PermissionFunctionCallback with(FragmentActivity activity) {
        return new PermissionHelper(activity);
    }

    public static PermissionFunctionCallback with(Fragment fragment) {
        return new PermissionHelper(fragment);
    }

    public static void showGoSettingDialog(FragmentActivity activity, String content, PermissionCallback callback) {
        registerActivityLifecycleCallbacks(activity, callback);
        AppSettingsDialogHolderActivity.showAppSettingDialogHolderActivity(activity, content);
    }

    public static void showGoSettingDialog(FragmentActivity activity, PermissionCallback callback) {
        registerActivityLifecycleCallbacks(activity, callback);
        AppSettingsDialogHolderActivity.showAppSettingDialogHolderActivity(activity, null);
    }

    private static void registerActivityLifecycleCallbacks(FragmentActivity activity, final PermissionCallback callback) {
        activity.getApplication().registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacksAdapter() {
            @Override
            public void onActivityCreated(@NonNull Activity activity, Bundle savedInstanceState) {
                if (activity instanceof AppSettingsDialogHolderActivity) {
                    ((AppSettingsDialogHolderActivity) activity).setCallback(callback);
                }
            }

            @Override
            public void onActivityDestroyed(@NonNull Activity activity) {
                if (activity instanceof AppSettingsDialogHolderActivity) {
                    activity.getApplication().unregisterActivityLifecycleCallbacks(this);
                }
            }
        });
    }

    @Override
    public void request(Consumer<ArrayList<String>> consumer) {
        findDelegatePermissionHelperAndRequest(consumer);
    }

    private void findDelegatePermissionHelperAndRequest(Consumer<ArrayList<String>> consumer) {
        ArrayList<String> permissions = new ArrayList<>();
        consumer.accept(permissions);
        if (permissions.isEmpty()) {
            throw new IllegalArgumentException("permissions that you want to request can not empty");
        }
        FragmentActivity activity = null;
        if (mContext instanceof FragmentActivity) {
            activity = (FragmentActivity) mContext;
        } else if (mContext instanceof Fragment) {
            activity = ((Fragment) mContext).getActivity();
        }
        if (activity == null) {
            throw new NullPointerException("FragmentActivity or Fragment context can not be null");
        }
        PermissionDelegateFragment delegateFragment = PermissionDelegateFinder.getInstance().find(activity);
        if (delegateFragment != null) {
            delegateFragment.request(permissions.toArray(new String[]{}), new PermissionCallbackAdapter(mCallback));
        } else {
            if (mCallback != null) {
                mCallback.onPermissionsDenied(permissions);
            }
        }
    }

    @Override
    public PermissionFunctionInterface setCallback(PermissionCallback callback) {
        this.mCallback = callback;
        return this;
    }
}
