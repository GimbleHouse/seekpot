package com.gimble.seekpot.ui.frags

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import com.gimble.seekpot.R
import com.gimble.seekpot.databinding.FragmentSignInBinding
import com.gimble.seekpot.feature.authentication.AuthenticationManager
import com.gimble.seekpot.ui.MainActivity


class SignInFrag : Fragment() {

private lateinit var binding : FragmentSignInBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignInBinding.inflate(layoutInflater,container,false)
        setup()
        return binding.root
    }
    private fun setup() {
        binding.signupButton.setOnClickListener{
            val sip = SignUpFrag()
            val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.fragmentContainerView,sip)
            transaction.commit()
        }
        binding.signinButton.setOnClickListener {
            val am = AuthenticationManager()
            am.signInAccount(requireContext(), MainActivity.auth, binding.itemname.text.toString(),binding.passwordEditText.text.toString()
            ) { isSuccess ->
                if (isSuccess) {
                    // Sign-in successful, navigate to the home page
                    navigateToHomePage()
                } else {
                    // Sign-in failed, handle accordingly
                    Toast.makeText(context,"something went wrong", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    private fun navigateToHomePage() {
        val i = Intent(activity,MainActivity::class.java)
        startActivity(i)
        activity?.finish()
    }

}