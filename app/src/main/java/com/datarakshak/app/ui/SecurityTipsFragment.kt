package com.datarakshak.app.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.datarakshak.app.databinding.FragmentSecurityTipsBinding
import com.datarakshak.app.viewmodel.SecurityTipsViewModel

class SecurityTipsFragment : Fragment() {
    private var _binding: FragmentSecurityTipsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SecurityTipsViewModel by viewModels()
    private lateinit var securityTipsAdapter: SecurityTipsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSecurityTipsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeViewModel()
        viewModel.analyzeSecurity()
    }

    private fun setupRecyclerView() {
        securityTipsAdapter = SecurityTipsAdapter()
        binding.recyclerViewTips.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = securityTipsAdapter
        }
    }

    private fun observeViewModel() {
        viewModel.securityTips.observe(viewLifecycleOwner) { tips ->
            securityTipsAdapter.submitList(tips)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 