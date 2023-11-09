package com.example.daily

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.daily.databinding.ActivityHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class HomeActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityHomeBinding
    private var dbref =  Firebase.database.getReference("Daily")
    private val UID = FirebaseAuth.getInstance().uid.toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)



        setSupportActionBar(binding.appBarHome.toolbar)

        binding.appBarHome.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navViewHome
        val navController = findNavController(R.id.nav_host_fragment_content_drawer_example)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow , R.id.nav_todo
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        /*val headerView = navView.getHeaderView(0)
        headerView.findViewById<TextView>(R.id.textView)*/
        val header = binding.navViewHome.getHeaderView(0)
        val name = header.findViewById<TextView>(R.id.txtCurrentUserName)
        val email = header.findViewById<TextView>(R.id.txtCurrentEmail)
        val pic = header.findViewById<ImageView>(R.id.imgUser)

        pic.setImageResource(R.drawable.batmanlogo)
        dbref.child("UserInfo").child(UID).child("Images").get().addOnSuccessListener {
            val url = it.child("url").value.toString()
            Glide.with(this).load(url).into(pic)
        }

        pic.setOnClickListener(View.OnClickListener {
            Toast.makeText(this,"yeeeeeeeeeeeeeeeee hold",Toast.LENGTH_SHORT).show()
            val iUpdateProfile = Intent(this@HomeActivity, UpdateProfile::class.java)
            startActivity(iUpdateProfile)
        })


        //dbref = Firebase.database.getReference("Daily")
        dbref.child("UserInfo").child(UID).get().addOnSuccessListener {
            val currentName = it.child("name").value.toString()
            val currentEmail = it.child("email").value.toString()
            Log.e("NAME ",currentName)
            name.setText("Welcome "+currentName)
            email.setText(currentEmail)
        }


    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.drawer_example, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_drawer_example)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }




}