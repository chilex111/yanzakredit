package com.yanza.kredit.helper

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate

class Yanza : Application() {

    init {
        instance = this
    }

    companion object {
        var instance: Yanza? = null
        val applicationContext : Context

            get() = instance!!.applicationContext
    }



    override fun onCreate() {
        super.onCreate()
       // Fabric.with(this, Crashlytics())
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)

        //startKoin(this, listOf(viewModelModule, repositoryModule))

       // Stetho.initializeWithDefaults(this)
    }
}