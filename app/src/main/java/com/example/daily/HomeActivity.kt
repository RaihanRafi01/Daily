package com.example.daily

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
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
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class HomeActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityHomeBinding
    private var dbref =  Firebase.database.getReference("Daily")
    private val UID = FirebaseAuth.getInstance().uid.toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val appbarHome = binding.appBarHome.toolbar
        setSupportActionBar(appbarHome)

        binding.appBarHome.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navViewHome
        val navController = findNavController(R.id.nav_host_fragment_content_drawer_example)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow , R.id.nav_todo , R.id.action_logout
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
        dbref.child("UserInfo").child(UID).get().addOnSuccessListener {
            val url = it.child("imgUrl").value.toString()
            Glide.with(this).load(url).into(pic)
        }

        pic.setOnClickListener(View.OnClickListener {
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
        menuInflater.inflate(R.menu.drawer_example, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.getItemId() == R.id.action_logout){
            val iLogin = Intent(this@HomeActivity, LoginActivity::class.java)
            getSharedPreferences("login", MODE_PRIVATE).edit().putBoolean("loginFlag", false).apply()
            startActivity(iLogin)
        }
        else if (item.getItemId() == R.id.action_settings){
            Toast.makeText(this, "Clicked Settings Icon..", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_drawer_example)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }




}