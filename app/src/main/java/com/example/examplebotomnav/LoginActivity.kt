package com.example.examplebotomnav

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.examplebotomnav.data.User
import com.example.examplebotomnav.databinding.ActivityLoginBinding
import com.example.examplebotomnav.databinding.ActivityRegistBinding
import com.example.examplebotomnav.home.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityLoginBinding

    val sampleUser = User("admin", "123")
    var listUser = listOf(sampleUser)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Auth
        auth = Firebase.auth

        // Check if user is already logged in
        checkUserSession()

        binding.signup.setOnClickListener {
            val intent = Intent(this, RegistActivity::class.java)
            startActivity(intent)
        }

        // Handle the login button click
        binding.loginButton.setOnClickListener {
            val email = binding.tvEmail.text.toString()
            val password = binding.tvPassword.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Email atau password tidak boleh kosong", Toast.LENGTH_SHORT).show()
            } else {
                signIn(email, password)
            }
        }
    }

    private fun checkUserSession() {
        val sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)

        if (isLoggedIn) {
            // User is already logged in, go to main activity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    saveUserSession(user)
                    updateUI(user)
                } else {
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }
    }

    private fun saveUserSession(user: FirebaseUser?){
        SessionManager.setLoggedIn(this,true)
        val sharedPreferences = getSharedPreferences("User Session", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("userEmail",user?.email)
        editor.apply()
    }

    private fun signOut() {
        auth.signOut()
        clearUserSession()
    }


    fun clearUserSession() {
        val sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            // Go to main activity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    companion object {
        private const val TAG = "EmailPassword"
    }

    fun addUser(username : String, password : String) {
        listUser
    }
}