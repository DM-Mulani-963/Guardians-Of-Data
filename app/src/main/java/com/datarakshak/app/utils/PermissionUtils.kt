package com.datarakshak.app.utils

import android.content.Context
import android.content.pm.PackageManager
import com.datarakshak.app.model.RiskLevel
import com.datarakshak.app.R

object PermissionUtils {
    fun getPermissionRiskIcon(riskLevel: RiskLevel): Int {
        return when (riskLevel) {
            RiskLevel.HIGH -> R.drawable.ic_high_risk
            RiskLevel.MEDIUM -> R.drawable.ic_medium_risk
            RiskLevel.LOW -> R.drawable.ic_low_risk
        }
    }

    fun scanInstalledApps(context: Context): List<Triple<String, String, RiskLevel>> {
        val permissions = mutableListOf<Triple<String, String, RiskLevel>>()
        try {
            val pm = context.packageManager
            val installedApps = pm.getInstalledPackages(PackageManager.GET_PERMISSIONS)
            
            for (packageInfo in installedApps) {
                val requestedPermissions = packageInfo.requestedPermissions
                if (requestedPermissions != null) {
                    for (permission in requestedPermissions) {
                        val riskLevel = RiskLevel.fromPermission(permission)
                        val permissionInfo = pm.getPermissionInfo(permission, 0)
                        val permissionName = permissionInfo.loadLabel(pm).toString()
                        permissions.add(Triple(permissionName, permission, riskLevel))
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return permissions.distinctBy { it.second }
    }
} 