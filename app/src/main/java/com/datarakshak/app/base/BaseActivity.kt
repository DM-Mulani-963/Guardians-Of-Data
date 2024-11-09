package com.datarakshak.app.base
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {
    // ViewBinding instance to be initialized in onCreate
    protected lateinit var binding: VB
    // This method should be implemented to return the ViewBinding instance for the activity
    abstract fun getViewBinding(): VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getViewBinding()
        setContentView(binding.root)
        setupUI()
    }
    // Abstract method to set up the UI components
    abstract fun setupUI()
