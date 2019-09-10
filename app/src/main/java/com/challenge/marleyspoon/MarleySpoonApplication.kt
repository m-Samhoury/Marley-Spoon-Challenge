package com.challenge.marleyspoon

import android.app.Application
import com.challenge.marleyspoon.di.repositoryModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

/**
 * @author moustafasamhoury
 * created on Tuesday, 10 Sep, 2019
 */

class MarleySpoonApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MarleySpoonApplication)

            if (BuildConfig.DEBUG) {
                androidLogger()
            }
            androidFileProperties()

            modules(repositoryModule)
        }
    }
}