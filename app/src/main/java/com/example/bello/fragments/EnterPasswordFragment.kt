package com.example.bello.fragments

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.bello.R
import com.example.bello.databinding.FragmentEnterPasswordBinding
import com.example.bello.util.DATA_USERS
import com.example.bello.util.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class EnterPasswordFragment : DialogFragment(R.layout.fragment_enter_password) {

    private var _binding: FragmentEnterPasswordBinding?=null
    private val binding get() = _binding!!
    private val firebaseAuth = FirebaseAuth.getInstance()
    private var currentUser = firebaseAuth.currentUser?.uid
    private val fbDB = FirebaseFirestore.getInstance()
    private lateinit var email: String

    private lateinit var result: Bundle

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
            _binding = FragmentEnterPasswordBinding.inflate(inflater, container, false)

            return binding.root
        }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var user: User

        fbDB.collection(DATA_USERS).document(currentUser!!).get()
            .addOnSuccessListener { documentSnapshot ->
                user = documentSnapshot.toObject(User::class.java)!!
                email = user.email.toString()
                binding.emailTV.text = email
            }


        binding.buttonCancel.setOnClickListener {
            onSelectingCancel()
        }
        binding.buttonOK.setOnClickListener {
            onSelectingOk()
        }
    }

    private fun onSelectingOk() {
        result.putString("result", "1")

        val password = binding.passwordEnterET.text.toString()

        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    currentUser = firebaseAuth.currentUser?.uid
                    parentFragmentManager.setFragmentResult("result", result)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(activity, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }

    }

    private fun onSelectingCancel() {
        result.putString("result", '0'.toString())
        parentFragmentManager.setFragmentResult("result", result)
    }


}
