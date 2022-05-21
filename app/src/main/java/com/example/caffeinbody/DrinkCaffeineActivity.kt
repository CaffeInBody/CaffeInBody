package com.example.caffeinbody

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.caffeinbody.databinding.ActivityDrinkCaffeineBinding
import com.example.caffeinbody.databinding.RegisterLayoutDrinkBinding

class DrinkCaffeineActivity : AppCompatActivity() {
    private val binding: ActivityDrinkCaffeineBinding by lazy{
        ActivityDrinkCaffeineBinding.inflate(
            layoutInflater
        )
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        //setContentView(R.layout.activity_drink_caffeine)
        intent.getIntExtra("id",1) //data id 가져오기

        val size1_btn = binding.size1
        val size2_btn = binding.size2
        val size3_btn = binding.size3

        size1_btn.setOnClickListener {

        }

        size2_btn.setOnClickListener {

        }

        size3_btn.setOnClickListener {

        }



        binding.save.setOnClickListener {
            finish()
        }
    }
}