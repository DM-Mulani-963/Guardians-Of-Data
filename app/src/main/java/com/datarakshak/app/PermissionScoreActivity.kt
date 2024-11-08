package com.datarakshak.app

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.datarakshak.app.databinding.ActivityPermissionScoreBinding
import com.permissionx.guolindev.PermissionX

class PermissionScoreActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPermissionScoreBinding
    private lateinit var permissionAdapter: PermissionAdapter
    private val permissions = listOf(
        Manifest.permission.CAMERA,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.READ_CONTACTS,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPermissionScoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        checkPermissions()
    }

    private fun setupRecyclerView() {
        permissionAdapter = PermissionAdapter()
        binding.permissionList.apply {
            layoutManager = LinearLayoutManager(this@PermissionScoreActivity)
            adapter = permissionAdapter
        }
    }

    private fun checkPermissions() {
        PermissionX.init(this)
            .permissions(permissions)
            .onExplainRequestReason { scope, deniedList ->
                scope.showRequestReasonDialog(
                    deniedList,
                    "These permissions are essential for analyzing your privacy score",
                    "OK",
                    "Cancel"
                )
            }
            .request { allGranted, grantedList, deniedList ->
                updatePermissionList()
                calculatePrivacyScore()
            }
    }

    private fun calculatePrivacyScore() {
        var totalScore = 100
        var grantedHighRisk = 0
        var grantedMediumRisk = 0

        permissions.forEach { permission ->
            when {
                checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED -> {
                    when (getRiskLevel(permission)) {
                        RiskLevel.HIGH -> {
                            totalScore -= 15
                            grantedHighRisk++
                        }
                        RiskLevel.MEDIUM -> {
                            totalScore -= 10
                            grantedMediumRisk++
                        }
                        RiskLevel.LOW -> totalScore -= 5
                    }
                }
            }
        }

        binding.scoreProgressBar.progress = totalScore.coerceIn(0, 100)
        binding.overallScoreText.text = buildString {
            append("Overall Privacy Score: $totalScore%\n")
            if (grantedHighRisk > 0) {
                append("Warning: $grantedHighRisk high-risk permissions granted")
            }
        }
    }

    private fun updatePermissionList() {
        val permissionItems = permissions.map { permission ->
            PermissionItem(
                name = getPermissionName(permission),
                description = getPermissionDescription(permission),
                riskLevel = getRiskLevel(permission)
            )
        }
        permissionAdapter.submitList(permissionItems)
    }

    private fun getPermissionName(permission: String): String {
        return when (permission) {
            Manifest.permission.CAMERA -> "Camera"
            Manifest.permission.ACCESS_FINE_LOCATION -> "Location"
            Manifest.permission.READ_CONTACTS -> "Contacts"
            Manifest.permission.READ_EXTERNAL_STORAGE -> "Storage"
            else -> "Unknown Permission"
        }
    }

    private fun getPermissionDescription(permission: String): String {
        return when (permission) {
            Manifest.permission.CAMERA -> "Access to take photos and record videos"
            Manifest.permission.ACCESS_FINE_LOCATION -> "Access to precise location (GPS and network-based)"
            Manifest.permission.READ_CONTACTS -> "Access to view your contacts"
            Manifest.permission.READ_EXTERNAL_STORAGE -> "Access to photos, media, and files"
            else -> "Unknown permission description"
        }
    }

    private fun getRiskLevel(permission: String): RiskLevel {
        return when (permission) {
            Manifest.permission.ACCESS_FINE_LOCATION -> RiskLevel.HIGH
            Manifest.permission.READ_CONTACTS -> RiskLevel.HIGH
            Manifest.permission.CAMERA -> RiskLevel.MEDIUM
            else -> RiskLevel.LOW
        }
    }
} 