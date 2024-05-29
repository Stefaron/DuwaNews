package com.example.examplebotomnav

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.os.Handler
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast
import com.example.examplebotomnav.databinding.ActivityRegistBinding

class RegistActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegistBinding.inflate(layoutInflater)
        setContentView(binding.root)



    }
}