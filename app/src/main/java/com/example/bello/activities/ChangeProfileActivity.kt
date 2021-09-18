package com.example.bello.activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.bello.R
import com.example.bello.databinding.ActivityChangeProfileBinding
import com.example.bello.util.*
import com.github.drjacky.imagepicker.ImagePicker
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class ChangeProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChangeProfileBinding
    private var profilePIC: ImageView?=null
    private val fbStorage = FirebaseStorage.getInstance().reference
    private val firebaseDB = FirebaseFirestore.getInstance()
    private val userId = FirebaseAuth.getInstance().currentUser?.uid
    private var imageUrl: String?= null
    private var uri: Uri?= null
    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            uri = it.data?.data!!
            // Use the uri to load the image
            profilePIC?.setImageURI(uri)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangeProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()


        if(userId == null){
            finish()
        }

        populateInfo()

        binding.bio.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                binding.textInputCounter.text = (150 - binding.bio.text.toString().length).toString()
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                binding.textInputCounter.text = (150 - binding.bio.text.toString().length).toString()
            }

            override fun afterTextChanged(s: Editable) {
                binding.textInputCounter.text = (150 - binding.bio.text.toString().length).toString()
            }
        })


        binding.backFAB.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.putExtra("command", '1')
            startActivity(intent)
            finish()
        }

        profilePIC = binding.profilePIC

        binding.takePicFAB.setOnClickListener {
            Log.d("ChangeProfileActivity", "Going to select the photo!!")

            getPicture()
        }

        binding.buttonApply.setOnClickListener {
            onApply()
        }

        setTextChangeListener(binding.userNameET, binding.userNameTIL)
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

    private fun getPicture() {
        getTheImage()
        profilePIC?.setImageURI(uri)
    }

    private fun getTheImage() {
        ImagePicker.with(this)
            .crop()
            .cropOval()
            .maxResultSize(1080,1080)
            .cropFreeStyle()
            .createIntentFromDialog { launcher.launch(it) }
    }

    private fun storeImage(uriProfile: Uri) {
        uriProfile.let {
            Log.e("ChangeProfileActivity", "Uploading...")
            Toast.makeText(this, "Uploading...", Toast.LENGTH_SHORT).show()

            binding.progressBar3.visibility = View.VISIBLE

            val filePath = fbStorage.child(DATA_IMAGES).child(userId!!)
            filePath.putFile(uriProfile)
                .addOnSuccessListener {
                    filePath.downloadUrl
                        .addOnSuccessListener { uri ->
                            val url = uri.toString()
                            firebaseDB.collection(DATA_USERS).document(userId).update(
                                DATA_USER_IMAGE_URL, url)
                                .addOnSuccessListener {
                                    imageUrl = url
                                    binding.profilePIC.loadURL(imageUrl, R.drawable.default_user)
                                    Log.d("Store Image", "Line has passed the pics is uploaded")
                                }
                            binding.progressBar3.visibility = View.GONE
                        }
                        .addOnFailureListener {
                            failedUploading()
                        }
                }
                .addOnFailureListener{
                    failedUploading()
                }
        }
    }

    private fun failedUploading() {
        Log.e("ChangeProfileActivity(storeImage)", "Image uploading failed.")
        Toast.makeText(this, "Uploading failed!! Try again later.", Toast.LENGTH_SHORT).show()
        binding.progressBar3.visibility = View.GONE
    }

    private fun onApply() {
        updateChangeProfileActivity()
        populateInfo()
    }

    private fun updateChangeProfileActivity() {
        binding.progressBar3.visibility = View.VISIBLE
        uri?.let { storeImage(it) }
        val userName = binding.userNameET.text.toString()

        val map = HashMap<String, Any>()
        map[DATA_USER_USERNAME] = userName
        map[DATA_USER_BIO] = binding.bio.text.toString()

        if (userId != null) {
            firebaseDB.collection(DATA_USERS).document(userId).update(map)
                .addOnSuccessListener {
                    Toast.makeText(this, "Update Successful", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener { e ->
                    Log.e("ChangeProfilePicture", e.printStackTrace().toString())
                    Toast.makeText(this, "Try Again!! Not Successful", Toast.LENGTH_SHORT).show()
                    binding.progressBar3.visibility = View.GONE
                }
        }

    }




    private fun populateInfo() {
        binding.progressBar3.visibility = View.VISIBLE
        firebaseDB.collection(DATA_USERS).document(userId!!).get()
            .addOnSuccessListener { documentSnapshot ->
                val user = documentSnapshot.toObject(User::class.java)
                binding.userNameET.setText(user?.username, TextView.BufferType.EDITABLE)
                binding.bio.setText(user?.bio.toString())
                imageUrl = user?.imageUrl
                imageUrl?.let{
                    profilePIC?.loadURL(user?.imageUrl, R.drawable.default_user)
                }
                binding.progressBar3.visibility = View.GONE
            }.addOnFailureListener { e ->
                Log.e("ChangeProfilePicture", e.printStackTrace().toString())
                finish()
            }
    }
}


