package com.example.euledit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.parse.ParseUser

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Check if user is still signed in
        if (ParseUser.getCurrentUser() != null) {
            goToMainActivity()
        }

        val loginButton = findViewById<Button>(R.id.login_button)
        val usernameEt = findViewById<EditText>(R.id.login_username_et)
        val passwordEt = findViewById<EditText>(R.id.login_password_et)

        loginButton.setOnClickListener {
            val username = usernameEt.text.toString()
            val password = passwordEt.text.toString()
            loginUser(username, password)
        }
    }

    // Send credentials to parse to attempt authentication
    private fun loginUser(username: String, password: String) {
        ParseUser.logInInBackground(username, password, ({ user, e ->
            if (user != null) {
                Log.i("LoginActivity", "Successfully logged in user")
                goToMainActivity()
            } else {
                e.printStackTrace()
                Toast.makeText(this, "Error logging in", Toast.LENGTH_SHORT).show()
            }
        }))
    }

    // Navigate to Main activity
    private fun goToMainActivity(){
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        startActivity(intent)
        finish() // So that we can't come back to the log in page by hitting back
    }
}