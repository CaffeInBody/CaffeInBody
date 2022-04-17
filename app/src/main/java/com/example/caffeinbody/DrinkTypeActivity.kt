package com.example.caffeinbody

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.caffeinbody.databinding.ActivityDrinkTypeBinding

class DrinkTypeActivity : AppCompatActivity() {

    private val binding: ActivityDrinkTypeBinding by lazy {
        ActivityDrinkTypeBinding.inflate(
            layoutInflater
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.caffeBtn.setOnClickListener{nextactivity(0)}
        binding.generalBtn.setOnClickListener {nextactivity(1)}
        binding.etcBtn.setOnClickListener{nextactivity(2)}

    }

    fun nextactivity(num:Int){
        val caffeineListActivity =  CaffeineListActivity()
        val intent = Intent(this, caffeineListActivity::class.java)
        intent.putExtra("listnum",num)
        finish()
        startActivity(intent)

    }
}