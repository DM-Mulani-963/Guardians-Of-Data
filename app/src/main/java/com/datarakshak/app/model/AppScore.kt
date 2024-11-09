package com.datarakshak.app.model

data class AppScore(
    val appName: String,
    val packageName: String,
    val score: Int,
    val permissions: List<String>,
    val riskLevel: RiskLevel,
    val icon: android.graphics.drawable.Drawable?
) 