package com.datarakshak.app

import android.Manifest
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.datarakshak.app.databinding.ActivityPermissionScoreBinding
import com.permissionx.guolindev.PermissionX

class PermissionScoreActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPermissionScoreBinding
    private lateinit var permissionAdapter: PermissionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPermissionScoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        checkPermissions()
        calculatePrivacyScore()
    }

    private fun setupRecyclerView() {
        permissionAdapter = PermissionAdapter()
        binding.permissionList.apply {
            layoutManager = LinearLayoutManager(this@PermissionScoreActivity)
            adapter = permissionAdapter
        }
    }

    private fun checkPermissions() {
        val permissions = listOf(
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )

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
                if (allGranted) {
                    Toast.makeText(this, "All permissions granted", Toast.LENGTH_SHORT).show()
                    updatePermissionList(grantedList)
                } else {
                    Toast.makeText(
                        this,
                        "These permissions are denied: $deniedList",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun calculatePrivacyScore() {
        // Simple scoring logic (can be enhanced based on requirements)
        val score = 75 // Example score
        binding.scoreProgressBar.progress = score
        binding.overallScoreText.text = "Overall Privacy Score: $score%"
    }

    private fun updatePermissionList(grantedPermissions: List<String>) {
        val permissionItems = grantedPermissions.map { permission ->
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
            Manifest.permission.CAMERA -> "Access to device camera"
            Manifest.permission.ACCESS_FINE_LOCATION -> "Precise location access"
            Manifest.permission.READ_CONTACTS -> "Access to contacts"
            Manifest.permission.READ_EXTERNAL_STORAGE -> "Access to files and media"
            else -> "Unknown permission description"
        }
    }

    private fun getRiskLevel(permission: String): RiskLevel {
        return when (permission) {
            Manifest.permission.ACCESS_FINE_LOCATION -> RiskLevel.HIGH
            Manifest.permission.READ_CONTACTS -> RiskLevel.MEDIUM
            else -> RiskLevel.LOW
        }
    }
} 