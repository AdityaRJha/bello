package com.example.bello.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.bello.R

class AccountSettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_settings)

        supportActionBar?.hide()
    }
}