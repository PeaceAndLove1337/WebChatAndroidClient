package com.example.webchatclient.ui.presentation.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.webchatclient.R
import com.example.webchatclient.ui.presentation.fragment.LoginFragment

class LoginActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportFragmentManager
            .beginTransaction()
            .add(R.id.main_activity_container, LoginFragment.newInstance())
            .commit()
    }



    companion object{
        const val API_KEY="API_KEY"
    }
}