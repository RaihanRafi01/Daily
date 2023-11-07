package com.example.daily

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import com.example.daily.databinding.MainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    private lateinit var binding : MainBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var toggle: ActionBarDrawerToggle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()
        var uid = firebaseAuth.currentUser?.uid.toString()
        Log.e("UID",uid)

        toggle = ActionBarDrawerToggle(this@MainActivity,binding.mainDrawer,R.string.open,R.string.close)
        binding.mainDrawer.addDrawerListener(toggle)
        toggle.syncState()

        binding.btnLogout.setOnClickListener {
            val iLogin = Intent(this@MainActivity, LoginActivity::class.java)
            getSharedPreferences("login", MODE_PRIVATE).edit().putBoolean("loginFlag", false).apply()
            startActivity(iLogin)

        }

        binding.mainDrawer.closeDrawers()
    }
}
