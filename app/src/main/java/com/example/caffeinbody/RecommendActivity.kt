package com.example.caffeinbody

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import com.example.caffeinbody.database.DrinksDatabase
import com.example.caffeinbody.databinding.ActivityRecommendBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class RecommendActivity : AppCompatActivity() {
    private lateinit var db: DrinksDatabase
    lateinit var caffeineadapter: CaffeineAdapter
    lateinit var caffeineadapternon: CaffeineAdapter

    private val binding: ActivityRecommendBinding by lazy{
        ActivityRecommendBinding.inflate(
            layoutInflater
        )
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        //    supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        var caffeine = App.prefs.currentcaffeine?.toDouble()
        caffeineadapter = CaffeineAdapter(this, CaffeinCase.SMALL)
        caffeineadapternon = CaffeineAdapter(this, CaffeinCase.SMALL)

        //    caffeineadapter.type = CaffeinCase.SMALL

        //    val r = Runnable {
        //      try {
        CoroutineScope(Dispatchers.IO).launch {

            db = DrinksDatabase.getInstance(applicationContext)!!

            var nondatas = db.drinksDao().recommendnoncaffeine()
            var datas = caffeine?.let { db.drinksDao().recommendcaffeine(it) }
            Log.e("data",db.drinksDao().recommendnoncaffeine().toString())

            if (datas?.size != 0 && datas != null) {
                datas?.let {
                    //데이터가 많아서 랜덤으로 6개만 뽑아서 보여줌
                    it.shuffled() //데이터 섞기
                    for (i in 0 until 6) { //6개만 뽑아서 넣음
                        if (it.size <= i) break
                        caffeineadapter.datas.add(it[i])
                    }
                    //caffeineadapter.datas.addAll(it)
                    nondatas?.let {
                        it.shuffled() //데이터 섞기
                        for (i in 0 until 3) { //6개만 뽑아서 넣음
                            if (it.size <= i) break
                            caffeineadapternon.datas.add(it[i])
                        }
                        //caffeineadapter.datas.addAll(it)

                    }
                    runOnUiThread {
                        binding.nonetext.visibility = GONE //데이터 없을때 나오는 텍스트
                        binding.recyclerView.visibility = VISIBLE
                        // call the invalidate()
                    }
                }

            }
        }



            binding.recyclerView.adapter = caffeineadapter
            caffeineadapter.notifyDataSetChanged()

            binding.recyclerView2.adapter = caffeineadapternon
            caffeineadapternon.notifyDataSetChanged()

            val servingsize = App.prefs.currentcaffeine


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