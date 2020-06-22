package com.lamandys.permission.other;

import androidx.core.util.Consumer;

import com.lamandys.permission.callback.PermissionCallback;

import java.util.ArrayList;

/**
 * Created by @Author(lamandys) on 2020/5/11 10:27 AM.
 */
public interface PermissionFunctionInterface {

    void request(Consumer<ArrayList<String>> consumer);

    interface PermissionFunctionCallback {
        PermissionFunctionInterface setCallback(PermissionCallback callback);
    }
}
