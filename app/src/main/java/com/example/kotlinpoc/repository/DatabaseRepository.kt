package com.example.kotlinpoc.repository

import com.example.kotlinpoc.dao.QueryDataDao
import com.example.kotlinpoc.data.model.database.LoginDataModel
import com.example.kotlinpoc.data.model.database.UserMainTable
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class DatabaseRepository @Inject constructor(val queryDataDao: QueryDataDao) {

    fun insetLoginData(loginDataModel: LoginDataModel): Completable {
        return queryDataDao.insertLoginData(loginDataModel)
    }

    fun getLoginRecordByUsername(username: String): Single<LoginDataModel> {
        return queryDataDao.getLoginRecordForUsername(username)
    }

    fun insetUserData(userMainTable: UserMainTable): Completable {
        return queryDataDao.insertUserData(userMainTable)
    }

    fun getAllUserData(): Single<List<UserMainTable>> {
        return queryDataDao.getAllUserDataRecords()
    }
}