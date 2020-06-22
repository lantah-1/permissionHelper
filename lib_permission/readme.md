#### 使用说明(2019-08-09更新)

至于为什么要写一个这样的东西呢？可能是习惯吧！我在用EasyPermission的时候，常常感觉到配置比较繁琐，回调比较乱，

个人感觉比较难用，所以就写了这个东西，如果使用过程中遇到哪些问题或者是看源码的时候，觉得可以优化的地方，可以提出来，优化和完善一下。

###### A. 常用的使用方式
1. 实现 **PermissionCallback** 接口，如Fragment或者Activity：（可省略）
```
class IVoiceContactFragment : IVoiceBaseFragment<BaseViewModel>(), PermissionCallback by PermissionCallbackAdapter() {}

class IVoiceMainActivity : BaseActivity<BaseViewModel>(),PermissionCallback by PermissionCallbackAdapter() {}
```
2. 在需要的地方调用：
 ```
PermissionHelper.request(activity, {
            it.add(Manifest.permission.READ_CONTACTS)
            it.add(Manifest.permission.WRITE_CONTACTS)
            it.add(Manifest.permission.GET_ACCOUNTS)
        }, this)
        
// 如果第一步省略了的话，this就换成匿名内部类的方式去写
PermissionHelper.request(this, { permission ->
                permission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }, object : PermissionCallbackAdapter() {
            
                override fun onPermissionsGranted() {
                    // do something after onPermissionsGranted
                }

                override fun onPermissionsDenied(perms: MutableList<String>?) {
                    // 当isShowRationaleDialogAfterDenied为false时，拒绝后回调这个
                    // 当isShowRationaleDialogAfterDenied为true时，拒绝后，会先弹窗，如果弹窗后点击取消，会回调这个
                }
            })
```
3. 看你需要什么回调，重写相应的方法，如下是所有的方法，解释在里面。

    一般只需要重写三个方法（申请成功，申请失败，不再提醒（可省））
```
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
```

###### B. 自定义使用方式
1. 首先，说一下上面的那种申请方式： 

```
a. 当用户点击拒绝 但 没有点击不再提醒，会弹窗提示用户是否再次申请权限

b. 提醒用户的弹窗的文案是默认的一个提示，无法更改，而且弹窗只会弹一次

c. 当用户点击拒绝 并且 点击不再提醒，会弹窗告诉用户，没有权限不能正常工作，引导去设置页面

d. 引导去设置页面的弹窗内容也是无法更改的。
```
2. 那么，如果上面的达不到你的要求，那就把callback里面的方法全部重写，即可。
 
   比如说，我想更改提示再次申请的文案？那没办法，只能你自己重写**onPermissionsDenied**去处理。
   
   因为弹窗用的都是dialog，所以上下文只能用activity对象。
   
   还有就是不能更改提示窗的标题，为啥？感觉标题很通俗了呀。（应用权限申请）
   
   至于最后一个参数，callback是用于对按钮的点击响应，如果用当前的callback去作为参数，注意**isShowRationaleDialogAfterDenied**是否一直为true，不然会一直循环去申请权限的
```
 override fun onPermissionsDenied(perms: MutableList<String>?) {
        PermissionHelper.showPermissionDeniedDialog(activity,"这个是自定义的提示内容",callback)
    }
    
    
     PermissionHelper.request(this, { permission ->
                permission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }, object :PermissionCallback {
                override fun onPermissionsGranted() {
                    // do something after onPermissionsGranted
                }

                override fun onPermissionsDenied(perms: MutableList<String>?) {
                    // 当isShowRationaleDialogAfterDenied为false时，拒绝后回调这个
                    // 当isShowRationaleDialogAfterDenied为true时，拒绝后，会先弹窗，如果弹窗后点击取消，会回调这个
                }

                override fun onPermissionDeniedAndNotAskAgain() {
                    // 当点击拒绝并且不再提醒后，回调这个
                }

                override fun isShowRationaleDialogAfterDenied(): Boolean {
                    // 拒绝后是否显示提示弹窗
                    return false
                }

                override fun onRationaleDenied() {
                    // 弹窗，如果点击取消，会回调这个和onPermissionsDenied
                }

                override fun onRationaleAccepted() {
                    // 弹窗，接受再次申请，需要主动去调用申请权限
                }

                override fun isShowGoSettingDialogAfterClickNotShowAgain(): Boolean {
                    // 是否需要弹窗提醒去设置页面
                    return false
                }

            })
```
###### C.需要注意的地方 
1. 需要注意的是，**isShowRationaleDialogAfterDenied**是判断拒绝后是否弹窗提醒继续申请权限的唯一条件，如果该值一直为true，用户一直拒绝，那么将会循环申请权限
2. 点击去设置页面后，回来页面如何监控？

    ~~本来想去设置页面时，新开一个activity，并把当前的callback传递到这个activity中，等待从设置页面回来，再判断是否申请了权限回调到callback中的。~~
    
    ~~但是，我没想到如何把callback传递到activity中，所以没法回调到。~~
    
    ~~如果需要的判断的话，可以从传递的activity对象中回调onActivityResult方法，判断requestCode是否等于PermissionHelper.PERMISSION_REQUEST_CODE，然后再次申请权限即可回调~~
    
3. 第二点的问题已经解决了，利用lifecycle的方法，往activity里面设置callback,要想监听，回调**onReturnFromSettingPage**方法即可
4. 发现了一个关于权限的问题，就是在app使用中，弹窗申请权限，用户拒绝并不再提醒后，会引导用户去设置页面打开权限，
   但是，如果用户非但不去打开权限，反而把某个权限禁止了，在你禁止的那一刻，应用就会被强杀了
   当你点击返回，想要返回应用时，此时会执行两个操作：restart application ,和
   restart topActivity。
   
   如果，引导去设置页面单纯用的是dialog或者fragmentDialog去显示的话，那么返回时，最顶层的activity就是你申请权限页面所在的activity。
   如果，该activity有一些类似callback的回调，那么这些将会是null的状态。从而影响后续操作。
   
   或者说，当返回时，重新打开了topActivity，但是topActivity里面的服务是从MainActivity去启动的，此时也会有问题，因为MainActivity还没启动。
   
   此时，查看EasyPermission的操作是：引导去设置页面的弹窗是用activity去承载的，这就很好的解决了topActivity的问题，果然是坑多啊。
   但是，这样的操作并不能解决返回后的页面属性为null的问题，所以我们应该监听从设置页面回来，最好就是立刻杀死应用，让用户手动重启（高德地图就是这样操作的，但是不知道究竟怎么操作才可以像高德那样）
   其次的方法，我觉得比较全面的是：回来后，清除盏内其他activity，启动MainActivity,并启动引导页去请求权限。
   
   当然，你可以尝试一下，上面三个步骤中的后两个，只取其中一个会有什么样的问题。还有一个问题就是：从设置页面回来，因为重启application会很耗时，导致卡顿了很久
   并且白屏了一会，才执行我上面的三个操作，不知道是否可以继续优化一下。。。其实这种情况的操作率不大，只是刚好碰到了有这个问题。