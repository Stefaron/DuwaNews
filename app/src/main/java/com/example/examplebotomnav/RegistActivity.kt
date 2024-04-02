package com.example.examplebotomnav

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.os.Handler
import android.widget.Button
import android.widget.CheckBox

class RegistActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_regist)

        val button: Button = findViewById(R.id.signup)
        val checkbox = findViewById<CheckBox>(R.id.checkBox)

        button.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        checkbox.setOnClickListener {
            val intent = Intent(this, BiometricActivity::class.java)
            startActivity(intent)
        }

    }

}