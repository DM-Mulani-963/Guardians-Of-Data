package com.datarakshak.app.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.datarakshak.app.RiskLevel

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

    fun getPermissionName(permission: String): String {
        return when (permission) {
            Manifest.permission.CAMERA -> "Camera"
            Manifest.permission.ACCESS_FINE_LOCATION -> "Location"
            Manifest.permission.READ_CONTACTS -> "Contacts"
            Manifest.permission.READ_EXTERNAL_STORAGE -> "Storage"
            else -> "Unknown Permission"
        }
    }

    fun getPermissionDescription(permission: String): String {
        return when (permission) {
            Manifest.permission.CAMERA -> "Access to take photos and record videos"
            Manifest.permission.ACCESS_FINE_LOCATION -> "Access to precise location (GPS and network-based)"
            Manifest.permission.READ_CONTACTS -> "Access to view your contacts"
            Manifest.permission.READ_EXTERNAL_STORAGE -> "Access to photos, media, and files"
            else -> "Unknown permission description"
        }
    }

    fun getRiskLevel(permission: String): RiskLevel {
        return when (permission) {
            Manifest.permission.ACCESS_FINE_LOCATION -> RiskLevel.HIGH
            Manifest.permission.READ_CONTACTS -> RiskLevel.HIGH
            Manifest.permission.CAMERA -> RiskLevel.MEDIUM
            else -> RiskLevel.LOW
        }
    }
} 