package com.example.bello

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firebaseAuthListener = FirebaseAuth.AuthStateListener {
        val user = firebaseAuth.currentUser?.uid
        user?.let {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
    }
    private val emailET = findViewById<EditText>(R.id.emailET)
    private val emailTIL = findViewById<TextInputLayout>(R.id.emailTIL)
    private val passwordET = findViewById<EditText>(R.id.passwordET)
    private val passwordTIL = findViewById<TextInputLayout>(R.id.passwordTIL)
    private val loginProgressLayout = findViewById<ProgressBar>(R.id.progressBar)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //removing the Action Bar
        supportActionBar?.hide()


        setTextChangeListener(emailET, emailTIL)
        setTextChangeListener(passwordET, passwordTIL)
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



    fun onLogin(v: View) {
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
            loginProgressLayout.visibility = View.VISIBLE
            firebaseAuth.signInWithEmailAndPassword(emailET.text.toString(), passwordET.text.toString())
                .addOnCompleteListener { task: Task<AuthResult> ->
                    if (!task.isSuccessful) {
                        loginProgressLayout.visibility = View.GONE
                        Toast.makeText(this@LoginActivity, "Login error: Either the email or password is wrong.", Toast.LENGTH_SHORT).show()
                    }else{

                        Toast.makeText(this, "Successfully Logged in", Toast.LENGTH_LONG).show()
                        val intent = Intent(this, HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }.addOnFailureListener{e: Exception ->
                    e.printStackTrace()
                    loginProgressLayout.visibility = View.GONE
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

    fun goToSignUp(v: View) {
        startActivity(Intent(this, SignUpActivity::class.java))
        finish()
    }

    fun goToForgotPassword(v: View) {
        startActivity(Intent(this, ForgotPasswordActivity::class.java))
        Toast.makeText(this@LoginActivity, "Don't worry your password would be restored.", Toast.LENGTH_SHORT).show()
        finish()
    }
}