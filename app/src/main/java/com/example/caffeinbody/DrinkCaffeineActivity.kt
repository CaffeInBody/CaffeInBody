package com.example.caffeinbody

import abak.tr.com.boxedverticalseekbar.BoxedVertical
import abak.tr.com.boxedverticalseekbar.BoxedVertical.OnValuesChangeListener
import android.content.Intent
import android.graphics.Point
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.caffeinbody.databinding.ActivityDrinkCaffeineBinding
import org.json.JSONArray
import java.util.*


var arraySize = arrayOf<Int>(100,200,300)
var resultInt:Int = 100

var rangevalue:Int = 100
var size:Int = 0

class DrinkCaffeineActivity : AppCompatActivity() {
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
        supportActionBar!!.setTitle("아메리카노")
        //setContentView(R.layout.activity_drink_caffeine)


        val display = windowManager.defaultDisplay // in case of Activity
/* val display = activity!!.windowManaver.defaultDisplay */ // in case of Fragment
        var devicesize = Point()
        display.getRealSize(devicesize) // or getSize(size)
        binding.volume.layoutParams.height=devicesize.x *355/1000

        //음료 사이즈 선택
        binding.size1.setOnClickListener {
            resultInt = arraySize[0] * rangevalue /100
            size = 0
            binding.result.setText(resultInt.toString()+"mg")
        }

        binding.size2.setOnClickListener {
            resultInt = arraySize[1] * rangevalue /100
            size = 1
            binding.result.setText(resultInt.toString()+"mg")
        }

        binding.size3.setOnClickListener {
            resultInt = arraySize[2] * rangevalue /100
            size = 2
            binding.result.setText(resultInt.toString()+"mg")
        }

        //용량 선택
        binding.volume.setOnBoxedPointsChangeListener(object : OnValuesChangeListener {
            override fun onPointsChanged(boxedPoints: BoxedVertical, value: Int) {
                println(value)
                rangevalue = value
                resultInt = value * arraySize[size] / 100
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
            addTimeJson()//카페인 등록 시간을 jsonArray로
            addCaffeineJson(resultInt)//각각의 카페인 양을 jsonArray로

            val msg = App.prefs.todayCaf
            if (msg != null) {
                resultInt = msg + resultInt
            }
            App.prefs.todayCaf = resultInt

            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
            finish()
        }
    }

    public fun addTimeJson(){
        //시간을 json으로 저장
        val calendar = Calendar.getInstance()
        val date = Date()
        calendar.setTime(date)

        var blank = App.prefs.date
        if (blank != null){
            var a = JSONArray(blank)
            a.put(calendar.time.hours*60 + calendar.time.minutes)
            App.prefs.date = a.toString()
            Log.e("json length", " // " + calendar.time.seconds)
        }else{
            var a = JSONArray()
            a.put(calendar.time.hours*60 + calendar.time.minutes)
            App.prefs.date = a.toString()
        }
    }

    public fun addCaffeineJson(caffeine: Int){
        var blank = App.prefs.todayCafJson
        if (blank != null){
            var a = JSONArray(blank)
            a.put(caffeine)
            App.prefs.todayCafJson = a.toString()
            Log.e("json caffeine", " // $caffeine" + " " + a.length())
        }else{
            var a = JSONArray()
            a.put(caffeine)
            App.prefs.todayCafJson = a.toString()
            Log.e("json caffeine", " // $caffeine" + " " + a.length())
        }
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