package com.datarakshak.app

import android.app.Application
import androidx.work.Configuration

class App : Application(), Configuration.Provider {
    override fun onCreate() {
        super.onCreate()
        // Initialize WorkManager is handled by the Configuration.Provider implementation
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setMinimumLoggingLevel(android.util.Log.INFO)
            .build()

    companion object {
        const val TAG = "DATARakshak"
    }
} 