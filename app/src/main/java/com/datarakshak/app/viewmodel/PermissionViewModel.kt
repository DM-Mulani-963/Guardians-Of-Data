package com.datarakshak.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.datarakshak.app.model.PermissionItem
import com.datarakshak.app.model.RiskLevel
import com.datarakshak.app.utils.PermissionUtils

class PermissionViewModel(application: Application) : AndroidViewModel(application) {
    private val _permissions = MutableLiveData<List<PermissionItem>>()
    val permissions: LiveData<List<PermissionItem>> = _permissions

    fun scanPermissions() {
        val scannedPermissions = PermissionUtils.scanInstalledApps(getApplication())
        val permissionItems = scannedPermissions.map { (name, _, riskLevel) ->
            PermissionItem(
                name = name,
                riskLevel = riskLevel.displayName,
                riskIcon = PermissionUtils.getPermissionRiskIcon(riskLevel)
            )
        }.sortedBy { 
            when (RiskLevel.valueOf(it.riskLevel.replace(" Risk", "").uppercase())) {
                RiskLevel.HIGH -> 0
                RiskLevel.MEDIUM -> 1
                RiskLevel.LOW -> 2
            }
        }
        _permissions.value = permissionItems
    }
} 