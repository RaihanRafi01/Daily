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
import com.example.daily.databinding.ActivityLoginBinding
import com.example.daily.databinding.ActivityRegistrationBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
private lateinit var binding : ActivityLoginBinding
private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        firebaseAuth = FirebaseAuth.getInstance()

        binding.btnLogin.setOnClickListener(View.OnClickListener {
            val ihome = Intent(this@LoginActivity, HomeActivity::class.java)

            val editor = getSharedPreferences("login", MODE_PRIVATE).edit()
            // login Condition

            val edtEmail = binding.edtName.text.toString()
            val edtPassword =  binding.edtPassword.text.toString()
            if(edtEmail.isNotEmpty() && edtPassword.isNotEmpty()){
                firebaseAuth.signInWithEmailAndPassword(edtEmail,edtPassword).addOnCompleteListener(this){ task ->
                    if (task.isSuccessful){
                        Toast.makeText(this@LoginActivity,"Please hold for a while",Toast.LENGTH_SHORT).show()
                        editor.putBoolean("loginFlag", true).apply()
                        startActivity(ihome)
                        finish()
                    }else{
                        Toast.makeText(this@LoginActivity,"Wrong Name and Password",Toast.LENGTH_SHORT).show()
                    }
                }
            }else{
                Toast.makeText(this@LoginActivity,"Please Provide Name and Password",Toast.LENGTH_SHORT).show()
            }
        })
        binding.btnReg.setOnClickListener {
            val iReg = Intent(this@LoginActivity, RegistrationActivity::class.java)
            startActivity(iReg)
        }
    }
}