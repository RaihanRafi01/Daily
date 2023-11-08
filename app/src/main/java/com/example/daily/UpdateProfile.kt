package com.example.daily

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.daily.databinding.ActivityHomeBinding
import com.example.daily.databinding.ActivityUpdateProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class UpdateProfile : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateProfileBinding
    private lateinit var storageRef : FirebaseStorage
    private lateinit var firebaseFirestore : FirebaseFirestore
    private lateinit var imageUri : Uri
    private lateinit var pgbar : ProgressBar
    var UID = FirebaseAuth.getInstance().uid.toString()
    val databaseRef = FirebaseDatabase.getInstance().getReference("Daily")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        pgbar = binding.progressBarUpdate
        pgbar.visibility = View.INVISIBLE
        initVars()
        registerClickEvents()
    }

    private fun uploadText() {
        var upName = binding.updName.text.toString()
        var upEmail = binding.updEmail.text.toString()
        var upNumber = binding.updNumber.text.toString()
        if (upEmail.isNotEmpty() && upName.isNotEmpty() && upNumber.isNotEmpty()){
            var model = UserLoginModel(upName,upNumber,upEmail)
            databaseRef.child("UserInfo").child(UID).setValue(model)
            Toast.makeText(this,"Updated",Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(this,"Not Updated",Toast.LENGTH_SHORT).show()
        }
    }

    private fun registerClickEvents() {
        binding.btnImgUpload.setOnClickListener(View.OnClickListener {
            uploadText()
            uploadImg()
            val iHome = Intent(this, HomeActivity::class.java)
            startActivity(iHome)
        })
        binding.imgUp.setOnClickListener(View.OnClickListener {
            resultLauncher.launch("image/*")
        })
        binding.btnBackUptoHome.setOnClickListener {
            val iHome = Intent(this, HomeActivity::class.java)
            startActivity(iHome)
        }
    }

    private fun uploadImg() {
        pgbar.visibility = View.VISIBLE
        storageRef.getReference("images").child(System.currentTimeMillis().toString())
            .putFile(imageUri).addOnSuccessListener {task ->
                task.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
                    val mapImage = mapOf("url" to it.toString())
                    databaseRef.child("UserInfo").child(UID).child("Images").setValue(mapImage).addOnSuccessListener {
                        Toast.makeText(this,"Picture Uploaded Please hold",Toast.LENGTH_SHORT).show()
                    }
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

    private fun initVars() {
        storageRef = FirebaseStorage.getInstance()
        firebaseFirestore = FirebaseFirestore.getInstance()
    }
}