package com.example.caffeinbody

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
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
        setSupportActionBar(binding.toolbar)
        //    supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setTitle("아메리카노")
        //setContentView(R.layout.activity_drink_caffeine)

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

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        android.R.id.home -> {
            finish()
            true
        }

        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }
}