package com.datarakshak.app

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.datarakshak.app.databinding.ActivityMainBinding
import com.permissionx.guolindev.PermissionX

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
    }

    private fun setupUI() {
        binding.btnCheckPermissions.setOnClickListener {
            checkPermissions()
        }
    }

    private fun checkPermissions() {
        PermissionX.init(this)
            .permissions(
                android.Manifest.permission.READ_MEDIA_IMAGES,
                android.Manifest.permission.READ_MEDIA_VIDEO,
                android.Manifest.permission.READ_MEDIA_AUDIO
            )
            .onExplainRequestReason { scope, deniedList ->
                scope.showRequestReasonDialog(
                    deniedList,
                    "Core functionality requires these permissions",
                    "OK",
                    "Cancel"
                )
            }
            .onForwardToSettings { scope, deniedList ->
                scope.showForwardToSettingsDialog(
                    deniedList,
                    "You need to allow necessary permissions in Settings manually",
                    "OK",
                    "Cancel"
                )
            }
            .request { allGranted, _, _ ->
                if (allGranted) {
                    startActivity(Intent(this, PermissionScoreActivity::class.java))
                }
            }
    }
} 