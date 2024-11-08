package com.datarakshak.app

import android.Manifest
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.datarakshak.app.databinding.ItemPermissionBinding

class PermissionAdapter : ListAdapter<PermissionItem, PermissionAdapter.ViewHolder>(PermissionDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPermissionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(private val binding: ItemPermissionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PermissionItem) {
            binding.apply {
                permissionName.text = item.name
                permissionDescription.text = item.description
                
                // Automatically set permission icon based on permission type
                permissionIcon.setImageResource(getPermissionIcon(item.name))
                
                // Automatically set risk level icon
                riskLevel.setImageResource(getRiskLevelIcon(item.riskLevel))
            }
        }

        private fun getPermissionIcon(permissionName: String): Int {
            return when (permissionName) {
                "Camera" -> android.R.drawable.ic_menu_camera
                "Location" -> android.R.drawable.ic_menu_mylocation
                "Contacts" -> android.R.drawable.ic_menu_my_calendar
                "Storage" -> android.R.drawable.ic_menu_save
                else -> android.R.drawable.ic_menu_help
            }
        }

        private fun getRiskLevelIcon(riskLevel: RiskLevel): Int {
            return when (riskLevel) {
                RiskLevel.HIGH -> R.drawable.ic_high_risk
                RiskLevel.MEDIUM -> R.drawable.ic_medium_risk
                RiskLevel.LOW -> R.drawable.ic_low_risk
            }
        }
    }
}

class PermissionDiffCallback : DiffUtil.ItemCallback<PermissionItem>() {
    override fun areItemsTheSame(oldItem: PermissionItem, newItem: PermissionItem): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: PermissionItem, newItem: PermissionItem): Boolean {
        return oldItem == newItem
    }
}

data class PermissionItem(
    val name: String,
    val description: String,
    val riskLevel: RiskLevel
)

enum class RiskLevel {
    HIGH,
    MEDIUM,
    LOW
} 