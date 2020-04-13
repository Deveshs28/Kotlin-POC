package com.example.kotlinpoc.data.model.database

import androidx.room.ColumnInfo

data class NameObj(
    @ColumnInfo(name = "title")
    var title: String? = null,
    @ColumnInfo(name = "first")
    var first: String? = null,
    @ColumnInfo(name = "last")
    var last: String? = null
)