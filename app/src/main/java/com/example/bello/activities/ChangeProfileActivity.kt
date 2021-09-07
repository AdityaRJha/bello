package com.example.bello.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.bello.R

class ChangeProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_profile)

        supportActionBar?.hide()
    }
}