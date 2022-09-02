package com.bbj.kinono

import android.app.Application
import com.bbj.kinono.di.appModule
import com.bbj.kinono.di.dataModule
import com.bbj.kinono.di.domainModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class CustomApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin{
            androidContext(this@CustomApplication)
            androidLogger(Level.DEBUG)
            modules(listOf(appModule, dataModule, domainModule))
        }

    }


}