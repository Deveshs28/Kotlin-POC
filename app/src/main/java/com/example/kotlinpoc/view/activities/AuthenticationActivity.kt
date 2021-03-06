package com.example.kotlinpoc.view.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.kotlinpoc.R
import com.example.kotlinpoc.view.fragments.LoginFragment

class AuthenticationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)
        replaceFragment(LoginFragment.newInstance(), false)
    }

    private fun replaceFragment(fragment: Fragment, addToBackStack: Boolean) {
        if (addToBackStack) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, fragment, fragment::class.java.simpleName)
                .addToBackStack(fragment::class.java.simpleName).commit()
        } else {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, fragment, fragment::class.java.simpleName)
                .commit()
        }
    }
}