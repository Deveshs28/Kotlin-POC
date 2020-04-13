package com.example.kotlinpoc

import android.app.Application
import android.content.Context
import com.example.kotlinpoc.di.component.AppComponent
import com.example.kotlinpoc.di.component.DaggerAppComponent
import com.example.kotlinpoc.di.module.ApiModule
import com.example.kotlinpoc.di.module.AppModule

class MyApplication : Application() {
    companion object {
        var ctx: Context? = null
        lateinit var apiComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        ctx = applicationContext
        apiComponent = initDaggerComponent()
    }

    fun getMyComponent(): AppComponent {
        return apiComponent
    }

    private fun initDaggerComponent(): AppComponent {
        apiComponent = DaggerAppComponent
            .builder()
            .appModule(AppModule(this))
            .apiModule(ApiModule())
            .build()
        return apiComponent
    }
}