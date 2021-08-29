package com.example.bello

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bello.databinding.ActivityConfirmPasswordBinding
import com.example.bello.util.DATA_USERS
import com.example.bello.util.User
import com.google.android.gms.tasks.Task
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ConfirmPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConfirmPasswordBinding
    private val mAuth = FirebaseAuth.getInstance()
    private val firebaseDB = FirebaseFirestore.getInstance()
    private val firebaseAuthListener = FirebaseAuth.AuthStateListener {
        val user = mAuth.currentUser?.uid
        user?.let {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfirmPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val pass = intent.getStringExtra("pass").toString()
        val username = intent.getStringExtra("user").toString()



        //removing the Action Bar
        supportActionBar?.hide()
        binding.progressBar.visibility = View.GONE

        setTextChangeListener(
            binding.emailET,
            binding.emailTIL
        )
        setTextChangeListener(
            binding.confirmPasswordET,
            binding.confirmPasswordTIL
        )

        binding.buttonSignUp.setOnClickListener {
            signup(pass, username)
        }
    }

    private fun setTextChangeListener(et: EditText, til: TextInputLayout) {
        et.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                til.isErrorEnabled = false
            }

        })
    }

    private fun signup(pass: String, username: String){
        var proceed = true
        val conPasswordET = binding.confirmPasswordET
        val conPasswordTIL = binding.confirmPasswordTIL
        val emailET = binding.emailET
        val emailTIL = binding.emailTIL
        println()
        println(pass)
        if (emailET.text.isNullOrEmpty()){
            emailTIL.error = "email is required"
            emailTIL.isErrorEnabled = true
            proceed = false
        }
        if(conPasswordET.text.toString() != pass){
            conPasswordTIL.error ="password doesn't match"
            conPasswordTIL.isErrorEnabled = true
            proceed = false
        }
        println(pass)
        println(conPasswordET.text.toString())
        if(proceed){
            binding.progressBar.visibility = View.VISIBLE
            mAuth.createUserWithEmailAndPassword(emailET.text.toString().trim(), pass)
                .addOnCompleteListener {    task: Task<AuthResult> ->
                    if (task.isSuccessful) {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(this@ConfirmPasswordActivity, "SignUp error: ${task.exception?.localizedMessage}", Toast.LENGTH_SHORT).show()
                    } else {
                        mAuth.currentUser!!.sendEmailVerification().addOnCompleteListener {
                                task -> if(task.isSuccessful) {
                            Log.d(TAG,"Email Sent, Verify!!")
                        }
                    }
                    Toast.makeText(this@ConfirmPasswordActivity, "Please check your email for verification", Toast.LENGTH_SHORT).show()
                    val email = emailET.text.toString()
                    val user = User(email, username, "", arrayListOf(), arrayListOf())
                    firebaseDB.collection(DATA_USERS).document(mAuth.uid!!).set(user)
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                    }
                binding.progressBar.visibility = View.GONE
            }.addOnFailureListener {    e: Exception ->
                    e.printStackTrace()
                    binding.progressBar.visibility = View.GONE
                }
        }
    }

    override fun onStart() {
        super.onStart()
        mAuth.addAuthStateListener { firebaseAuthListener }
    }

    override fun onStop() {
        super.onStop()
        mAuth.removeAuthStateListener { firebaseAuthListener }
    }

}