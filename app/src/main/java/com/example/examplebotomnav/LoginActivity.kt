package com.example.examplebotomnav

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import com.example.examplebotomnav.data.User
import com.example.examplebotomnav.home.MainActivity

class LoginActivity : AppCompatActivity() {
    val sampleUser = User("admin", "123")
    var listUser = listOf(sampleUser)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val username = findViewById<EditText>(R.id.username).text.toString()
        val password = findViewById<EditText>(R.id.password).text.toString()

        val signup = findViewById<TextView>(R.id.signup)
        val signin = findViewById<Button>(R.id.loginButton)

        signup.setOnClickListener {
            val intent = Intent(this, RegistActivity::class.java)
            startActivity(intent)
        }

        signin.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }

    fun addUser(username : String, password : String) {
        listUser
    }
}