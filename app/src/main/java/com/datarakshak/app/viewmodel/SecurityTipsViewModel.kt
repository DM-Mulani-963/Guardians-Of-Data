package com.datarakshak.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.datarakshak.app.model.RiskLevel
import com.datarakshak.app.model.SecurityTip

class SecurityTipsViewModel(application: Application) : AndroidViewModel(application) {
    private val _securityTips = MutableLiveData<List<SecurityTip>>()
    val securityTips: LiveData<List<SecurityTip>> = _securityTips

    fun analyzeSecurity() {
        val pm = getApplication<Application>().packageManager
        val installedApps = pm.getInstalledPackages(android.content.pm.PackageManager.GET_PERMISSIONS)
        
        val tips = mutableListOf<SecurityTip>()
        
        for (packageInfo in installedApps) {
            val permissions = packageInfo.requestedPermissions?.toList() ?: emptyList()
            val suspiciousPermissions = mutableListOf<String>()
            
            // Analyze permissions based on app category
            when {
                packageInfo.applicationInfo.loadLabel(pm).contains("camera", true) -> {
                    // Check if camera app requests suspicious permissions
                    permissions.forEach { permission ->
                        if (permission.contains("CONTACTS") || 
                            permission.contains("LOCATION") ||
                            permission.contains("RECORD_AUDIO")) {
                            suspiciousPermissions.add(permission)
                        }
                    }
                }
                packageInfo.applicationInfo.loadLabel(pm).contains("calculator", true) -> {
                    // Calculator shouldn't need many permissions
                    permissions.forEach { permission ->
                        if (!permission.contains("INTERNET")) {
                            suspiciousPermissions.add(permission)
                        }
                    }
                }
                // Add more app category checks
            }
            
            if (suspiciousPermissions.isNotEmpty()) {
                tips.add(
                    SecurityTip(
                        appName = packageInfo.applicationInfo.loadLabel(pm).toString(),
                        packageName = packageInfo.packageName,
                        icon = pm.getApplicationIcon(packageInfo.applicationInfo),
                        suspiciousPermissions = suspiciousPermissions,
                        recommendation = "This app might be requesting unnecessary permissions. " +
                                "Consider reviewing and revoking these permissions.",
                        severity = if (suspiciousPermissions.size > 2) RiskLevel.HIGH else RiskLevel.MEDIUM
                    )
                )
            }
        }
        
        _securityTips.value = tips.sortedBy { it.severity }
    }
} 