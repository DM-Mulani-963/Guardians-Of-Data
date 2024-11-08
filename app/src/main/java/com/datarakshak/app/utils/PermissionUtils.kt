package com.datarakshak.app.utils

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

object PermissionUtils {
    fun isPermissionGranted(context: Context, permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun getGrantedPermissionsCount(context: Context, permissions: List<String>): Int {
        return permissions.count { isPermissionGranted(context, it) }
    }
} 