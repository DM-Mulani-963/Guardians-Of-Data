package com.datarakshak.app.ui

import android.os.Bundle
import androidx.activity.viewModels
import com.datarakshak.app.base.BaseActivity
import com.datarakshak.app.databinding.ActivityAppDetailBinding
import com.datarakshak.app.viewmodel.AppDetailViewModel

class AppDetailActivity : BaseActivity<ActivityAppDetailBinding>() {
    private val viewModel: AppDetailViewModel by viewModels()

    override fun getViewBinding() = ActivityAppDetailBinding.inflate(layoutInflater)

    override fun setupUI() {
        val packageName = intent.getStringExtra("package_name") ?: return
        val score = intent.getIntExtra("score", 0)
        val riskLevel = intent.getStringExtra("risk_level")

        viewModel.loadAppDetails(packageName)
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.appDetails.observe(this) { details ->
            binding.apply {
                textAppName.text = details.appName
                textScore.text = "${details.score}%"
                textRiskLevel.text = details.riskLevel.name
                imageAppIcon.setImageDrawable(details.icon)
                
                // Set up permissions list
                recyclerViewPermissions.adapter = PermissionDetailAdapter(details.permissions)
                
                // Show recommendations
                textRecommendations.text = buildRecommendations(details)
            }
        }
    }

    private fun buildRecommendations(details: AppDetails): String {
        return buildString {
            append("Recommendations:\n\n")
            if (details.score < 33) {
                append("⚠️ High Risk App - Consider uninstalling or restricting permissions\n")
            }
            details.permissions.forEach { permission ->
                append("• ${permission.name}: ${permission.recommendation}\n")
            }
        }
    }
} 