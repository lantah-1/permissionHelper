package com.lamandys.permission.callback;

import android.content.Context;

import java.util.List;

/**
 * Created by lamandys on 2019-08-06 17:02.
 */
public interface PermissionCallback {
    /**
     * 权限申请通过
     */
    void onPermissionsGranted();

    /**
     * 用户拒绝的权限列表
     * @param perms 权限列表
     */
    void onPermissionsDenied(List<String> perms);

    /**
     * 当用户点击拒绝并且不再提醒后的回调
     */
    void onPermissionDeniedAndNotAskAgain();

    /**
     * 是否在权限申请拒绝后弹窗
     * @return 返回是就系统自动弹窗
     */
    boolean isShowRationaleDialogAfterDenied();

    /**
     * 提示弹窗的取消按钮点击回调
     */
    void onRationaleDenied();

    /**
     * 提示弹窗的确定回调
     */
    void onRationaleAccepted();

    /**
     * 是否显示去设置页面弹窗
     * @return 返回是就显示弹窗
     */
    boolean isShowGoSettingDialogAfterClickNotShowAgain();

    /**
     * 从设置页面返回后的回调
     */
    void onReturnFromSettingPage();

    /**
     * 自定义提示弹窗的内容
     * @return 内容
     */
    String customRationaleDialogContent(Context context);

    /**
     * 自定义设置弹窗的提示内容
     * @return 内容
     */
    String customAppSettingDialogContent(Context context);
}
