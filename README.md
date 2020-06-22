### 说明

适用于 **Activity** / **Fragment** 的简单的权限申请工具，无需手动回调 **onActivityResult**，支持自定义各种事件回调处理，如下：

- 权限申请通过
    ```
    void onPermissionsGranted();
    ```
- 用户拒绝的权限列表
    ```
    void onPermissionsDenied(List<String> perms);
    ```
- 当用户点击拒绝并且不再提醒后的回调
    ```
    void onPermissionDeniedAndNotAskAgain();
    ```
- 是否在权限申请拒绝后弹窗
    ```
    boolean isShowRationaleDialogAfterDenied();
    ```
- 提示弹窗的取消按钮点击回调
    ```
    void onRationaleDenied();
    ```
- 提示弹窗的确定回调
    ```
    void onRationaleAccepted();
    ```
- 再次询问拒绝后是否显示去设置页面弹窗
    ```
    boolean isShowGoSettingDialogAfterClickNotShowAgain();
    ```
- 从设置页面返回后的回调
    ```
    void onReturnFromSettingPage();
    ```
- 自定义提示弹窗的内容
    ```
    String customRationaleDialogContent(Context context);
    ```
- 自定义设置弹窗的提示内容
    ```
    String customAppSettingDialogContent(Context context);
    ```

### 使用

1. 在build.gradle中添加：

    ```
    implementation 'com.lamandys:permissionhelper:1.0.0'
    ```

2. 在需要申请权限的地方：

    ```
    PermissionHelper.with(this)
                    .setCallback(object :PermissionCallbackAdapter() {
                        override fun onPermissionsGranted() {
                            super.onPermissionsGranted()
                        }

                        override fun onPermissionsDenied(perms: MutableList<String>?) {
                            super.onPermissionsDenied(perms)
                        }
                    })
                    .request {
                        it.add(android.Manifest.permission.CAMERA)
                        it.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        it.add(android.Manifest.permission.ACCESS_FINE_LOCATION)
                    }
    ```

或者：

    ```
    class MainActivity : AppCompatActivity(), PermissionCallback by PermissionCallbackAdapter() {

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)


            PermissionHelper.with(this)
                .setCallback(this)
                .request {
                    it.add(android.Manifest.permission.CAMERA)
                    it.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    it.add(android.Manifest.permission.ACCESS_FINE_LOCATION)
                }
        }

        override fun onPermissionsGranted() {
            Toast.makeText(applicationContext, "all permission granted", Toast.LENGTH_SHORT).show()
        }

        override fun onPermissionsDenied(perms: MutableList<String>?) {
            Toast.makeText(applicationContext, "${perms?.size} permission denied", Toast.LENGTH_SHORT)
                .show()
        }
    }
    ```

3. ojbk!