package com.example.caffeinbody

import abak.tr.com.boxedverticalseekbar.BoxedVertical
import abak.tr.com.boxedverticalseekbar.BoxedVertical.OnValuesChangeListener
import android.R
import android.content.Intent
import android.graphics.Point
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.caffeinbody.databinding.ActivityDrinkCaffeineBinding

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
            binding.result.setText(resultInt.toString())
        }

        binding.size2.setOnClickListener {
            resultInt = arraySize[1] * rangevalue /100
            size = 1
            binding.result.setText(resultInt.toString())
        }

        binding.size3.setOnClickListener {
            resultInt = arraySize[2] * rangevalue /100
            size = 2
            binding.result.setText(resultInt.toString())
        }

        //용량 선택
        binding.volume.setOnBoxedPointsChangeListener(object : OnValuesChangeListener {
            override fun onPointsChanged(boxedPoints: BoxedVertical, value: Int) {
                println(value)
                rangevalue = value
                resultInt = value * arraySize[size] / 100
                binding.result.setText(resultInt.toString())
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