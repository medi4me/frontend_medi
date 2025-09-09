package com.example.mediforme

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MediformeApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // 여기에 앱 초기화에 필요한 추가적인 로직을 넣을 수 있습니다.
        // 예: Timber.plant(Timber.DebugTree())
    }
}