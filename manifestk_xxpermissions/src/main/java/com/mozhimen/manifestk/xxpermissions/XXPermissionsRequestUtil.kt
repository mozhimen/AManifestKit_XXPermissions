package com.mozhimen.manifestk.xxpermissions

import android.content.Context
import androidx.annotation.RequiresPermission
import com.hjq.permissions.XXPermissions
import com.hjq.permissions.Permission
import com.mozhimen.kotlin.elemk.android.os.cons.CVersCode
import com.mozhimen.kotlin.elemk.commons.I_Listener
import com.mozhimen.kotlin.lintk.optins.permission.OPermission_ACCESS_COARSE_LOCATION
import com.mozhimen.kotlin.lintk.optins.permission.OPermission_ACCESS_FINE_LOCATION
import com.mozhimen.kotlin.lintk.optins.permission.OPermission_CAMERA
import com.mozhimen.kotlin.lintk.optins.permission.OPermission_GET_INSTALLED_APPS
import com.mozhimen.kotlin.lintk.optins.permission.OPermission_MANAGE_EXTERNAL_STORAGE
import com.mozhimen.kotlin.lintk.optins.permission.OPermission_POST_NOTIFICATIONS
import com.mozhimen.kotlin.lintk.optins.permission.OPermission_READ_EXTERNAL_STORAGE
import com.mozhimen.kotlin.lintk.optins.permission.OPermission_REQUEST_INSTALL_PACKAGES
import com.mozhimen.kotlin.lintk.optins.permission.OPermission_WRITE_EXTERNAL_STORAGE
import com.mozhimen.kotlin.elemk.android.cons.CPermission
import com.mozhimen.kotlin.lintk.optins.permission.OPermission_SYSTEM_ALERT_WINDOW
import com.mozhimen.kotlin.utilk.android.content.UtilKApplicationInfo
import com.mozhimen.kotlin.utilk.android.os.UtilKBuildVersion
import com.mozhimen.kotlin.utilk.commons.IUtilK

/**
 * @ClassName HelperPermission
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2023/11/24 16:21
 * @Version 1.0
 */
object XXPermissionsRequestUtil : IUtilK {
    //申请获取程序包名权限
    @JvmStatic
    @RequiresPermission(CPermission.GET_INSTALLED_APPS)
    @OPermission_GET_INSTALLED_APPS
    fun requestInstalledAppsPermission(context: Context, onGranted: I_Listener, onDenied: I_Listener) {
        XXPermissions.with(context)
            .permission(Permission.GET_INSTALLED_APPS)
//            .interceptor(PermissionInterceptor())
            .request { _, allGranted ->
                if (allGranted) onGranted.invoke() else onDenied.invoke()
            }
    }

    //申请通知权限
    @JvmStatic
    @RequiresPermission(CPermission.POST_NOTIFICATIONS)
    @OPermission_POST_NOTIFICATIONS
    fun requestPostNotificationPermission(context: Context, onGranted: I_Listener, onDenied: I_Listener) {
        if (UtilKBuildVersion.isAfterV_33_13_T()) {
            XXPermissions.with(context)
                .permission(Permission.POST_NOTIFICATIONS)
//            .interceptor(PermissionInterceptor())
                .request { _, allGranted -> if (allGranted) onGranted.invoke() else onDenied.invoke() }
        } else
            onGranted.invoke()
    }

    //申请读写权限

    @JvmStatic
    @RequiresPermission(allOf = [CPermission.MANAGE_EXTERNAL_STORAGE, CPermission.WRITE_EXTERNAL_STORAGE, CPermission.READ_EXTERNAL_STORAGE])
    @OPermission_WRITE_EXTERNAL_STORAGE
    @OPermission_READ_EXTERNAL_STORAGE
    @OPermission_MANAGE_EXTERNAL_STORAGE
    fun requestReadWritePermission(context: Context, onGranted: I_Listener, onDenied: I_Listener? = null) {
        try {
            if (UtilKApplicationInfo.getTargetSdkVersion(context) >= CVersCode.V_30_11_R) {
                XXPermissions.with(context) // 适配分区存储应该这样写
                    //.permission(Permission.Group.STORAGE)
                    // 不适配分区存储应该这样写
                    .permission(Permission.MANAGE_EXTERNAL_STORAGE)
//            .interceptor(PermissionInterceptor())
                    .request { _, allGranted -> if (allGranted) onGranted.invoke() else onDenied?.invoke() }
            } else /*if (UtilKApplicationInfo.getTargetSdkVersion(this)!! >= CVersCode.V_23_6_M)*/
                XXPermissions.with(context) // 适配分区存储应该这样写
                    //.permission(Permission.Group.STORAGE)
                    // 不适配分区存储应该这样写
                    .permission(CPermission.WRITE_EXTERNAL_STORAGE, Permission.READ_EXTERNAL_STORAGE)
//            .interceptor(PermissionInterceptor())
                    .request { _, allGranted -> if (allGranted) onGranted.invoke() else onDenied?.invoke() }
        } catch (e: Exception) {
            e.printStackTrace()
            onDenied?.invoke()
        }
    }

    //申请安装权限
    @JvmStatic
    @RequiresPermission(CPermission.REQUEST_INSTALL_PACKAGES)
    @OPermission_REQUEST_INSTALL_PACKAGES
    fun requestInstallPermission(context: Context, onGranted: I_Listener, onDenied: I_Listener) {
        if (UtilKBuildVersion.isAfterV_23_6_M()) {
            try {
                XXPermissions.with(context)
                    .permission(Permission.REQUEST_INSTALL_PACKAGES)
//            .interceptor(PermissionInterceptor())
                    .request { _, allGranted -> if (allGranted) onGranted.invoke() else onDenied.invoke() }
            } catch (e: Exception) {
                e.printStackTrace()
                onDenied.invoke()
            }
        } else
            onGranted.invoke()
    }

    //申请相机权限
    @JvmStatic
    @RequiresPermission(CPermission.CAMERA)
    @OPermission_CAMERA
    fun requestCameraPermission(context: Context, onGranted: I_Listener, onDenied: I_Listener? = null) {
        try {
            XXPermissions.with(context)
                .permission(Permission.CAMERA)
//            .interceptor(PermissionInterceptor())
                .request { _, allGranted -> if (allGranted) onGranted.invoke() else onDenied?.invoke() }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //申请位置
    @JvmStatic
    @RequiresPermission(allOf = [CPermission.ACCESS_FINE_LOCATION, CPermission.ACCESS_COARSE_LOCATION])
    @OPermission_ACCESS_COARSE_LOCATION
    @OPermission_ACCESS_FINE_LOCATION
    fun requestLocationPermission(context: Context, onGranted: I_Listener, onDenied: I_Listener? = null) {
        try {
            XXPermissions.with(context)
                .permission(Permission.ACCESS_FINE_LOCATION)
                .permission(Permission.ACCESS_COARSE_LOCATION)
//            .interceptor(PermissionInterceptor())
                .request { _, allGranted -> if (allGranted) onGranted.invoke() else onDenied?.invoke() }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //申请悬浮窗
    @JvmStatic
    @RequiresPermission(CPermission.SYSTEM_ALERT_WINDOW)
    @OPermission_SYSTEM_ALERT_WINDOW
    fun requestOverlayPermission(context: Context, onGranted: I_Listener, onDenied: I_Listener? = null) {
        try {
            XXPermissions.with(context)
                .permission(Permission.SYSTEM_ALERT_WINDOW)
//            .interceptor(PermissionInterceptor())
                .request { _, allGranted -> if (allGranted) onGranted.invoke() else onDenied?.invoke() }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}