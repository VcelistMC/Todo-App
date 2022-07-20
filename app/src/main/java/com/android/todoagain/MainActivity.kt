package com.android.todoagain

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.android.todoagain.fragments.MainFragment
import com.android.todoagain.models.ActionType

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        val mainFragment = MainFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_screen, mainFragment)
            .addToBackStack("Main List")
            .commit()


//        setSupportActionBar(findViewById(R.id.my_toolbar))
    }
}