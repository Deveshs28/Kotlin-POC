package com.example.kotlinpoc.view.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.example.kotlinpoc.R
import com.example.kotlinpoc.view.fragments.UserListFragment

class MainActivity : AppCompatActivity() {
    private lateinit var toolbar: Toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initToolbar()
        replaceFragment(UserListFragment.newInstance(), true)
    }

    private fun initToolbar() {
        toolbar = findViewById(R.id.main_ab_toolbar)
        setSupportActionBar(toolbar)
        actionBar?.setHomeButtonEnabled(false);
        actionBar?.setDisplayHomeAsUpEnabled(false)
        actionBar?.setDisplayShowHomeEnabled(false)
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
