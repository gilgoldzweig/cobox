package com.example.gilgoldzweig.cobox.modules.di.fragments

import com.example.gilgoldzweig.cobox.activities.MainActivity
import dagger.Component

@Component(modules = [FragmentsModule::class])
interface FragmentsComponent {

    fun inject(mainActivity: MainActivity)
}