package com.example.daily

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val iHome = Intent(this@SplashActivity, MainActivity::class.java)
        val ilogin = Intent(this@SplashActivity, LoginActivity::class.java)
        Handler().postDelayed({
            /*val check = getSharedPreferences("login", MODE_PRIVATE).getBoolean("loginFlag", false)
            if (check) {
                startActivity(iHome)
                finish()
            } else {
                startActivity(ilogin)
                finish()
            }*/
            startActivity(iHome)
            finish()
        }, 3000)
    }
}