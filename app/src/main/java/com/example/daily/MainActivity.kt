package com.example.daily

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toolbar
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
        var drawer = binding.mainDrawer
        firebaseAuth = FirebaseAuth.getInstance()
        var uid = firebaseAuth.currentUser?.uid.toString()
        Log.e("UID",uid)

        toggle = ActionBarDrawerToggle(this@MainActivity,drawer,R.string.open_nav,R.string.close_nav)
        drawer.addDrawerListener(toggle)
        toggle.syncState()
        actionBar?.setDisplayHomeAsUpEnabled(true)



       /* binding.btnLogout.setOnClickListener {
            val iLogin = Intent(this@MainActivity, LoginActivity::class.java)
            getSharedPreferences("login", MODE_PRIVATE).edit().putBoolean("loginFlag", false).apply()
            startActivity(iLogin)

        }*/

    }
}
