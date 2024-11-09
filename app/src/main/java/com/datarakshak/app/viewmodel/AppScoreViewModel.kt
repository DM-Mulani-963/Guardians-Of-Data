package com.datarakshak.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.datarakshak.app.R
import com.datarakshak.app.model.AppScore
import com.datarakshak.app.model.RiskLevel

class AppScoreViewModel(application: Application) : AndroidViewModel(application) {
    private val _apps = MutableLiveData<List<AppScore>>()
    val apps: LiveData<List<AppScore>> = _apps

    private val notificationManager = application.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    
    init {
        createNotificationChannel()
    }

    fun scanApps() {
        val pm = getApplication<Application>().packageManager
        val installedApps = pm.getInstalledPackages(android.content.pm.PackageManager.GET_PERMISSIONS)
        
        val appScores = installedApps.map { packageInfo ->
            val permissions = packageInfo.requestedPermissions?.toList() ?: emptyList()
            val score = calculateScore(permissions)
            val riskLevel = when {
                score <= 33 -> RiskLevel.HIGH
                score <= 66 -> RiskLevel.MEDIUM
                else -> RiskLevel.LOW
            }
            
            AppScore(
                appName = pm.getApplicationLabel(packageInfo.applicationInfo).toString(),
                packageName = packageInfo.packageName,
                score = score,
                permissions = permissions,
                riskLevel = riskLevel,
                icon = pm.getApplicationIcon(packageInfo.applicationInfo)
            )
        }.sortedBy { it.score }

        _apps.value = appScores
    }

    private fun calculateScore(permissions: List<String>): Int {
        if (permissions.isEmpty()) return 100

        val allPossiblePermissions = listOf(
            "LOCATION", "CAMERA", "CONTACTS", "RECORD_AUDIO", "READ_MEDIA",
            "STORAGE", "INTERNET", "BLUETOOTH", "VIBRATE", "CALENDAR",
            "PHONE", "SMS", "SENSORS", "ACTIVITY_RECOGNITION"
        )

        // Check if app has all dangerous permissions
        val hasAllPermissions = allPossiblePermissions.all { required ->
            permissions.any { it.contains(required, ignoreCase = true) }
        }

        if (hasAllPermissions) {
            showHighRiskNotification(permissions.size)
            return 0
        }

        val highRiskPerms = permissions.count { perm ->
            perm.contains("LOCATION") || perm.contains("CAMERA") ||
            perm.contains("CONTACTS") || perm.contains("RECORD_AUDIO") ||
            perm.contains("READ_MEDIA")
        }

        val mediumRiskPerms = permissions.count { perm ->
            perm.contains("STORAGE") || perm.contains("INTERNET") ||
            perm.contains("BLUETOOTH") || perm.contains("VIBRATE")
        }

        val lowRiskPerms = permissions.size - (highRiskPerms + mediumRiskPerms)

        val score = when {
            highRiskPerms > 0 -> 33 - (highRiskPerms * 3)
            mediumRiskPerms > 0 -> 66 - (mediumRiskPerms * 3)
            lowRiskPerms > 0 -> 99 - lowRiskPerms
            else -> 100
        }

        return score.coerceIn(0, 100)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "high_risk_channel",
                "High Risk Apps",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications for high-risk apps"
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun showHighRiskNotification(permissionCount: Int) {
        val notification = NotificationCompat.Builder(getApplication(), "high_risk_channel")
            .setSmallIcon(R.drawable.ic_high_risk)
            .setContentTitle("⚠️ High Risk App Detected!")
            .setContentText("An app with $permissionCount permissions poses significant privacy risk")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }
} 