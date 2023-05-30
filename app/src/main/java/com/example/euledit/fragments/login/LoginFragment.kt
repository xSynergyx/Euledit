package com.example.euledit.fragments.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.euledit.MainActivity
import com.example.euledit.R
import com.parse.ParseUser


class LoginFragment : Fragment() {

/*
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }
*/

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Check if user is still signed in
        if (ParseUser.getCurrentUser() != null) {
            goToMainActivity()
        }

        val loginButton = view.findViewById<Button>(R.id.login_button)
        val registerButton: Button = view.findViewById(R.id.register_button)
        val usernameEt = view.findViewById<EditText>(R.id.login_username_et)
        val passwordEt = view.findViewById<EditText>(R.id.login_password_et)

        loginButton.setOnClickListener {
            val username = usernameEt.text.toString()
            val password = passwordEt.text.toString()
            loginUser(username, password)
        }

        // Navigate to register fragment
        registerButton.setOnClickListener {
            parentFragmentManager.beginTransaction().replace(R.id.login_container, RegisterFragment()).commit()
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
                Toast.makeText(activity, "Error logging in", Toast.LENGTH_SHORT).show()
            }
        }))
    }

    // Navigate to Main activity
    private fun goToMainActivity(){
        val intent = Intent(activity, MainActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }
}