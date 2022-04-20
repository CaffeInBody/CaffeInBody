package com.example.caffeinbody

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.example.caffeinbody.databinding.ActivityCaffeineListBinding


class CaffeineListActivity : AppCompatActivity() {
    var arrayString = arrayOf<String>("카페음료","일반음료","기타")
    lateinit var caffeineadapter: CaffeineAdapter

    private val binding: ActivityCaffeineListBinding by lazy {
        ActivityCaffeineListBinding.inflate(
            layoutInflater
        )
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        var num= intent.getIntExtra("listnum",1)
        binding.textView2.setText(arrayString[num])


        caffeineadapter = CaffeineAdapter(this)
        caffeineadapter.datas.add(CaffeineData(0,"아메리카노","")) //샘플 데이터
        binding.caffeinList.adapter = caffeineadapter
        caffeineadapter.notifyDataSetChanged()


        val layoutManager = GridLayoutManager(this,2)
        binding.caffeinList.layoutManager = layoutManager

    }
}