package com.datarakshak.app

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.datarakshak.app.databinding.ItemPermissionBinding
import com.datarakshak.app.model.PermissionItem

class PermissionAdapter : ListAdapter<PermissionItem, PermissionAdapter.PermissionViewHolder>(PermissionDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PermissionViewHolder {
        val binding = ItemPermissionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PermissionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PermissionViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class PermissionViewHolder(
        private val binding: ItemPermissionBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: PermissionItem) {
            binding.textPermissionName.text = item.name
            binding.textRiskLevel.text = item.riskLevel
            binding.imageRiskLevel.setImageResource(item.riskIcon)
        }
    }

    private class PermissionDiffCallback : DiffUtil.ItemCallback<PermissionItem>() {
        override fun areItemsTheSame(oldItem: PermissionItem, newItem: PermissionItem): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: PermissionItem, newItem: PermissionItem): Boolean {
            return oldItem == newItem
        }
    }
} 