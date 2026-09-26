package com.example.runapp

import android.app.Application
import com.example.auth.presentation.di.authViewModelModule
import com.example.core.data.di.coreDataModule
import com.example.data.di.authDataModule
import com.example.run.presentation.di.runViewModelModule
import com.example.runapp.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class RunApp: Application() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        startKoin {
            androidLogger()
            androidContext(this@RunApp)
            modules(
                appModule,
                authDataModule,
                authViewModelModule,
                coreDataModule,
                runViewModelModule
            )
        }
    }
}