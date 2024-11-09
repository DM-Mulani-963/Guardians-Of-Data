package com.datarakshak.app.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.PendingIntentCompat
import com.datarakshak.app.R
import com.datarakshak.app.model.RiskLevel

class AppInstallReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            Intent.ACTION_PACKAGE_ADDED -> {
                val packageName = intent.data?.schemeSpecificPart ?: return
                analyzeNewApp(context, packageName)
            }
        }
    }

    private fun analyzeNewApp(context: Context, packageName: String) {
        try {
            val pm = context.packageManager
            val packageInfo = pm.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS)
            val permissions = packageInfo.requestedPermissions?.toList() ?: emptyList()
            
            // Calculate risk score
            val score = calculateScore(permissions)
            val riskLevel = when {
                score <= 33 -> RiskLevel.HIGH
                score <= 66 -> RiskLevel.MEDIUM
                else -> RiskLevel.LOW
            }

            // Show notification
            showNotification(
                context,
                packageInfo.applicationInfo.loadLabel(pm).toString(),
                score,
                riskLevel,
                permissions.size
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun calculateScore(permissions: List<String>): Int {
        if (permissions.isEmpty()) return 100

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

        return when {
            highRiskPerms > 0 -> 33 - (highRiskPerms * 3)
            mediumRiskPerms > 0 -> 66 - (mediumRiskPerms * 3)
            lowRiskPerms > 0 -> 99 - lowRiskPerms
            else -> 100
        }.coerceIn(0, 100)
    }

    private fun showNotification(
        context: Context,
        appName: String,
        score: Int,
        riskLevel: RiskLevel,
        permissionCount: Int
    ) {
        val channelId = "new_app_install_channel"
        val notificationId = System.currentTimeMillis().toInt()

        // Create deep link intent
        val detailIntent = Intent(context, AppDetailActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("package_name", packageName)
            putExtra("score", score)
            putExtra("risk_level", riskLevel.name)
        }

        val pendingIntent = PendingIntentCompat.getActivity(
            context,
            notificationId,
            detailIntent,
            PendingIntentCompat.FLAG_UPDATE_CURRENT or PendingIntentCompat.FLAG_IMMUTABLE
        )

        val icon = when (riskLevel) {
            RiskLevel.HIGH -> R.drawable.ic_risk_high
            RiskLevel.MEDIUM -> R.drawable.ic_risk_medium
            RiskLevel.LOW -> R.drawable.ic_risk_low
        }

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(icon)
            .setContentTitle("New App Installed: $appName")
            .setContentText("Privacy Score: $score% | Permissions: $permissionCount")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText("Privacy Score: $score%\nPermissions Required: $permissionCount\n" +
                        "Risk Level: ${riskLevel.name}\n" +
                        "Tap to view detailed analysis"))
            .build()

        NotificationManagerCompat.from(context).notify(notificationId, notification)
    }
} 