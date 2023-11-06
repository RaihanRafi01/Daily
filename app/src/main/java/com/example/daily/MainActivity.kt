package com.example.daily

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import com.example.daily.databinding.ActivityLoginBinding
import com.example.daily.databinding.MainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    private lateinit var binding : MainBinding
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()
        var uid = firebaseAuth.currentUser?.uid.toString()
        Log.e("UID",uid)
    }
}
