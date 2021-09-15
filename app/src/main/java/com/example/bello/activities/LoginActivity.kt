package com.example.bello.activities

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bello.databinding.ActivityLoginBinding
import com.google.android.gms.tasks.Task
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firebaseAuthListener = FirebaseAuth.AuthStateListener {
        val user = firebaseAuth.currentUser?.uid
        if(user != null){
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //removing the Action Bar
        supportActionBar?.hide()

        binding.progressBar.visibility = View.GONE
        setTextChangeListener(binding.emailET, binding.emailTIL)
        setTextChangeListener(binding.passwordET, binding.passwordTIL)

        binding.forgetPasswordTV.setOnClickListener {
            goToForgotPassword()
        }
        binding.signUpTV.setOnClickListener {
            goToSignUp()
        }
        binding.buttonLogin.setOnClickListener {
            onLogin()
        }
    }

    private fun setTextChangeListener(et: EditText, til: TextInputLayout) {
        et.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                til.isErrorEnabled = false
            }

        })
    }



    private fun onLogin() {
        val emailET: EditText = binding.emailET
        val emailTIL: TextInputLayout = binding.emailTIL
        val passwordET: EditText = binding.passwordET
        val passwordTIL: TextInputLayout = binding.passwordTIL
        var proceed = true
        if(emailET.text.isNullOrEmpty()){
            emailTIL.error = "Email is required"
            emailTIL.isErrorEnabled = true
            proceed = false
        }
        if(passwordET.text.isNullOrEmpty()){
            passwordTIL.error ="Password is required"
            passwordTIL.isErrorEnabled = true
            proceed = false
        }
        if(proceed){
            binding.progressBar.visibility = View.VISIBLE
            firebaseAuth.signInWithEmailAndPassword(emailET.text.toString(), passwordET.text.toString())
                .addOnCompleteListener { task: Task<AuthResult> ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Successfully Logged in", Toast.LENGTH_LONG).show()
                        startActivity(Intent(this, HomeActivity::class.java))
                        finish()
                    }
                    else{
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(this@LoginActivity, "Login error: Either the email or password is wrong.", Toast.LENGTH_SHORT).show()
                    }
                }.addOnFailureListener{e: Exception ->
                    e.printStackTrace()
                    binding.progressBar.visibility = View.GONE
                }

        }
    }

    override fun onStart() {
        super.onStart()
        firebaseAuth.addAuthStateListener { firebaseAuthListener }
    }

    override fun onStop() {
        super.onStop()
        firebaseAuth.removeAuthStateListener { firebaseAuthListener }
    }

    private fun goToSignUp() {
        startActivity(Intent(this, SignUpActivity::class.java))
        finish()
    }

    private fun goToForgotPassword() {
        startActivity(Intent(this, ForgotPasswordActivity::class.java))
        Toast.makeText(this@LoginActivity, "Don't worry your password would be restored.", Toast.LENGTH_SHORT).show()
        finish()
    }
}