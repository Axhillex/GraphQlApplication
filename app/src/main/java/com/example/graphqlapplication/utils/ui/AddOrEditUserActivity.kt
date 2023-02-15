package com.example.graphqlapplication.utils.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.example.graphqlapplication.databinding.ActivityAddOrEditUserBinding

class AddOrEditUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddOrEditUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launchWhenCreated {
            binding = ActivityAddOrEditUserBinding.inflate(layoutInflater)
            setContentView(binding.root)
        }
    }
}