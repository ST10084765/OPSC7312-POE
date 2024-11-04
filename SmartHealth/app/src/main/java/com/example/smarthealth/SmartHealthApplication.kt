package com.example.smarthealth

import android.app.Application
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger

class SmartHealthApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Initialize the Facebook SDK
        FacebookSdk.sdkInitialize(applicationContext)
        AppEventsLogger.activateApp(this)
    }
}
