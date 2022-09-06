package com.example.caffeinbody

import abak.tr.com.boxedverticalseekbar.BoxedVertical
import abak.tr.com.boxedverticalseekbar.BoxedVertical.OnValuesChangeListener
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Point
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.caffeinbody.DetailActivity.Companion.getDate
import com.example.caffeinbody.DetailActivity.Companion.getMonth
import com.example.caffeinbody.DetailActivity.Companion.getTime
import com.example.caffeinbody.DetailActivity.Companion.getYear
import com.example.caffeinbody.ReportFragment.Companion.calMonthCaffeineColor
import com.example.caffeinbody.ReportFragment.Companion.saveMonthCafJson
import com.example.caffeinbody.database.Drinks
import com.example.caffeinbody.database.DrinksDatabase
import com.example.caffeinbody.databinding.ActivityDrinkCaffeineBinding
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.Wearable
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import org.json.JSONArray
import java.util.*



class DrinkCaffeineActivity : AppCompatActivity() {
    var arraySize = arrayOf<Int>(1,2,3)
    var resultInt = 100f
    var article : Drinks? = null
    var caffeine:Int = 100 //샷
    var rangevalue:Int = 100 //샷
    var size:Int = 0
    private lateinit var db: DrinksDatabase

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

        //setContentView(R.layout.activity_drink_caffeine)

        val intent = getIntent()
        val name = intent.getIntExtra("name",0)
        binding.shot.minValue = 0
        binding.shot.maxValue = 12

        db = DrinksDatabase.getInstance(applicationContext)!!

        CoroutineScope(Dispatchers.Main).launch {
            CoroutineScope(Dispatchers.IO).async {
                article =  db.drinksDao().selectByID(name)
                arraySize[0] = article!!.caffeine?.caffeine1?.toInt()!!
                arraySize[1] = article!!.caffeine?.caffeine2?.toInt()!!
                arraySize[2] = article!!.caffeine?.caffeine3?.toInt()!!
            }.await()
            runOnUiThread {
                initUI(article!!)
                if(article!!.size?.size1 == 0 || article!!.size?.size2 ==0) {
                    binding.textView.visibility = GONE
                    binding.radio.visibility = GONE
                }
                else if (article!!.size?.size3 ==0 ) binding.size3.visibility = GONE
                if(article!!.isCoffee) binding.shot.value =2
                else binding.shot.value =0
                if(!article!!.iscafe) binding.cafeShotLayout.visibility =GONE
                setImg(article!!)
            }
            binding.size1.isChecked = true

        }

        //음료 사이즈 선택
        binding.size1.setOnClickListener {
            resultInt =  arraySize[0] * rangevalue /100f
            size = 0
            binding.result.setText(resultInt.toString()+"mg")
        }

        binding.size2.setOnClickListener {
            resultInt = arraySize[1] * rangevalue /100f
            size = 1
            binding.result.setText(resultInt.toString()+"mg")
        }

        binding.size3.setOnClickListener {
            resultInt = arraySize[2]* rangevalue /100f
            size = 2
            binding.result.setText(resultInt.toString()+"mg")
        }
        binding.shot.setOnValueChangedListener { numberPicker, i, i2 ->
            resultInt += i2 * (article?.caffeine?.caffeine1?.toInt()!! / 2)


        }
        //용량 선택
        binding.volume.setOnBoxedPointsChangeListener(object : OnValuesChangeListener {
            override fun onPointsChanged(boxedPoints: BoxedVertical, value: Int) {
                println(value)
                rangevalue = value
                resultInt = caffeine * value * arraySize[size] / 100f
                binding.textView7.setText((value.toDouble() / 100).toString()+"잔")
                binding.result.setText(resultInt.toString()+"mg")
            }

            override fun onStartTrackingTouch(boxedPoints: BoxedVertical) {
              //  Toast.makeText(this, "onStartTrackingTouch", Toast.LENGTH_SHORT).show()
            }

            override fun onStopTrackingTouch(boxedPoints: BoxedVertical) {
              //  Toast.makeText(this, "onStopTrackingTouch", Toast.LENGTH_SHORT).show()
            }
        })

