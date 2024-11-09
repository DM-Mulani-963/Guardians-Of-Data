package com.datarakshak.app.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.datarakshak.app.databinding.ItemSecurityTipBinding
import com.datarakshak.app.model.SecurityTip

class SecurityTipsAdapter : ListAdapter<SecurityTip, SecurityTipsAdapter.SecurityTipViewHolder>(SecurityTipDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SecurityTipViewHolder {
        val binding = ItemSecurityTipBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SecurityTipViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SecurityTipViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class SecurityTipViewHolder(
        private val binding: ItemSecurityTipBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: SecurityTip) {
            binding.apply {
                imageAppIcon.setImageDrawable(item.icon)
                textAppName.text = item.appName
                textSuspiciousPermissions.text = item.suspiciousPermissions.joinToString("\n")
                textRecommendation.text = item.recommendation
                cardTip.setCardBackgroundColor(
                    binding.root.context.getColor(
                        when (item.severity) {
                            RiskLevel.HIGH -> com.datarakshak.app.R.color.risk_high
                            RiskLevel.MEDIUM -> com.datarakshak.app.R.color.risk_medium
                            RiskLevel.LOW -> com.datarakshak.app.R.color.risk_low
                        }
                    )
                )
            }
        }
    }

    private class SecurityTipDiffCallback : DiffUtil.ItemCallback<SecurityTip>() {
        override fun areItemsTheSame(oldItem: SecurityTip, newItem: SecurityTip): Boolean {
            return oldItem.packageName == newItem.packageName
        }

        override fun areContentsTheSame(oldItem: SecurityTip, newItem: SecurityTip): Boolean {
            return oldItem == newItem
        }
    }
} 