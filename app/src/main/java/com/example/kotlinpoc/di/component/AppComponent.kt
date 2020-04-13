package com.example.kotlinpoc.di.component

import android.content.Context
import com.example.kotlinpoc.MyApplication
import com.example.kotlinpoc.di.module.ApiModule
import com.example.kotlinpoc.di.module.AppModule
import com.example.kotlinpoc.di.module.ContextModule
import com.example.kotlinpoc.view.fragments.LoginFragment
import com.example.kotlinpoc.view.fragments.UserListFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, ApiModule::class, ContextModule::class])
interface AppComponent {
    fun inject(application: MyApplication)
    fun inject(context: Context)
    fun inject(loginFragment: LoginFragment)
    fun inject(userListFragment: UserListFragment)
}