package com.example.wachacha

import android.app.Activity
import android.os.Bundle
import com.example.wachacha.databinding.ActivityMainWearOsBinding

class MainActivityWearOS : Activity() {

    private lateinit var binding: ActivityMainWearOsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainWearOsBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}