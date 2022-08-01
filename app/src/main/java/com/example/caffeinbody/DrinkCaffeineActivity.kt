package com.example.caffeinbody

import abak.tr.com.boxedverticalseekbar.BoxedVertical
import abak.tr.com.boxedverticalseekbar.BoxedVertical.OnValuesChangeListener
import android.content.Intent
import android.graphics.Point
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.caffeinbody.DetailActivity.Companion.getDate
import com.example.caffeinbody.DetailActivity.Companion.getMonth
import com.example.caffeinbody.DetailActivity.Companion.getTime
import com.example.caffeinbody.DetailActivity.Companion.getYear
import com.example.caffeinbody.ReportFragment.Companion.calMonthCaffeineColor
import com.example.caffeinbody.ReportFragment.Companion.saveMonthCafJson
import com.example.caffeinbody.databinding.ActivityDrinkCaffeineBinding
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.Wearable
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.json.JSONArray
import java.util.*


var arraySize = arrayOf<Int>(1,2,3)
var resultInt = 100f

var caffeine:Int = 100 //샷
var rangevalue:Int = 100 //샷
var size:Int = 0

class DrinkCaffeineActivity : AppCompatActivity() {
    private val binding: ActivityDrinkCaffeineBinding by lazy{
        ActivityDrinkCaffeineBinding.inflate(
            layoutInflater
        )
    }
    private val dataClient by lazy { Wearable.getDataClient(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        //    supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        //setContentView(R.layout.activity_drink_caffeine)

        val intent = getIntent()
        val name = intent.getStringExtra("name")
        val image= intent.getStringExtra("img")
        caffeine = intent.getIntExtra("caffeine",100)

        supportActionBar!!.setTitle(name.toString())

        Glide.with(this).load(image).into(binding.imageViewDrink)

        if(image == null || image == "" )
            Glide.with(this).load(R.drawable.coffee_sample).into(binding.imageViewDrink)
        else if ( image == "url") Glide.with(this).load(R.drawable.cola_sample).into(binding.imageViewDrink)
        else Glide.with(this).load(image).override(800,).into(binding.imageViewDrink)


        val display = windowManager.defaultDisplay // in case of Activity
/* val display = activity!!.windowManaver.defaultDisplay */ // in case of Fragment
        var devicesize = Point()
        display.getRealSize(devicesize) // or getSize(size)
        binding.volume.layoutParams.height=devicesize.x *355/1000

        //음료 사이즈 선택
        binding.size1.setOnClickListener {
            resultInt = arraySize[0] * caffeine * rangevalue /100f
            size = 0
            binding.result.setText(resultInt.toString()+"mg")
        }

        binding.size2.setOnClickListener {
            resultInt = arraySize[1] * caffeine * rangevalue /100f
            size = 1
            binding.result.setText(resultInt.toString()+"mg")
        }

        binding.size3.setOnClickListener {
            resultInt = arraySize[2] * caffeine * rangevalue /100f
            size = 2
            binding.result.setText(resultInt.toString()+"mg")
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
            sendCaffeineDatas(resultInt)

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

    private fun sendCaffeineDatas(msg: Float) {
        Log.e("보내짐", "")
        lifecycleScope.launch {
            Log.e("TAG", "안녕")
            try {
                val request = PutDataMapRequest.create("/favorite").apply {
                    dataMap.putString("favorite", "누적 카페인: $msg")
                    //dataMap.putString(FAVORITE_KEY, (++count).toString())//메시지가 변경돼야 전송됨.
                    //dataMap.putStringArrayList(FAVORITE_KEY, 리스트)//즐겨찾기 리스트
                }
                    .asPutDataRequest()
                    .setUrgent()

                val result = dataClient.putDataItem(request).await()
                Log.e("DrinkCaffeineActivity", "DataItem saved: $result")
            } catch (cancellationException: CancellationException) {
                Log.e("DrinkCaffeineActivity", "캔슬됨")
            } catch (exception: Exception) {
                Log.d("DrinkCaffeineActivity", "Saving DataItem failed: $exception")
            }
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


}