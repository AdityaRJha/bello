package com.example.bello.activities

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.bello.databinding.ActivitySignUpBinding
import com.google.android.material.textfield.TextInputLayout

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //removing the Action Bar
        supportActionBar?.hide()
        binding.progressBar.visibility = View.GONE

        setTextChangeListener(
            binding.usernameET,
            binding.usernameTIL
        )
        setTextChangeListener(
            binding.passwordET,
            binding.passwordTIL
        )

        binding.buttonNext.setOnClickListener {
            goToNext()
        }

        binding.loginTV.setOnClickListener{
            goToLogin()
        }

    }

    private fun goToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    private fun goToNext() {
        var proceed = true
        val passwordET = binding.passwordET
        val passwordTIL = binding.passwordTIL
        val usernameET = binding.usernameET
        val usernameTIL = binding.usernameTIL
        if (usernameET.text.isNullOrEmpty()){
            usernameTIL.error = "Username is required"
            usernameTIL.isErrorEnabled = true
            proceed = false
        }
        if(passwordET.text.isNullOrEmpty()){
            passwordTIL.error ="Password is required"
            passwordTIL.isErrorEnabled = true
            proceed = false
        }
        if(proceed){
            binding.progressBar.visibility = View.VISIBLE
            val i = Intent(this, ConfirmPasswordActivity::class.java)
            i.apply {
                    putExtra("pass", binding.passwordET.text.toString())
                    putExtra("user", binding.usernameET.text.toString())
                    }
            startActivity(i)
            finish()
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

}