package com.datarakshak.app

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
        loadPermissions()
    }

    private fun setupRecyclerView() {
        permissionAdapter = PermissionAdapter()
        binding.recyclerViewPermissions.apply {
            layoutManager = LinearLayoutManager(this@PermissionScoreActivity)
            adapter = permissionAdapter
        }
    }

    private fun loadPermissions() {
        // TODO: Implement permission scanning logic
        val permissions = listOf(
            PermissionItem("Camera", "High Risk", R.drawable.ic_high_risk),
            PermissionItem("Location", "Medium Risk", R.drawable.ic_medium_risk),
            PermissionItem("Storage", "Low Risk", R.drawable.ic_low_risk)
        )
        permissionAdapter.submitList(permissions)
    }
} 