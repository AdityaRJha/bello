package com.example.bello.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.bello.R
import com.example.bello.activities.AccountSettingsActivity
import com.example.bello.activities.ChangeProfileActivity
import com.example.bello.activities.LoginActivity
import com.example.bello.databinding.NavigationFragmentLayoutBinding
import com.google.firebase.auth.FirebaseAuth

class NavigationFragment: DialogFragment(R.layout.navigation_fragment_layout) {

    private val mAuth = FirebaseAuth.getInstance()
    private var _binding: NavigationFragmentLayoutBinding ?= null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = NavigationFragmentLayoutBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navView = binding.navView

        navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.profile -> {
                    startChangeProfileActivity()
                    true
                }

                R.id.account -> {
                    startAccountSettingsActivity()
                    true
                }

                R.id.logout -> {
                    mAuth.signOut()
                    startLoginActivity()
                    true
                }

                R.id.back -> {
                    dismiss()
                    true
                }
                else -> false
            }
        }
    }

    private fun startChangeProfileActivity() {
        requireActivity().run {
            startActivity(Intent(this, ChangeProfileActivity::class.java))
            finish()
        }
    }

    private fun startAccountSettingsActivity() {
        requireActivity().run {
            startActivity(Intent(this, AccountSettingsActivity::class.java))
            finish()
        }
    }

    private fun startLoginActivity() {
        requireActivity().run {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}