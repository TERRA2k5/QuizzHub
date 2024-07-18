package com.example.quizzhub

import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
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
    lateinit var bookmarkViewModel: BookmarkViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        auth = Firebase.auth


        drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)
        navView = findViewById(R.id.navView)
        val repository = BookmarkRepository(QuizDatabase.getDatabase(this))
        val viewModelFactory = BookmarkViewModelFactory(application , repository)
        bookmarkViewModel = ViewModelProvider(this, viewModelFactory)[BookmarkViewModel::class.java]

        if(auth.currentUser != null){
            val header: View = navView.getHeaderView(0)

            header.findViewById<TextView>(R.id.tvName).text = auth.currentUser?.displayName.toString()
        }

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
                    Toast.makeText(this, "Signed Out", Toast.LENGTH_SHORT).show()
                    bookmarkViewModel.deleteAll()
                    navView.menu.findItem(R.id.log).setTitle("LogIn")
                    navView.menu.findItem(R.id.log).setIcon(R.drawable.baseline_login_24)

                    val header: View = navView.getHeaderView(0)

                    header.findViewById<TextView>(R.id.tvName).text = " "
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