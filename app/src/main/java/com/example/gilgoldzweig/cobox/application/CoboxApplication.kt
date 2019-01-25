package com.example.gilgoldzweig.cobox.application

import android.app.Application
import com.example.gilgoldzweig.cobox.BuildConfig
import com.example.gilgoldzweig.cobox.modules.di.ApplicationComponent
import com.example.gilgoldzweig.cobox.modules.di.DaggerApplicationComponent
import com.example.gilgoldzweig.cobox.modules.di.fragments.DaggerFragmentsComponent
import com.example.gilgoldzweig.cobox.modules.di.fragments.FragmentsComponent
import com.example.gilgoldzweig.cobox.timber.Timber
import com.example.gilgoldzweig.cobox.timber.Timber.crash
import com.github.anrwatchdog.ANRWatchDog
import com.squareup.leakcanary.LeakCanary

class CoboxApplication : Application() {

    lateinit var applicationComponent: ApplicationComponent
    lateinit var fragmentsComponent: FragmentsComponent

    override fun onCreate() {
        super.onCreate()

        if (LeakCanary.isInAnalyzerProcess(this)) return

        LeakCanary.install(this)

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        applicationComponent = DaggerApplicationComponent.create()
        fragmentsComponent = DaggerFragmentsComponent.create()


        ANRWatchDog()
            .setIgnoreDebugger(true)
            .setANRListener(::crash)
            .setInterruptionListener(::crash)
            .start()
    }
}
