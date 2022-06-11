package com.example.wachacha

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.wachacha.databinding.ActivityDrinkregisterBinding

class DrinkActivity: AppCompatActivity() {
    private val binding: ActivityDrinkregisterBinding by lazy {
        ActivityDrinkregisterBinding.inflate(
            layoutInflater
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val favorite = App.prefs.favorite
        binding.messageFav.setText(favorite)
        Log.e("favorite", favorite.toString() + "공백")
    }
}