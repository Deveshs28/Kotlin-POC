package com.example.kotlinpoc.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.kotlinpoc.data.model.database.LoginDataModel
import com.example.kotlinpoc.data.model.database.UserMainTable
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface QueryDataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLoginData(data: LoginDataModel): Completable

    @Query("SELECT * FROM login_details")
    fun getAllLoginDataRecords(): Single<List<LoginDataModel>>

    @Query("SELECT * FROM login_details WHERE username = :usrname")
    fun getLoginRecordForUsername(usrname: String): Single<LoginDataModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUserData(data: UserMainTable):Completable

    @Query("SELECT * FROM users")
    fun getAllUserDataRecords(): Single<List<UserMainTable>>

}