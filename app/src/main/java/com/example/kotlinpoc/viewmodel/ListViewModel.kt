package com.example.kotlinpoc.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kotlinpoc.MyApplication
import com.example.kotlinpoc.data.model.NameModel
import com.example.kotlinpoc.data.model.PictureModel
import com.example.kotlinpoc.data.model.UserMainModel
import com.example.kotlinpoc.data.model.UserResultModel
import com.example.kotlinpoc.data.model.database.NameObj
import com.example.kotlinpoc.data.model.database.PictureObj
import com.example.kotlinpoc.data.model.database.UserMainTable
import com.example.kotlinpoc.data.rest.ApiService
import com.example.kotlinpoc.repository.DatabaseRepository
import com.example.kotlinpoc.utils.ConnectivityReceiver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableCompletableObserver
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class ListViewModel(
    private val app: MyApplication,
    private val apiService: ApiService,
    private val databaseRepository: DatabaseRepository
) :
    ViewModel() {

    private var disposable: CompositeDisposable? = null

    val userList = MutableLiveData<List<UserResultModel>>()
    val loadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()
    val errorMessage = MutableLiveData<String>()

    init {
        disposable = CompositeDisposable()
    }

    fun getUserList(): LiveData<List<UserResultModel>> {
        return userList
    }

    fun getError(): LiveData<Boolean> {
        return loadError
    }

    fun getLoading(): LiveData<Boolean> {
        return loading
    }

    fun getErrorMessage(): LiveData<String> {
        return errorMessage
    }

    fun fetchUserList(page: Int) {
        loading.value = true
        app.getMyComponent()
        if (!ConnectivityReceiver.isConnected(app.applicationContext)) {
            fetchFromDatabase()
        }else {
            fetchFromApi(page)
        }
    }

    private fun fetchFromApi(page: Int) {
        disposable?.add(
            apiService.fetchUserList("15", page.toString()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribeWith(object :
                    DisposableSingleObserver<UserMainModel>() {
                    override fun onSuccess(t: UserMainModel) {
                        loading.value = false
                        if (t.error.isNullOrEmpty()) {
                            loadError.value = false
                            userList.value = t.results
                            saveListToDatabase(t.results)
                        } else {
                            loadError.value = true
                            errorMessage.value = t.error
                        }
                    }

                    override fun onError(e: Throwable) {
                        loading.value = false
                        loadError.value = true
                        errorMessage.value = e.message
                    }
                })
        )
    }

    private fun fetchFromDatabase() {
        disposable?.add(
            databaseRepository.getAllUserData().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribeWith(object : DisposableSingleObserver<List<UserMainTable>>(){
                    override fun onSuccess(t: List<UserMainTable>) {
                        loading.value = false
                        if (!t.isEmpty()){
                            loadError.value = false
                            val tempList = ArrayList<UserResultModel>()
                            for(obj in t){
                                val nameModel = NameModel(obj.name?.title, obj.name?.first, obj.name?.last)
                                val pictureModel = PictureModel(obj.picture?.large, obj.picture?.medium, obj.picture?.thumbnail)
                                val resultModel = UserResultModel(obj.gender, obj.email, obj.phone,obj.cell, nameModel, null, pictureModel)
                                tempList.add(resultModel)
                            }
                            userList.value = tempList
                        } else {
                            loadError.value = true
                            errorMessage.value = "Please check internet connection"
                        }
                    }

                    override fun onError(e: Throwable) {
                        loading.value = false
                        loadError.value = true
                        errorMessage.value = e.message
                    }

                })
        )
    }

    private fun saveListToDatabase(results: ArrayList<UserResultModel>?) {
        for (user in results!!) {
            val nameObj = NameObj()
            nameObj.title = user.name?.title
            nameObj.first = user.name?.first
            nameObj.last = user.name?.last
            val pictureObj = PictureObj()
            pictureObj.large = user.picture?.large
            pictureObj.medium = user.picture?.medium
            pictureObj.thumbnail = user.picture?.thumbnail
            val userObj =
                UserMainTable(name = nameObj, picture = pictureObj)
            userObj.gender = user.gender
            userObj.email = user.email
            userObj.phone = user.phone
            userObj.cell = user.cell
            disposable?.add(
                databaseRepository.insetUserData(userObj).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : DisposableCompletableObserver() {
                        override fun onComplete() {
                        }

                        override fun onError(e: Throwable) {
                        }
                    })
            )
        }
    }

    fun fetchUserListBySearchQuery(query: String?){
        if (query.isNullOrEmpty()) {
            loadError.value = true
            errorMessage.value = "Please check search query"
            return
        }
        if (getUserList().value?.size == 0){
            loadError.value = true
            errorMessage.value = "Please check search query"
            return
        }
        val tempList = ArrayList<UserResultModel>()
        for (item in getUserList().value!!) {
            if (item.name!!.first.equals(query)) {
                tempList.add(item)
            }
        }
        if (tempList.size == 0){
            loadError.value = true
            errorMessage.value = "No Result Found"
            return
        }
        loadError.value = false
        userList.value = tempList
    }

    override fun onCleared() {
        super.onCleared()
        if (disposable != null) {
            disposable?.clear();
            disposable = null;
        }
    }

}