package com.example.kotlinpoc.repository

import com.example.kotlinpoc.data.model.PostInfo
import com.example.kotlinpoc.data.rest.ApiService
import io.reactivex.Single

class RetrofitRepository(repoService: ApiService) {
    private var repoService: ApiService = repoService

    fun getRepositories(): Single<List<PostInfo>> {
        return repoService.makeRequest()
    }
}