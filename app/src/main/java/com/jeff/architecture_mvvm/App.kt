package com.jeff.architecture_mvvm

import android.app.Application
import com.facebook.stetho.Stetho
import com.jeff.architecture_mvvm.di.apiModule
import com.jeff.architecture_mvvm.di.viewModelModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App : Application() {
    companion object {
        lateinit var instance: App
            private set
    }

    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this)
        }

        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@App)
            modules(listOf(apiModule, viewModelModules))
        }
    }
}