package com.example.euledit

import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.ImageViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.euledit.fragments.DashboardFragment
import com.example.euledit.fragments.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import java.io.File
import java.io.FileOutputStream


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val constraintLayout = findViewById<ConstraintLayout>(R.id.constraint_layout)
        val fragmentManager: FragmentManager = supportFragmentManager

        // Display dashboard fragment by default
        fragmentManager.beginTransaction().replace(R.id.fragment_container, DashboardFragment()).commit()

        // Bottom navigation view listener for switching between fragments
        findViewById<BottomNavigationView>(R.id.bottom_nav_bar).setOnItemSelectedListener {
                item ->

            var currentFragment: Fragment? = null
            when (item.itemId) {
                R.id.bottom_nav_dashboard -> {
                    currentFragment = DashboardFragment()
                }
                R.id.bottom_nav_explore -> {
                    //currentFragment = ExploreFragment()
                    Snackbar
                        .make(constraintLayout, "Coming soon", Snackbar.LENGTH_SHORT)
                        .setAction("Dismiss") {
                            Log.d("Snackbar", "Dismissed snackbar")
                        }
                        .show()
                }
                R.id.bottom_nav_friends -> {
                    //currentFragment = FriendsFragment()
                    Snackbar
                        .make(constraintLayout, "Coming soon", Snackbar.LENGTH_SHORT)
                        .setAction("Dismiss") {
                            Log.d("Snackbar", "Dismissed snackbar")
                        }
                        .show()
                }
                R.id.bottom_nav_profile -> {
                    currentFragment = ProfileFragment()
                }
            }
            if (currentFragment != null) {
                fragmentManager.beginTransaction().replace(R.id.fragment_container, currentFragment).commit()
            }
            // This true signifies we handled the user interaction
            true
        }
    }

}