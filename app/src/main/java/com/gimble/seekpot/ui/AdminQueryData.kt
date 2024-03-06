package com.gimble.seekpot.ui

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.gimble.seekpot.databinding.ActivityAdminQueryDataBinding
import com.gimble.seekpot.feature.enquiry.domain.model.FoundItemData
import com.gimble.seekpot.feature.enquiry.presentation.ProgShowOne
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso

class AdminQueryData : AppCompatActivity() {
    private lateinit var binding : ActivityAdminQueryDataBinding
    private lateinit var itemId : String
    private lateinit var fireref : DatabaseReference
    private lateinit var itemlist: ArrayList<FoundItemData>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminQueryDataBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setup()
    }
    private fun setup() {
        itemId = intent.getStringExtra("itemId")!!
        fetchData(itemId)

    }

    private fun fetchData(itemId: String) {
        val sendingDialog = ProgShowOne()
        sendingDialog.show(supportFragmentManager, "sendingDialog")
        val databaseRef1 = FirebaseDatabase.getInstance().getReference("lostitems")
        val databaseRef2 = FirebaseDatabase.getInstance().getReference("founditems")

        itemId.let { id ->
            // First, check in the first database reference
            databaseRef1.child(id).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // Data found in the first path
                        sendingDialog.dismiss()
                        val foundItemData = dataSnapshot.getValue(FoundItemData::class.java)
                        setDataInUi(foundItemData)
                    } else {
                        // Data not found in the first path, check in the second path
                        databaseRef2.child(id).addListenerForSingleValueEvent(object :
                            ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    // Data found in the second path
                                    sendingDialog.dismiss()
                                    val foundItemData = dataSnapshot.getValue(FoundItemData::class.java)
                                    setDataInUi(foundItemData)
                                    // Handle the retrieved data here
                                } else {
                                    sendingDialog.dismiss()
                                    // Data not found in both paths
                                    // Handle the case where data is not found
                                }
                            }

                            override fun onCancelled(databaseError: DatabaseError) {
                                // Handle error
                            }
                        })
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle error
                }
            })
        }
    }


    private fun setDataInUi(foundItemData: FoundItemData?) {

        var status = "unknown"
        if (foundItemData?.category.equals("L")){
            status = "missing"
        }
        else if ( foundItemData?.category.equals("F")){
            status = "waiting"
        }
        val infoOnItem : String = " Name : ${foundItemData?.itemname} \n Type : ${foundItemData?.itemtype} \n Location : ${foundItemData?.itemlocation}" +
                "\n Date : ${foundItemData?.time} \n Status : $status"

        binding.datapanel.text = infoOnItem
        binding.datadescriptionpanel.text ="${foundItemData?.itemdescripton}"
        Picasso.get().load(foundItemData?.itempicture).into(binding.itempic)
        setUpBtn(foundItemData?.phonenumber,foundItemData?.category,foundItemData?.itemname)
    }

    private fun setUpBtn(phonenumber: String?, category: String?, itemname: String?) {
        binding.button4.setOnClickListener{
            makePhoneCall(phonenumber!!)
        }
        binding.delete.setOnClickListener{
            if(category.equals("L")){
                fireref = FirebaseDatabase.getInstance().getReference("lostitems")
                itemlist = arrayListOf()
                deleteItemByName(itemname!!)
            }
            else{
                fireref = FirebaseDatabase.getInstance().getReference("founditems")
                itemlist = arrayListOf()
                deleteItemByName(itemname!!)
            }
        }
    }
    fun makePhoneCall( number: String) {
        val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number))
        startActivity(intent)
    }
    private fun deleteItemByName(itemName: String) {
        val query = fireref.orderByChild("itemname").equalTo(itemName)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (itemSnapshot in snapshot.children) {
                    itemSnapshot.ref.removeValue()
                }
                // Handle success message or any other actions after deletion
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
                Log.e("deleteItemByName", "Error deleting item: ${error.message}")
            }
        })
    }
}