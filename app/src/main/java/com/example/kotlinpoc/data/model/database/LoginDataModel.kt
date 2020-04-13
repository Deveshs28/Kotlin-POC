package com.example.kotlinpoc.data.model.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "login_details")
data class LoginDataModel(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int? = null,
    @ColumnInfo(name = "username")
    var username: String? = null,
    @ColumnInfo(name = "password")
    var password: String? = null
)