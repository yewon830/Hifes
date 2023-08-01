package com.ssafy.hifes

import android.app.Application
import android.content.SharedPreferences
import com.ssafy.hifes.data.local.AppPreferences
import com.ssafy.hifes.data.AppContainer
import com.ssafy.hifes.data.AppContainerImpl
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {
    companion object {
        lateinit var prefs: SharedPreferences
        lateinit var shuttleBusPrefs: SharedPreferences
    }

    lateinit var container: AppContainer

    override fun onCreate() {
        prefs = AppPreferences.openSharedPreferences(applicationContext)
        container = AppContainerImpl(this)
        super.onCreate()
    }
}