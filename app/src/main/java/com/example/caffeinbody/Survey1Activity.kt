package com.example.caffeinbody

import android.R
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.caffeinbody.databinding.FragmentSurvey1Binding


class Survey1Activity  : AppCompatActivity() {

    private val binding: FragmentSurvey1Binding by lazy {
        FragmentSurvey1Binding.inflate(
            layoutInflater
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.buttonNext.setOnClickListener {
            val selectActivity =  Survey2Activity()
            val intent = Intent(this, selectActivity::class.java)
            startActivity(intent)
            finish()

        }

    }



}