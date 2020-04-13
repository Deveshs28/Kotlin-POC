package com.example.kotlinpoc.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kotlinpoc.data.model.database.LoginDataModel
import com.example.kotlinpoc.repository.DatabaseRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableCompletableObserver
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class LoginViewModel(private val databaseRepository: DatabaseRepository) : ViewModel() {
    private var disposable: CompositeDisposable? = null
    val loading = MutableLiveData<Boolean>()
    val error = MutableLiveData<Boolean>()
    val success = MutableLiveData<Boolean>()
    private val errorMessage = MutableLiveData<String>()
    private val successMessage = MutableLiveData<String>()
    private val userModel = MutableLiveData<LoginDataModel>()

    init {
        disposable = CompositeDisposable()
    }

    fun getError(): LiveData<Boolean> {
        return error
    }

    fun getLoading(): LiveData<Boolean> {
        return loading
    }

    fun getErrorMessage(): LiveData<String> {
        return errorMessage
    }

    fun getSuccessMessage(): LiveData<String> {
        return successMessage
    }

    fun getSuccess(): LiveData<Boolean> {
        return success
    }

    fun getUserModel():LiveData<LoginDataModel>{
        return userModel
    }

    fun processLogin(userName: String?, password: String?, isRememberChecked: Boolean) {
        loading.value = true
        if (validationFailed(userName)) {
            return
        }
        if (validationFailed(password)) {
            return
        }
        if (isRememberChecked) {
            val model = LoginDataModel()
            model.username = userName
            model.password = password
            disposable?.add(
                databaseRepository.insetLoginData(model)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : DisposableCompletableObserver() {
                        override fun onComplete() {
                            loading.value = false
                            error.value = false
                            success.value = true
                            successMessage.value = "Login Successful"
                        }

                        override fun onError(e: Throwable) {
                            loading.value = false
                            error.value = true
                            success.value = false
                            errorMessage.value = "Login Failed"
                        }
                    })
            )
        } else {
            loading.value = false
            error.value = false
            success.value = true
            successMessage.value = "Login Successful"
        }
    }

    fun fetchPassword(username: String) {
        loading.value = true
        disposable?.add(
            databaseRepository.getLoginRecordByUsername(username)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<LoginDataModel>() {
                    override fun onSuccess(t: LoginDataModel) {
                        loading.value = false
                        userModel.value = t
                    }

                    override fun onError(e: Throwable) {
                        loading.value = false
                        errorMessage.value = e.message
                    }
                })
        )
    }

    private fun validationFailed(queryString: String?): Boolean {
        if (queryString.isNullOrBlank()) {
            loading.value = false
            error.value = true
            errorMessage.value = "Please check username"
            return true
        }
        return false
    }

    override fun onCleared() {
        super.onCleared()
        if (disposable != null) {
            disposable?.clear();
            disposable = null;
        }
    }
}