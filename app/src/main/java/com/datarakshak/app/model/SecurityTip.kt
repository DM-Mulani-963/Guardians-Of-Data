package com.datarakshak.app.model

data class SecurityTip(
    val appName: String,
    val packageName: String,
    val icon: android.graphics.drawable.Drawable?,
    val suspiciousPermissions: List<String>,
    val recommendation: String,
    val severity: RiskLevel
) 