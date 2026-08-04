package com.ultron.ai

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class UltronApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize any global singletons here
    }
}