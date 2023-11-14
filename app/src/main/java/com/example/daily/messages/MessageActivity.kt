package com.example.daily.messages

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.example.daily.LoginActivity
import com.example.daily.R
import com.example.daily.databinding.ActivityMessageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MessageActivity : AppCompatActivity() {
    companion object {
        var currentUser : UserModel? = null
    }
    private lateinit var binding : ActivityMessageBinding
    private val UID = FirebaseAuth.getInstance().uid
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar!!.title = "Messenger"
        fetchCurrentUser()
    }

    private fun fetchCurrentUser() {
        val ref = FirebaseDatabase.getInstance().getReference("Daily/UserInfo/$UID")
        ref.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
               currentUser = snapshot.getValue(UserModel::class.java)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.msg_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_logout -> {
                val iLogin = Intent(this@MessageActivity, LoginActivity::class.java)
                getSharedPreferences("login", MODE_PRIVATE).edit().putBoolean("loginFlag", false).apply()
                startActivity(iLogin)
            }
            R.id.action_new -> {
                val iNewMsg = Intent(this@MessageActivity, NewMessageActivity::class.java)
                startActivity(iNewMsg)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}