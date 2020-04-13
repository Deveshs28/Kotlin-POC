package com.example.kotlinpoc.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kotlinpoc.MyApplication
import com.example.kotlinpoc.R
import com.example.kotlinpoc.data.rest.ApiService
import com.example.kotlinpoc.repository.DatabaseRepository
import javax.inject.Inject

class ViewModelFactory @Inject
constructor(private val apiService: ApiService, private val application: MyApplication, private val databaseRepository: DatabaseRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ListViewModel::class.java)) {
            return ListViewModel(application, apiService, databaseRepository) as T
        }else if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(databaseRepository) as T
        }
        throw IllegalArgumentException(application.getString(R.string.unknown_class))
    }
}