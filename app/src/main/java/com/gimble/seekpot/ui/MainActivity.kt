package com.gimble.seekpot.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.gimble.seekpot.R
import com.gimble.seekpot.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    companion object{
        lateinit var auth: FirebaseAuth
    }
    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth= FirebaseAuth.getInstance()
       setUp()
    }
    private fun setUp(){
        if (auth.currentUser == null) {
            jumperToAuthentication()
        }
        else{
            binding.username.text = extractNameFromEmail(auth.currentUser?.email.toString())
            binding.imageView.setOnClickListener {
                val i = Intent(this,Account::class.java)
                startActivity(i)
            }
            binding.foundbutton.setOnClickListener{
                val q = Intent(this,ItemFound::class.java)
                startActivity(q)
            }
            binding.lostbutton.setOnClickListener {
                val r = Intent(this,ItemLost::class.java)
                startActivity(r)
            }

        }
    }
    private fun jumperToAuthentication(){
        val i = Intent(this,Authentication::class.java)
        startActivity(i)
        finish()
    }
    private fun extractNameFromEmail(email: String): String {
        val parts = email.split("@")
        return if (parts.size == 2 && parts[0].isNotEmpty()) {
            parts[0]
        } else {

            "Unknown"
        }
    }

}