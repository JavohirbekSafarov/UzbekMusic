package com.javokhirbekcoder.uzbekmusic

import android.app.Application
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import com.yandex.mobile.ads.common.MobileAds
import dagger.hilt.android.HiltAndroidApp

/*
Created by Javokhirbek on 04/03/2024 at 15:06
*/

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        MobileAds.initialize(this) {
            Log.d("TAG", " ---> Initialization AD completed!")
        }

    }
}