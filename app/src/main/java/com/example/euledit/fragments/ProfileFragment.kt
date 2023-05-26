package com.example.euledit.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.euledit.LoginActivity
import com.example.euledit.MainActivity
import com.example.euledit.R
import com.parse.ParseUser


class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val logoutButton = view.findViewById<Button>(R.id.logout_button)

        logoutButton.setOnClickListener {
            ParseUser.logOut()
            goToLoginActivity()
        }
    }

    private fun goToLoginActivity(){
        val intent = Intent(requireActivity(), LoginActivity::class.java)
        startActivity(intent)
        activity?.finish() // So that we can't come back to the log in page by hitting back
    }
}