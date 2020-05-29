package com.kottapa.youtubedemo

import android.app.Application
import timber.log.Timber

class MasterApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        //init Timber log
        Timber.plant(Timber.DebugTree())
    }
}