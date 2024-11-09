package com.datarakshak.app.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.datarakshak.app.databinding.ItemAppScoreBinding
import com.datarakshak.app.model.AppScore

class AppScoreAdapter : ListAdapter<AppScore, AppScoreAdapter.AppScoreViewHolder>(AppScoreDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppScoreViewHolder {
        val binding = ItemAppScoreBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AppScoreViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AppScoreViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class AppScoreViewHolder(
        private val binding: ItemAppScoreBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: AppScore) {
            binding.apply {
                imageAppIcon.setImageDrawable(item.icon)
                textAppName.text = item.appName
                textScore.text = "${item.score}%"
                progressScore.progress = item.score
                progressScore.setIndicatorColor(
                    when {
                        item.score <= 33 -> binding.root.context.getColor(com.datarakshak.app.R.color.risk_high)
                        item.score <= 66 -> binding.root.context.getColor(com.datarakshak.app.R.color.risk_medium)
                        else -> binding.root.context.getColor(com.datarakshak.app.R.color.risk_low)
                    }
                )
            }
        }
    }

    private class AppScoreDiffCallback : DiffUtil.ItemCallback<AppScore>() {
        override fun areItemsTheSame(oldItem: AppScore, newItem: AppScore): Boolean {
            return oldItem.packageName == newItem.packageName
        }

        override fun areContentsTheSame(oldItem: AppScore, newItem: AppScore): Boolean {
            return oldItem == newItem
        }
    }
} 