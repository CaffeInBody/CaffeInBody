package com.example.caffeinbody

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.caffeinbody.databinding.ActivityCaffeineListBinding


class CaffeineListActivity : AppCompatActivity() {

    private val binding: ActivityCaffeineListBinding by lazy {
        ActivityCaffeineListBinding.inflate(
            layoutInflater
        )
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_caffeine_list)
        var num= intent.getIntExtra("listnum",1)
        binding.textView2.setText(num)

    }
}