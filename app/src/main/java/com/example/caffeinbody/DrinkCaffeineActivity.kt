package com.example.caffeinbody

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class DrinkCaffeineActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drink_caffeine)
        intent.getIntExtra("id",1) //data id 가져오기


    }
}