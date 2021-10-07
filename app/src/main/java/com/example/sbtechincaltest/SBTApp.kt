package com.example.sbtechincaltest

import android.app.Application
import com.example.sbtechincaltest.di.repoModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class SBTApp: Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin()
    }

    private fun initKoin() {
        startKoin {
            androidContext(this@SBTApp)
            modules(repoModule)
        }
    }
}