package com.datarakshak.app

import android.app.Application
import androidx.work.Configuration
import androidx.work.WorkManager

class App : Application(), Configuration.Provider {
    override fun onCreate() {
        super.onCreate()
        // Initialize WorkManager
        WorkManager.initialize(this, workManagerConfiguration)
    }

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setMinimumLoggingLevel(android.util.Log.INFO)
            .build()
    }

    companion object {
        const val TAG = "DATARakshak"
    }
} 