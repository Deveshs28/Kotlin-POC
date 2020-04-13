package com.example.kotlinpoc.di.module

import androidx.room.Room
import com.example.kotlinpoc.MyApplication
import com.example.kotlinpoc.dao.AppDatabase
import com.example.kotlinpoc.dao.QueryDataDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(val myApplication: MyApplication) {
    @Provides
    @Singleton
    fun provideApplication() = myApplication

    @Provides
    @Singleton
    fun provideAppDatabase(myApplication: MyApplication): AppDatabase = Room.databaseBuilder(
        myApplication,
        AppDatabase::class.java, "appdatabase_db"
    ).build()

    @Provides
    @Singleton
    fun provideQueryDao(appDatabase: AppDatabase): QueryDataDao = appDatabase.queryDataDao()
}