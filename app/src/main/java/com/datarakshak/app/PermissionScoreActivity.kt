package com.datarakshak.app

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.datarakshak.app.databinding.ActivityPermissionScoreBinding
import com.datarakshak.app.viewmodel.PermissionViewModel

class PermissionScoreActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPermissionScoreBinding
    private lateinit var permissionAdapter: PermissionAdapter
    private val viewModel: PermissionViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPermissionScoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        observeViewModel()
        viewModel.scanPermissions()
    }

    private fun setupRecyclerView() {
        permissionAdapter = PermissionAdapter()
        binding.recyclerViewPermissions.apply {
            layoutManager = LinearLayoutManager(this@PermissionScoreActivity)
            adapter = permissionAdapter
        }
    }

    private fun observeViewModel() {
        viewModel.permissions.observe(this) { permissions ->
            permissionAdapter.submitList(permissions)
        }
    }
} 