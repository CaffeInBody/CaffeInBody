package com.example.caffeinbody

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.caffeinbody.databinding.FragmentSettingBinding
import com.example.caffeinbody.databinding.FragmentSurvey1Binding
import com.example.caffeinbody.databinding.FragmentSurvey3Binding

class Survey3Activity  : AppCompatActivity() {

    private val binding: FragmentSurvey3Binding by lazy {
        FragmentSurvey3Binding.inflate(
            layoutInflater
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.buttonNext.setOnClickListener {

            val selectActivity = MainActivity()
            val intent = Intent(this, selectActivity::class.java)
            startActivity(intent)
            finish()
        }
        setContentView(binding.root)
    }





}