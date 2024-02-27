package com.gimble.seekpot.feature.authentication

import android.content.Context
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth



class AuthenticationManager {

    // Registers an account
    fun registerAccount(appContext: Context, auth: FirebaseAuth, email: String, passcode: String, onCompletion: (Boolean) -> Unit
    ) {
        if (email.isNotEmpty() && passcode.isNotEmpty()) {
            val task: Task<*> = auth.createUserWithEmailAndPassword(email, passcode)
            task.addOnCompleteListener { authResult ->
                if (authResult.isSuccessful) {
                    // Registration successful
                    onCompletion(true)
                } else {
                    val exception = authResult.exception
                    Toast.makeText(appContext, exception?.localizedMessage, Toast.LENGTH_LONG).show()
                    onCompletion(false)
                }
            }
        } else {
            // Invalid email or password
            onCompletion(false)
        }
    }

    // Signs in
    fun signInAccount(appContext: Context, auth: FirebaseAuth, email: String, passcode: String, onCompletion: (Boolean) -> Unit) {
        if (email.isNotEmpty() && passcode.isNotEmpty()) {
            val task: Task<*> = auth.signInWithEmailAndPassword(email, passcode)
            task.addOnCompleteListener { authResult ->
                if (authResult.isSuccessful) {
                    // Sign-in successful
                    onCompletion(true)
                } else {
                    val exception = authResult.exception
                    Toast.makeText(appContext, exception?.localizedMessage, Toast.LENGTH_LONG).show()
                    onCompletion(false)
                }
            }
        } else {
            // Invalid email or password
            onCompletion(false)
        }
    }

    fun signOut(appContext: Context, auth: FirebaseAuth, onCompletion: () -> Unit) {
        try {
            auth.signOut()
            // Sign-out successful
            onCompletion()
        } catch (e: Exception) {
            // Handle exceptions, log, or show a meaningful error message
            Toast.makeText(appContext, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}