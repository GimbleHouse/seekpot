package com.gimble.seekpot.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.gimble.seekpot.R
import com.gimble.seekpot.databinding.ActivityAccountBinding
import com.gimble.seekpot.feature.authentication.AuthenticationManager

class Account : AppCompatActivity() {
    private lateinit var binding : ActivityAccountBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setup()
    }

    private fun setup() {
        binding.button.setOnClickListener{
            //disabling admin
            val sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.putBoolean("admin", false)
            editor.apply()
            val am = AuthenticationManager()
            am.signOut(this, MainActivity.auth) {
                Toast.makeText(this,"signout successful",Toast.LENGTH_SHORT).show()
                val i = Intent(this,MainActivity::class.java)
                startActivity(i)
                finish()
            }
        }
    }
}