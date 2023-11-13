package com.example.daily

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.ContactsContract.CommonDataKinds.Email
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide.init
import com.example.daily.databinding.ActivityRegistrationBinding
import com.google.android.material.transition.Hold
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

class RegistrationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrationBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var pgbar : ProgressBar
    private lateinit var storageRef : FirebaseStorage
    private lateinit var firebaseFirestore : FirebaseFirestore
    private lateinit var imageUri : Uri
    private var UID = FirebaseAuth.getInstance().uid.toString()
    private val databaseRef = FirebaseDatabase.getInstance().getReference("Daily")
    private lateinit var name: String
    private lateinit var number : String
    private lateinit var email : String
    private lateinit var pass : String
    private lateinit var mapImage : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()
        pgbar = binding.progressBarUpdate
        pgbar.visibility = View.INVISIBLE
        storageRef = FirebaseStorage.getInstance()
        firebaseFirestore = FirebaseFirestore.getInstance()
        initval()


        binding.imgUp.setOnClickListener(View.OnClickListener {
            resultLauncher.launch("image/*")
        })

        binding.btnRegSubmit.setOnClickListener {

            initval()
            val allChecked = CheckAllFields()
            if (allChecked){
                if (email.isNotEmpty() && pass.isNotEmpty() && name.isNotEmpty() && number.isNotEmpty()) {
                    firebaseAuth.createUserWithEmailAndPassword(email, pass)
                        .addOnCompleteListener(this){ task ->
                            if (task.isSuccessful) {
                                Toast.makeText(this, "SignUp Successful", Toast.LENGTH_SHORT).show()
                                uploadImg()
                                UID = firebaseAuth.currentUser?.uid.toString()
                                Handler().postDelayed({
                                    val model = UserLoginModel(name,number,email,pass,mapImage,UID)
                                    databaseRef.child("UserInfo").child(UID).setValue(model)
                                    val iLogin = Intent(this, LoginActivity::class.java)
                                    startActivity(iLogin)
                                    finish()
                                }, 9000)
                                /* startActivity(iLogin)
                                 finish()*/
                            } else {
                                Toast.makeText(this, "SignUp Unsuccessful", Toast.LENGTH_SHORT).show()
                            }

                        }
                } else {
                    Toast.makeText(this, "Please Provide Email and Password", Toast.LENGTH_SHORT).show()
                }
            }
            else
            {
                Toast.makeText(this, "Please Provide info", Toast.LENGTH_SHORT).show()
            }



        }

    }



    private fun uploadImg() {
        pgbar.visibility = View.VISIBLE
        storageRef.getReference("images").child(System.currentTimeMillis().toString())
            .putFile(imageUri).addOnSuccessListener {task ->
                task.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
                    mapImage =  it.toString()
                   /* databaseRef.child("UserInfo").child(UID).setValue(mapImage).addOnSuccessListener {
                        Toast.makeText(this,"Picture Uploaded Please hold",Toast.LENGTH_SHORT).show()
                    }*/
                }
            }
    }

    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()){
        if (it != null) {
            imageUri = it
        }
        binding.imgUp.setImageURI(it)
    }

    private fun initval() {
        name = binding.regName.text.toString()
        number = binding.regNumber.text.toString()
        email = binding.regEmail.text.toString()
        pass = binding.regPass.text.toString()
    }

    private fun CheckAllFields(): Boolean {
        if (name.length == 0) {
            binding.regName.error = "Name is required"
            return false
        }
        if (number.length == 0) {
            binding.regNumber.error = "Number is required"
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.regEmail.error = "Email is required"
            return false
        }
        else if (pass.length < 6) {
            binding.regPass.error = "Password must be minimum 6 characters"
            return false
        }

        // after all validation return true.
        return true
    }



}


