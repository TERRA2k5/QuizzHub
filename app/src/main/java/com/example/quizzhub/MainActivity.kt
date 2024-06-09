package com.example.quizzhub

import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.quizzhub.database.BookmarkRepository
import com.example.quizzhub.database.QuizDatabase
import com.example.quizzhub.model.BookmarkViewModel
import com.example.quizzhub.model.BookmarkViewModelFactory
import com.google.android.material.navigation.NavigationView
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener{

    private lateinit var drawerLayout: DrawerLayout
    lateinit var navView:NavigationView
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = Firebase.auth

        drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)
        navView = findViewById(R.id.navView)


        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        navView.setNavigationItemSelectedListener(this)
        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.open,
            R.string.close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
    }

    override fun onNavigationItemSelected(p0: MenuItem): Boolean {

        auth = Firebase.auth
        when(p0.itemId){

            R.id.log -> {
                if(auth.currentUser == null){
                    startActivity(Intent(this, SignActivity::class.java))
                }
                else{
                    auth.signOut()
                    navView.menu.findItem(R.id.log).setTitle("LogIn")
                    navView.menu.findItem(R.id.log).setIcon(R.drawable.baseline_login_24)
                }
                drawerLayout.closeDrawer(GravityCompat.START)
            }

            R.id.home -> {
                if(findNavController(R.id.fragmentContainerView).currentDestination?.id == R.id.bookmarkFragment) {
                    findNavController(R.id.fragmentContainerView).navigate(R.id.action_bookmarkFragment_to_homeFragment)
                    drawerLayout.closeDrawer(GravityCompat.START)
                }
            }

            R.id.bookMark -> {
                if(findNavController(R.id.fragmentContainerView).currentDestination?.id == R.id.homeFragment) {
                    findNavController(R.id.fragmentContainerView).navigate(R.id.action_homeFragment_to_bookmarkFragment)
                    drawerLayout.closeDrawer(GravityCompat.START)
                }
            }

        }
        return true
    }

    override fun onStart() {
        super.onStart()
        if(auth.currentUser != null){
            navView.menu.findItem(R.id.log).setTitle("LogOut")
            navView.menu.findItem(R.id.log).setIcon(R.drawable.baseline_logout_24)

            val repository = BookmarkRepository(QuizDatabase.getDatabase(this))
            val factory = BookmarkViewModelFactory(application , repository)
            val bookmarkViewModel: BookmarkViewModel by viewModels { factory!! }

            bookmarkViewModel.syncWithFirestore()

        }
    }
}