        // 저장
        binding.save.setOnClickListener {
            saveTime()//카페인 등록 시간 저장

            val msg = App.prefs.todayCaf//하루섭취카페인
            var msg2 = App.prefs.remainCafTmp//체내남은카페인(날짜변경시초기화안됨+시간별계산된버전)
            if (msg != null) {//msg2를 시간별로 계속 업데이트 해야만 가능하다
                msg2= msg2!! + resultInt
                App.prefs.remainCafTmp = msg2
                resultInt = msg + resultInt
            }else{
                App.prefs.remainCafTmp = resultInt
            }
            Log.e("DrinkCaffeine", "msg2!: $msg2 resultInt: $resultInt")
            App.prefs.todayCaf = resultInt
            App.prefs.remainCaf = App.prefs.remainCafTmp
            Log.e("drink: ", "remainCafReal: " + App.prefs.remainCafTmp)

            var todayCaf = App.prefs.todayCaf
            var caffeineColor = calMonthCaffeineColor(todayCaf!!)

            saveMonthCafJson(getDate(), caffeineColor, getMonth())

            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
            finish()
        }
    }

    fun saveTime(){
        App.prefs.registeredYear = getYear()
        App.prefs.registeredMonth = getMonth()
        App.prefs.registeredDate = getDate()
        App.prefs.registeredTime = getTime()
        Log.e("DrinkCaffeineActivity", App.prefs.registeredDate.toString())
    }

    //뒤로가기 버튼
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

    fun setImg(article: Drinks){
        if (article.imgurl.startsWith("https:")) {
            Glide.with(this).load(article.imgurl).placeholder(R.drawable.logo)
                .override(500).into(binding.imageViewDrink)

        } else if(article.imgurl.startsWith("$cacheDir/")){

            val bm = BitmapFactory.decodeFile(article.imgurl)
            Glide.with(this).load(bm).placeholder(R.drawable.logo)
                .override(600).into(binding.imageViewDrink)

        }
        else {
            if (article.madeBy == "스타벅스") Glide.with(this)
                .load(R.drawable.starbucks_logo).override(250).into(binding.logo)
            else if (article.madeBy == "이디야") Glide.with(this)
                .load(R.drawable.ediya_logo).override(250).into(binding.logo)
            else if (article.madeBy == "투썸플레이스") Glide.with(this)
                .load(R.drawable.twosome_logo).override(250).into(binding.logo)
            else if (article.madeBy == "할리스") Glide.with(this)
                .load(R.drawable.hollys_logo).override(250).into(binding.logo)
            else if (article.madeBy == "빽다방") Glide.with(this)
                .load(R.drawable.paiks_logo).override(250).into(binding.logo)
            else if (article.madeBy == "더벤티") Glide.with(this)
                .load(R.drawable.theventi_logo).override(250).into(binding.logo)
            else if (article.madeBy == "공차") Glide.with(this)
                .load(R.drawable.gongcha_logo).override(250).into(binding.logo)
            else Glide.with(this).load(R.drawable.logo).into(binding.logo)
            binding.imageViewDrink.visibility = GONE
            binding.logo.visibility = View.VISIBLE
        }


    }
    fun initUI(article: Drinks){
        Log.e("article", article.toString())
        supportActionBar!!.setTitle(article?.drinkName)

        val display = windowManager.defaultDisplay // in case of Activity
/* val display = activity!!.windowManaver.defaultDisplay */ // in case of Fragment
        var devicesize = Point()
        display.getRealSize(devicesize) // or getSize(size)
        binding.volume.layoutParams.height=devicesize.x *355/1000

    }
    fun selectByID(db: DrinksDatabase, find: Int){
        CoroutineScope(Dispatchers.Main).launch {
            CoroutineScope(Dispatchers.IO).async {
               article =  db.drinksDao().selectByID(find)
            }.await()

        }
    }



}