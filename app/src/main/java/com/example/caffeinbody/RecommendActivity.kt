package com.example.caffeinbody

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import com.example.caffeinbody.database.Drinks
import com.example.caffeinbody.database.DrinksDatabase
import com.example.caffeinbody.databinding.ActivityRecommendBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class RecommendActivity : AppCompatActivity() {
    private lateinit var db: DrinksDatabase
    lateinit var caffeineadapter: CaffeineAdapter


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
        caffeineadapter = CaffeineAdapter(this)


        //    caffeineadapter.type = CaffeinCase.SMALL

    //    val r = Runnable {
      //      try {
        CoroutineScope(Dispatchers.IO).launch {

                db = DrinksDatabase.getInstance(applicationContext)!!
                var datas = caffeine?.let { db.drinksDao().recommendcaffeine(it) }
                //Log.e("data",db.drinksDao().recommendcaffeine(100.0).toString())
                if(datas?.size != 0 && datas != null) {
                    datas?.let {
                        //데이터가 많아서 랜덤으로 6개만 뽑아서 보여줌
                        it.shuffled() //데이터 섞기
                        for (i in 0 until 6){ //6개만 뽑아서 넣음
                            if(it.size <= i ) break
                            caffeineadapter.datas.add(it[i])
                        }
                        //caffeineadapter.datas.addAll(it)
                        binding.nonetext.visibility = GONE //데이터 없을때 나오는 텍스트
                        binding.recyclerView.visibility = VISIBLE

                    }

                }
                //  Log.d("tag", "Error - "+ db.drinksDao().getAll().toString())

     //       } catch (e: Exception) {
      //          Log.d("tag", "Error - $e")
     //       }
        }
      //  val thread = Thread(r)
     //   thread.start()

        binding.recyclerView.adapter = caffeineadapter
        caffeineadapter.notifyDataSetChanged()

        val servingsize = App.prefs.currentcaffeine

    }
    fun <T> List<T>.random() : T {
        val random = Random().nextInt((size))
        return get(random)
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