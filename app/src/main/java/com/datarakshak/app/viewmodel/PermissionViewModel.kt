package com.datarakshak.app.viewmodel

import android.app.Application
import android.content.pm.PackageManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.datarakshak.app.PermissionItem
import com.datarakshak.app.RiskLevel
import com.datarakshak.app.utils.PermissionUtils

class PermissionViewModel(application: Application) : AndroidViewModel(application) {
    private val _permissionItems = MutableLiveData<List<PermissionItem>>()
    val permissionItems: LiveData<List<PermissionItem>> = _permissionItems

    private val _privacyScore = MutableLiveData<Int>()
    val privacyScore: LiveData<Int> = _privacyScore

    fun updatePermissions(permissions: List<String>) {
        val items = permissions.map { permission ->
            PermissionItem(
                name = PermissionUtils.getPermissionName(permission),
                description = PermissionUtils.getPermissionDescription(permission),
                riskLevel = PermissionUtils.getRiskLevel(permission)
            )
        }
        _permissionItems.value = items
        calculatePrivacyScore(permissions)
    }

    private fun calculatePrivacyScore(permissions: List<String>) {
        var score = 100
        permissions.forEach { permission ->
            if (getApplication<Application>().checkSelfPermission(permission) == 
                PackageManager.PERMISSION_GRANTED) {
                score -= when (PermissionUtils.getRiskLevel(permission)) {
                    RiskLevel.HIGH -> 15
                    RiskLevel.MEDIUM -> 10
                    RiskLevel.LOW -> 5
                }
            }
        }
        _privacyScore.value = score.coerceIn(0, 100)
    }
} 