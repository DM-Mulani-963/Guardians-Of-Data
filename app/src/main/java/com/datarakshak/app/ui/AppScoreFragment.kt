package com.datarakshak.app.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.datarakshak.app.databinding.FragmentAppScoreBinding
import com.datarakshak.app.viewmodel.AppScoreViewModel

class AppScoreFragment : Fragment() {
    private var _binding: FragmentAppScoreBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AppScoreViewModel by viewModels()
    private lateinit var appScoreAdapter: AppScoreAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAppScoreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeViewModel()
        viewModel.scanApps()
    }

    private fun setupRecyclerView() {
        appScoreAdapter = AppScoreAdapter()
        binding.recyclerViewApps.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = appScoreAdapter
        }
    }

    private fun observeViewModel() {
        viewModel.apps.observe(viewLifecycleOwner) { apps ->
            appScoreAdapter.submitList(apps)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 