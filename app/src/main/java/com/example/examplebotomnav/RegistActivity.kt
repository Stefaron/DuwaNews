package com.example.examplebotomnav

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.examplebotomnav.databinding.ActivityRegistBinding
import com.example.examplebotomnav.home.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class RegistActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityRegistBinding
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Auth and Database
        auth = Firebase.auth
        database = Firebase.database.reference

        // Set up the sign-up button listener
        binding.btnSignup.setOnClickListener {
            val username = binding.usernameRegis.text.toString()
            val email = binding.emailRegis.text.toString()
            val noTelp = binding.noTelp.text.toString()
            val password = binding.password.text.toString()

            if (username.isEmpty() || email.isEmpty() || noTelp.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Ada data yang masih kosong", Toast.LENGTH_SHORT).show()
            } else {
                createAccount(username, email, noTelp, password)
            }
        }


    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            reload()
        }
    }

    private fun createAccount(username: String, email: String, noTelp: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                    user?.let {
                        val uid = it.uid
                        val userMap = User(username, email, noTelp)
                        database.child("users").child(uid).setValue(userMap)
                            .addOnCompleteListener { dbTask ->
                                if (dbTask.isSuccessful) {
                                    updateUI(user)
                                } else {
                                    Log.w(TAG, "setValue:failure", dbTask.exception)
                                    Toast.makeText(baseContext, "Failed to save user data.", Toast.LENGTH_SHORT).show()
                                }
                            }
                    }
                } else {
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }
    }

    private fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }
    }

    private fun sendEmailVerification() {
        val user = auth.currentUser!!
        user.sendEmailVerification()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Verification email sent to ${user.email}", Toast.LENGTH_SHORT).show()
                } else {
                    Log.e(TAG, "sendEmailVerification", task.exception)
                    Toast.makeText(this, "Failed to send verification email.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun reload() {
        // Optionally reload the current activity or update UI
    }

    companion object {
        private const val TAG = "EmailPassword"
    }
}

data class User(val username: String, val email: String, val noTelp: String)
