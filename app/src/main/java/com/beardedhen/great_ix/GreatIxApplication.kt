package com.beardedhen.great_ix

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context


class GreatIxApplication : Application() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit  var context: Context
    }

//    lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()

        context = this

//        applicationComponent = DaggerConfigurationComponent.builder()
//            .configurationModule( ConfigurationModule(this) )
//            .build()
//
////        applicationComponent.inject(this)
//
//        EventBus.builder().throwSubscriberException(true).installDefaultEventBus()
    }
}
