package com.eseo.getmyspot

import android.app.Application
import com.eseo.getmyspot.di.moduleApp
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MainApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        // start Koin context
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@MainApplication)
            modules(moduleApp)
        }
    }
}