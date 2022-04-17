package com.example.caffeinbody

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.caffeinbody.databinding.ActivityCaffeineListBinding


class CaffeineListActivity : AppCompatActivity() {
    var arrayString = arrayOf<String>("카페음료","일반음료","기타")
    private val binding: ActivityCaffeineListBinding by lazy {
        ActivityCaffeineListBinding.inflate(
            layoutInflater
        )
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        intent=intent
        var num= intent.getIntExtra("listnum",1)
        binding.textView2.setText(arrayString[num])

    }
}