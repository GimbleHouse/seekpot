package com.gimble.seekpot.ui.frags

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.gimble.seekpot.databinding.FragmentSignUpBinding
import com.gimble.seekpot.ui.MainActivity
import java.util.Locale

class SignUpFrag : Fragment() {



    private lateinit var binding : FragmentSignUpBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       binding = FragmentSignUpBinding.inflate(layoutInflater,container,false)
       setup()
        return binding.root
    }


    private fun setup() {
        binding.fieldbutton.setOnClickListener{
            val email = binding.itemname.text.toString()
            val pass = binding.passwordEditText.text.toString()
            if(email.isNotEmpty() && pass.isNotEmpty() && isAllowedEmailDomain(email)){
                MainActivity.auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener {
                    val i = Intent(activity, MainActivity::class.java)
                    startActivity(i)
                    activity?.finish()
                }.addOnFailureListener { Toast.makeText(context,it.localizedMessage, Toast.LENGTH_SHORT).show()
                    Log.d("stuff",it.localizedMessage)
                }
            }
            else Toast.makeText(getActivity(),"Email not accepter !",Toast.LENGTH_SHORT).show()
        }
    }
    fun isAllowedEmailDomain(email: String): Boolean {
        val domain = email.substring(email.indexOf("@") + 1).lowercase(Locale.getDefault())
        return domain == "jainuniversity.ac.in"
    }
}