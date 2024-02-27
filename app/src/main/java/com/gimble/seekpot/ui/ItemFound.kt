package com.gimble.seekpot.ui

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import com.gimble.seekpot.R
import com.gimble.seekpot.databinding.ActivityItemFoundBinding
import com.gimble.seekpot.feature.enquiry.domain.model.FoundItemData
import com.gimble.seekpot.feature.enquiry.presentation.ProgShow
import com.google.android.material.internal.ContextUtils.getActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.util.UUID


class ItemFound : AppCompatActivity() {
    private lateinit var binding : ActivityItemFoundBinding
    private lateinit var  firebaseRef : DatabaseReference
    private val PICK_IMAGE_REQUEST = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityItemFoundBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setup()
    }

    private fun setup() {
        binding.imageadder.setOnClickListener{
            openGallery()
        }
        binding.submit.setOnClickListener{
            val name = binding.itemname.text.toString()
            val desc = binding.itemdescription.text.toString()
            val image: Bitmap? = (binding.imageView5.drawable as? BitmapDrawable)?.bitmap
            val ph = binding.phone.text.toString()
            val loc = binding.location.text.toString()
            val type ="misc"


            if(name!=null && desc!=null && image!=null && type!= null){
                uploadtodatabase(name,desc,image,ph,loc,type)
            }
            else{
                Toast.makeText(this,"Fill in all info",Toast.LENGTH_SHORT).show()
                binding.itemname.error="fill it up "
                binding.description.error

            }



        }
    }
    //image getting
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            // Get the URI of the selected image
            val imageUri: Uri? = data.data
            binding.imageView5.setImageURI(imageUri)
        }
    }


    //database stuff
    private fun uploadtodatabase(name: String, desc: String, image: Bitmap?, ph :String,loc:String, typer: String) {
        //show progress
        val sendingDialog = ProgShow()
        sendingDialog.show(supportFragmentManager, "sendingDialog")

        uploadimage(image) { imageUrl ->
            firebaseRef = FirebaseDatabase.getInstance().getReference("founditems")
            val id = firebaseRef.push().key!!
            val filee = FoundItemData(id, name, desc, imageUrl,ph,loc, typer)

            firebaseRef.child(id).setValue(filee).addOnCompleteListener {
                sendingDialog.dismiss()
               sucessfulEnd()
            }.addOnFailureListener {
                sendingDialog.dismiss()
            }
        }
    }

    private fun sucessfulEnd(){
        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
        finish()
    }

    private fun uploadimage(image: Bitmap?, callback: (String) -> Unit) {
        val storageRef = FirebaseStorage.getInstance().reference
        val imagesRef = storageRef.child("itemsfound/${UUID.randomUUID()}.jpg")

        image?.let {
            val baos = ByteArrayOutputStream()
            it.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val imageData = baos.toByteArray()

            val uploadTask = imagesRef.putBytes(imageData)
            uploadTask.addOnSuccessListener { taskSnapshot ->
                // Image uploaded successfully, get download URL
                imagesRef.downloadUrl.addOnSuccessListener { uri ->
                    val imageUrl = uri.toString()
                    Log.d("picsent", "Image uploaded successfully. URL: $imageUrl")
                    callback(imageUrl) // Pass the URL to the callback function
                }.addOnFailureListener {
                    Log.e("picnotsent", "Failed to get image URL: ${it.message}")
                }
            }.addOnFailureListener { exception ->
                Log.e("picnotsent", "Failed to upload image: ${exception.message}")
            }
        }
    }
}