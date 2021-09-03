package com.example.bello.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.bello.R
import com.example.bello.databinding.ActivityHomeBinding
import com.example.bello.fragments.HomeAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    private val mAuth = FirebaseAuth.getInstance()

    private val mAuthStateListener = FirebaseAuth.AuthStateListener {
        val user = mAuth.currentUser?.uid
        user?.let {
            startActivity(Intent (this, LoginActivity::class.java))
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //removing the Action Bar
        supportActionBar?.hide()

        val viewPager = binding.viewPager
        val tabLayout = binding.tabLayout

        val homeAdapter = HomeAdapter(this)
        viewPager.adapter = homeAdapter

        TabLayoutMediator(tabLayout, viewPager)
            { tab, position -> when(position){
                0 -> {
                    tab.setIcon(R.drawable.chat_feather)
                }
                1 -> {
                    tab.setIcon(R.drawable.status_icon)
                }
                2 -> {
                    tab.setIcon(R.drawable.broadcast_icon)
                }
            }
        }.attach()

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