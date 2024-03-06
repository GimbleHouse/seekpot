package com.gimble.seekpot.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.gimble.seekpot.R
import com.gimble.seekpot.databinding.ActivityAdminHomeBinding
import com.gimble.seekpot.ui.frags.FoundItemFrag
import com.gimble.seekpot.ui.frags.LostItemFrag

class AdminHome : AppCompatActivity() {
    private lateinit var binding : ActivityAdminHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUp()
    }

    private fun setUp() {
        //enabling admin
        val sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean("admin", true)
        editor.apply()
        binding.bottomNav.setOnItemSelectedListener {
                item ->
            when(item.itemId){
                R.id.homeNav -> pokefragg()
                R.id.pokeNav -> homefragg()
                else -> false}

            }
        binding.acc.setOnClickListener{
            val i = Intent(this,Account::class.java)
            startActivity(i)
            finish()
        }

        }
    private fun pokefragg(): Boolean {
        fragReplacer(LostItemFrag())
        return true
    }

    private fun homefragg(): Boolean {
        fragReplacer(FoundItemFrag())
        return true
    }
    private fun fragReplacer(frag: Fragment){
        val fragManager = supportFragmentManager
        val fragTransaction = fragManager.beginTransaction()
        fragTransaction.replace(R.id.fragcontainer,frag)
        fragTransaction.commit()

    }
    }
