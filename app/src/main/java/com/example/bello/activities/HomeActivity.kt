package com.example.bello.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.bello.databinding.ActivityHomeBinding
import com.google.firebase.auth.FirebaseAuth

class HomeActivity : AppCompatActivity() {

    private val mAuth = FirebaseAuth.getInstance()
    private val mAuthStateListener = FirebaseAuth.AuthStateListener {
        val user = mAuth.currentUser?.uid
        user?.let {
            startActivity(Intent (this, LoginActivity::class.java))
            finish()
        }
    }
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //removing the Action Bar
        supportActionBar?.hide()

        binding.button.setOnClickListener {
            mAuth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    override fun onStop()
    {
        super.onStop()
        mAuth.removeAuthStateListener { mAuthStateListener }
    }

    override fun onStart()
    {
        super.onStart()
        mAuth.addAuthStateListener { mAuthStateListener }
    }
}