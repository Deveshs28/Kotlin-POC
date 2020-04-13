package com.example.kotlinpoc.data.model

data class UserResultModel(
    var gender: String?,
    var email: String?,
    var phone: String?,
    var cell: String?,
    var name: NameModel?,
    var location: LocationModel?,
    var picture: PictureModel?
)