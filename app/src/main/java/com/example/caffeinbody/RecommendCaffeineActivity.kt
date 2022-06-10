package com.example.caffeinbody

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.caffeinbody.databinding.ActivityRecommendCaffeineBinding

class RecommendCaffeineActivity : AppCompatActivity() {
    private val binding: ActivityRecommendCaffeineBinding by lazy {
        ActivityRecommendCaffeineBinding.inflate(
            layoutInflater
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        var caffeine = intent.getDoubleExtra("caffeine", 0.0)

        binding.tvRecommendCaffeine.text = caffeine.toString()

        binding.buttonCheck.setOnClickListener {
            val selectActivity = MainActivity()
            val intent = Intent(this, selectActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}