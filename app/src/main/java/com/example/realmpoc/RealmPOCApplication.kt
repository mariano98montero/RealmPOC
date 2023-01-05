package com.example.realmpoc

import android.app.Application
import com.example.realmpoc.di.databaseModule
import com.example.realmpoc.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class RealmPOCApplication: Application() {
        override fun onCreate() {
            super.onCreate()
            startKoin {
                androidContext(this@RealmPOCApplication)
                modules(databaseModule, viewModelModule)
            }
        }
}
