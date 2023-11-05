package com.example.daily

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val btnLogin: Button = findViewById<Button>(R.id.btnLogin)

        btnLogin.setOnClickListener(View.OnClickListener {
            val ihome = Intent(this@LoginActivity, MainActivity::class.java)
            val pref = getSharedPreferences("login", MODE_PRIVATE)
            val editor = pref.edit()
            // login Condition
            val edtName : EditText = findViewById(R.id.edtName)
            val edtPassword : EditText = findViewById(R.id.edtPassword)
            if (edtName.text.toString().equals("admin")&&edtPassword.text.toString().equals("123")){
                editor.putBoolean("flag", true)
                editor.apply()
                startActivity(ihome)
                finish()
            }else{
                Toast.makeText(this@LoginActivity,"Wrong Name and Password",Toast.LENGTH_LONG).show()
            }
            //Log.e("value","Name : "+edtName.text +" , Pass : "+edtPassword.text)

        })
    }
}