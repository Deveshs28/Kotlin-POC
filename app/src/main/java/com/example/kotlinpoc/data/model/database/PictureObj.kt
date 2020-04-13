package com.example.kotlinpoc.data.model.database

import androidx.room.ColumnInfo

data class PictureObj(
    @ColumnInfo(name = "large")
    var large: String? = null,
    @ColumnInfo(name = "medium")
    var medium: String? = null,
    @ColumnInfo(name = "thumbnail")
    var thumbnail: String? = null
)