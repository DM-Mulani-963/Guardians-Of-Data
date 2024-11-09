package com.datarakshak.app

import android.Manifest
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.datarakshak.app.databinding.ActivityPermissionScoreBinding
import com.datarakshak.app.viewmodel.PermissionViewModel
import com.permissionx.guolindev.PermissionX

class PermissionScoreActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPermissionScoreBinding
    private lateinit var permissionAdapter: PermissionAdapter
    private val viewModel: PermissionViewModel by viewModels()
    
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
        setupObservers()
        checkPermissions()
    }

    private fun setupRecyclerView() {
        permissionAdapter = PermissionAdapter()
        binding.permissionList.apply {
            layoutManager = LinearLayoutManager(this@PermissionScoreActivity)
            adapter = permissionAdapter
        }
    }

    private fun setupObservers() {
        viewModel.permissionItems.observe(this) { items ->
            permissionAdapter.submitList(items)
        }

        viewModel.privacyScore.observe(this) { score ->
            binding.scoreProgressBar.progress = score
            binding.overallScoreText.text = "Overall Privacy Score: $score%"
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
                if (deniedList.isNotEmpty()) {
                    Toast.makeText(this, 
                        "Some permissions were denied. This may affect your privacy score.", 
                        Toast.LENGTH_LONG
                    ).show()
                }
                viewModel.updatePermissions(permissions)
            }
    }
} 