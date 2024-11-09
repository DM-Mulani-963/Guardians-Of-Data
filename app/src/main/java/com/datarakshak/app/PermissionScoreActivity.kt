package com.datarakshak.app

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.datarakshak.app.databinding.ActivityPermissionScoreBinding

class PermissionScoreActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPermissionScoreBinding
    private lateinit var permissionAdapter: PermissionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPermissionScoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        scanInstalledApps()
    }

    private fun setupRecyclerView() {
        permissionAdapter = PermissionAdapter()
        binding.recyclerViewPermissions.apply {
            layoutManager = LinearLayoutManager(this@PermissionScoreActivity)
            adapter = permissionAdapter
        }
    }

    private fun scanInstalledApps() {
        val permissions = mutableListOf<PermissionItem>()
        
        try {
            val pm = packageManager
            val installedApps = pm.getInstalledPackages(PackageManager.GET_PERMISSIONS)
            
            for (packageInfo in installedApps) {
                val requestedPermissions = packageInfo.requestedPermissions
                if (requestedPermissions != null) {
                    for (permission in requestedPermissions) {
                        when {
                            permission.contains("CAMERA") -> {
                                permissions.add(PermissionItem("Camera", "High Risk", R.drawable.ic_high_risk))
                            }
                            permission.contains("LOCATION") -> {
                                permissions.add(PermissionItem("Location", "High Risk", R.drawable.ic_high_risk))
                            }
                            permission.contains("STORAGE") -> {
                                permissions.add(PermissionItem("Storage", "Medium Risk", R.drawable.ic_medium_risk))
                            }
                            permission.contains("INTERNET") -> {
                                permissions.add(PermissionItem("Internet", "Medium Risk", R.drawable.ic_medium_risk))
                            }
                            permission.contains("BLUETOOTH") -> {
                                permissions.add(PermissionItem("Bluetooth", "Low Risk", R.drawable.ic_low_risk))
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            // Fallback to basic permissions if scanning fails
            permissions.addAll(listOf(
                PermissionItem("Camera", "High Risk", R.drawable.ic_high_risk),
                PermissionItem("Location", "High Risk", R.drawable.ic_high_risk),
                PermissionItem("Storage", "Medium Risk", R.drawable.ic_medium_risk)
            ))
        }
        
        // Remove duplicates and sort by risk level
        val uniquePermissions = permissions.distinctBy { it.name }
            .sortedBy { 
                when (it.riskLevel) {
                    "High Risk" -> 0
                    "Medium Risk" -> 1
                    else -> 2
                }
            }
        
        permissionAdapter.submitList(uniquePermissions)
    }
} 