package com.lamandys.permissionhelper

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.lamandys.permission.PermissionHelper
import com.lamandys.permission.callback.PermissionCallback
import com.lamandys.permission.callback.PermissionCallbackAdapter

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
