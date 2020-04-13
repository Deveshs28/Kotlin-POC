package com.example.kotlinpoc.data.rest

import com.example.kotlinpoc.data.model.PostInfo
import com.example.kotlinpoc.data.model.UserMainModel
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("post")
    fun makeRequest(): Single<List<PostInfo>>

    @GET("/api")
    fun fetchUserList(
        @Query("results") resCount: String,
        @Query("page") page: String
    ): Single<UserMainModel>
}