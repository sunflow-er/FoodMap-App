package com.masonk.foodmap

import android.app.Application
import android.content.Context

// Application: 가장 범용적, 최상위 개념
class FoodMapApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        
        // 앱이 onCreate될 때, applicationContext 할당
        FoodMapApplication.appContext = applicationContext
    }

    // 전역적으로 사용

    companion object {
        lateinit var appContext: Context

    }
}