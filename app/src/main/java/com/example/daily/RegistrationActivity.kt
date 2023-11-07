package com.example.daily

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.daily.databinding.ActivityRegistrationBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class RegistrationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrationBinding
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()




        var dbRef : DatabaseReference = FirebaseDatabase.getInstance().getReference("Daily")



        binding.btnRegSubmit.setOnClickListener {
            /// initial values
            var name = binding.regName.text.toString()
            var number = binding.regNumber.text.toString()
            val email = binding.regEmail.text.toString()
            val pass = binding.regPass.text.toString()


            if (email.isNotEmpty() && pass.isNotEmpty() && name.isNotEmpty() && number.isNotEmpty()) {
                firebaseAuth.createUserWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(this){ task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "SignUp Successful", Toast.LENGTH_SHORT).show()
                        var UID = firebaseAuth.currentUser?.uid.toString()
                        var model = UserLoginModel(name,number,email,pass)

                        dbRef.child("Registration").child(UID).setValue(model)

                        val iLogin = Intent(this, LoginActivity::class.java)
                        startActivity(iLogin)
                        finish()
                    } else {
                        Toast.makeText(this, "SignUp Unsuccessful", Toast.LENGTH_SHORT).show()
                    }

                }
            } else {
                Toast.makeText(this, "Please Provide Email and Password", Toast.LENGTH_SHORT).show()
            }


        }




        /*var btnReg : Button = findViewById(R.id.btnRegSubmit)
        btnReg.setOnClickListener {
            var email : EditText = findViewById(R.id.regEmail)
            var pass : EditText = findViewById(R.id.regPass)
            var userRef : DatabaseReference = FirebaseDatabase.getInstance().getReference("Daily")
            //Log.e("VALUES","name : "+name.text+ " pass : "+pass.toString())
           // var key : String = userRef.push().key.toString()
            //var model = UserLoginModel(name.text.toString(),pass.text.toString())
           // userRef.child("LoginInfo").child(name.text.toString()).setValue(model)*/

        /*DatabaseReference contactRef = FirebaseDatabase.getInstance().getReference("contacts");
        String contactId = contactRef.push().getKey();
        ContactModel contactModel = new ContactModel("Rafi","122333");
        contactRef.child(contactId).setValue(contactModel)*/
    }

}

