package com.lamandys.permission.core;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

/**
 * Created by lamandys on 2019-08-06 16:51.
 */
public class PermissionDelegateFinder {

    private PermissionDelegateFinder() {
    }

    private static final String PERMISSION_TAG = "permission_helper_PermissionDelegateFragment";

    private static class PermissionDelegateFinderHelper {
        public static PermissionDelegateFinder instance = new PermissionDelegateFinder();
    }

    public static PermissionDelegateFinder getInstance() {
        return PermissionDelegateFinderHelper.instance;
    }

    public PermissionDelegateFragment find(FragmentActivity fragmentActivity) {
        PermissionDelegateFragment permissionDelegateFragment = null;
        if (!fragmentActivity.isFinishing()) {
            permissionDelegateFragment = findByFragmentManager(fragmentActivity.getSupportFragmentManager());
        }
        return permissionDelegateFragment;
    }

    private PermissionDelegateFragment findByFragmentManager(final FragmentManager manager) {
        Fragment fragment = manager.findFragmentByTag(PERMISSION_TAG);
        if (fragment == null) {
            fragment = new PermissionDelegateFragment();
            manager.beginTransaction().add(fragment, PERMISSION_TAG).commitAllowingStateLoss();
        }
        return (PermissionDelegateFragment) fragment;
    }
}
