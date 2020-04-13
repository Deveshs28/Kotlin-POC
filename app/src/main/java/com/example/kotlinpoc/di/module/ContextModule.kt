package com.example.kotlinpoc.di.module

import android.content.Context
import dagger.Module
import dagger.Provides

@Module
class ContextModule(private val appContext: Context) {
    @Provides
    fun provideContext(): Context {
        return appContext
    }
}