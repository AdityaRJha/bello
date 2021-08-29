package com.example.bello

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.bello.databinding.ActivityForgotPasswordBinding
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgotPasswordBinding

    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()

        binding.progressBar.visibility = View.GONE
        setTextChangeListener(binding.emailFET, binding.emailFTIL)

        binding.buttonSubmit.setOnClickListener{
            submit()
        }
        binding.loginFTV.setOnClickListener{
            goToLogin()
        }
        binding.signUpFTV.setOnClickListener {
            goToSignUp()
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


    private fun submit()
    {
        var proceed = true
        if(binding.emailFET.text.isNullOrEmpty()){
            binding.emailFTIL.error = "Email is required"
            binding.emailFTIL.isErrorEnabled = true
            proceed = false
        }
        if (proceed){
            findViewById<ProgressBar>(R.id.progressBar).visibility = View.VISIBLE
            mAuth!!.sendPasswordResetEmail(binding.emailFET.text.toString().trim()).
                addOnCompleteListener{ task ->
                if (task.isSuccessful) {
                    Toast.makeText(this@ForgotPasswordActivity, "Check email to reset your password!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this@ForgotPasswordActivity, "Fail to send reset password email!", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener{e: Exception ->
                e.printStackTrace()
                findViewById<ProgressBar>(R.id.progressBar).visibility = View.GONE
            }

        }
    }

    private fun goToSignUp()
    {
        startActivity(Intent(this, SignUpActivity::class.java))
        finish()
    }

    private fun goToLogin()
    {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}
