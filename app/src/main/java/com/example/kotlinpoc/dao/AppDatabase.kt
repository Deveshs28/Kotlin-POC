package com.example.kotlinpoc.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.kotlinpoc.data.model.database.LoginDataModel
import com.example.kotlinpoc.data.model.database.UserMainTable

@Database(entities = arrayOf(LoginDataModel::class, UserMainTable::class), version = 1)
abstract class AppDatabase :RoomDatabase(){
    abstract fun queryDataDao():QueryDataDao
}