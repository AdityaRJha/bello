package com.example.bello.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.bello.R
import com.example.bello.databinding.ActivityHomeBinding
import com.example.bello.fragments.HomeAdapter
import com.example.bello.fragments.NavigationFragment
import com.example.bello.util.DATA_USERS
import com.example.bello.util.User
import com.example.bello.util.loadURL
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    private val mAuth = FirebaseAuth.getInstance()
    private var currentUser = FirebaseAuth.getInstance().currentUser?.uid
    private lateinit var user: User
    private val clickListener: View.OnClickListener = View.OnClickListener {    view ->
        when(view.id){
            R.id.cardViewProfileImage -> openNavDialog()
        }
    }
    private val firebaseDB = FirebaseFirestore.getInstance()
    private var imageUrl: String?= null
    private var profilePIC: ImageView?=null
    
    private fun openNavDialog() {
        val navDialog = NavigationFragment()
        navDialog.show(supportFragmentManager,"customDialog")
    }

    private val mAuthStateListener = FirebaseAuth.AuthStateListener {
        currentUser = mAuth.currentUser?.uid
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //removing the Action Bar
        supportActionBar?.hide()

        val command = intent.getCharExtra("command", '0')

        if(command == '1'){
            openNavDialog()
        }


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

        binding.cardViewProfileImage.setOnClickListener(clickListener)
        profilePIC = binding.cardViewProfileImage.findViewById(R.id.profile_picture)
        populateInfo()
        
    }

    private fun populateInfo() {
        firebaseDB.collection(DATA_USERS).document(currentUser!!).get()
            .addOnSuccessListener { documentSnapshot ->
                user = documentSnapshot.toObject(User::class.java)!!
                imageUrl = user.imageUrl
                imageUrl?.let{
                    profilePIC?.loadURL(user.imageUrl, R.drawable.default_user)
                }
            }.addOnFailureListener { e ->
                Log.e("ChangeProfilePicture", e.printStackTrace().toString())
                finish()
            }
    }


    override fun onResume(){
        super.onResume()
        currentUser = FirebaseAuth.getInstance().currentUser?.uid
        if(currentUser == null){
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