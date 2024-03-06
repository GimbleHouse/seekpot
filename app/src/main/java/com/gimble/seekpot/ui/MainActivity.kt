package com.gimble.seekpot.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.gimble.seekpot.databinding.ActivityMainBinding
import com.gimble.seekpot.feature.enquiry.domain.model.FoundItemData
import com.gimble.seekpot.feature.enquiry.presentation.EnquiryAdapter
import com.gimble.seekpot.feature.enquiry.presentation.ProgShowOne
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {
    private lateinit var fireref : DatabaseReference
    private lateinit var itemlist: ArrayList<FoundItemData>
    private lateinit var firerefB : DatabaseReference
    private lateinit var itemlistB: ArrayList<FoundItemData>
    companion object{
        lateinit var auth: FirebaseAuth
    }
    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?){
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
            val sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            val isAdmin = sharedPref.getBoolean("admin", false)

            if (isAdmin) {
                // Start a different activity for admin
                val intent = Intent(this, AdminHome::class.java)
                startActivity(intent)
                finish() // Optionally finish the current activity
            }
            else {
                binding.username.text = extractNameFromEmail(auth.currentUser?.email.toString())
                binding.acc.setOnClickListener {
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

                fireref = FirebaseDatabase.getInstance().getReference("lostitems")
                itemlist = arrayListOf()
                firerefB = FirebaseDatabase.getInstance().getReference("founditems")
                itemlistB = arrayListOf()
                fetchData()

                //fetchDataForFound(itemlist)
                binding.recycler.apply {
                    setHasFixedSize(true)
                    layoutManager = LinearLayoutManager(this.context)
                }
            }
            }

        //fetchDataFromDatabase()
    }
    private fun jumperToAuthentication(){
        val i = Intent(this,Authentication::class.java)
        startActivity(i)
        finish()
    }
    private fun extractNameFromEmail(email: String): String{
        val parts = email.split("@")
        return if (parts.size == 2 && parts[0].isNotEmpty()) {
            parts[0]
        } else {

            "Unknown"
        }
    }
    private fun fetchData() {
        val sendingDialog = ProgShowOne()
        sendingDialog.show(supportFragmentManager, "sendingDialog")
        val currentUser = FirebaseAuth.getInstance().currentUser
        val currentUserId = currentUser?.uid

        currentUserId?.let { userId ->
            // Assuming you have a field named "userId" in your FoundItemData class
            fireref.orderByChild("userid").equalTo(userId)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val itemlist = mutableListOf<FoundItemData>()
                        for (contactSnap in snapshot.children) {
                            val item = contactSnap.getValue(FoundItemData::class.java)
                            item?.let {
                                itemlist.add(it)
                            }
                        }

                        fetchDataForFound(itemlist)
                        sendingDialog.dismiss()
                    }

                    override fun onCancelled(error: DatabaseError) {
                        fetchDataForFound(itemlist)
                        sendingDialog.dismiss()
                    }
                })
        } ?: run {
            Log.e("fetchData", "Current user not logged in")
        }
    }
    private fun fetchDataForFound(itemlist: MutableList<FoundItemData>) {
        val sendingDialog = ProgShowOne()
        sendingDialog.show(supportFragmentManager, "sendingDialog")
        val currentUser = FirebaseAuth.getInstance().currentUser
        val currentUserId = currentUser?.uid

        currentUserId?.let { userId ->
            // Assuming you have a field named "userId" in your FoundItemData class
            firerefB.orderByChild("userid").equalTo(userId)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val itemlistB = mutableListOf<FoundItemData>()
                        for (contactSnap in snapshot.children) {
                            val item = contactSnap.getValue(FoundItemData::class.java)
                            item?.let {
                                Log.d("shit",it.toString())
                                itemlistB.add(it)
                                itemlistB.addAll(itemlist)
                            }
                        }
                        sendingDialog.dismiss()
                        val adapters = EnquiryAdapter(itemlistB,this@MainActivity)
                        binding.recycler.adapter = adapters
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(this@MainActivity, "Something went wrong", Toast.LENGTH_SHORT).show()
                        sendingDialog.dismiss()
                    }
                })
        } ?: run {
            Log.e("fetchData", "Current user not logged in")
        }
    }


}