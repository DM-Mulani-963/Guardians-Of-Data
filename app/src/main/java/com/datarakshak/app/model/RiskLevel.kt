package com.datarakshak.app.model

enum class RiskLevel(val displayName: String) {
    HIGH("High Risk"),
    MEDIUM("Medium Risk"),
    LOW("Low Risk");

    companion object {
        fun fromPermission(permission: String): RiskLevel {
            return when {
                permission.contains("CAMERA") || 
                permission.contains("LOCATION") || 
                permission.contains("CONTACTS") -> HIGH
                permission.contains("STORAGE") || 
                permission.contains("INTERNET") -> MEDIUM
                else -> LOW
            }
        }
    }
} 