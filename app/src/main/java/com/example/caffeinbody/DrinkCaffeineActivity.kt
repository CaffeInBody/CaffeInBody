package com.example.caffeinbody

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.view.MenuItem
import com.example.caffeinbody.databinding.ActivityDrinkCaffeineBinding


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