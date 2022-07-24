package com.example.caffeinbody

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.caffeinbody.databinding.ActivitySurveyResultBinding

class SurveyResultActivity : AppCompatActivity() {
    private val binding: ActivitySurveyResultBinding by lazy {
        ActivitySurveyResultBinding.inflate(
            layoutInflater
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        var caffeine = intent.getDoubleExtra("caffeine", 0.0)
        var recommendDayCaffeine = 400.0

        var age = App.prefs.age
        var isPregnant = App.prefs.isPregnant
        var heartbeat = App.prefs.heartbeat
        var headache = App.prefs.headache

        if (age=="minor" || age=="senior" || isPregnant==true)
            recommendDayCaffeine = 300.0

        if (heartbeat==true && headache==true){
            recommendDayCaffeine *= 0.4
            App.prefs.multiply = 0.4f
        }
        else if (heartbeat==true || headache==true){
            recommendDayCaffeine *= 0.7
            App.prefs.multiply = 0.7f
        }else
            App.prefs.multiply = 1f

        Log.e("detailActivity", App.prefs.multiply.toString())

        if (caffeine >= recommendDayCaffeine)
            caffeine = recommendDayCaffeine
//sensetivity/dayCaffeine/onceCaffeine
        //민감도 곱하는 수치 저장!
        App.prefs.sensetivity = caffeine.toString()

        binding.tvRecommendOnceCaffeine.text = caffeine.toString() + "mg"
        binding.tvRecommendDayCaffeine.text = recommendDayCaffeine.toString() + "mg"

        App.prefs.dayCaffeine = recommendDayCaffeine.toString()
        //App.prefs.oncecaffeine = caffeine.toString()


        binding.buttonCheck.setOnClickListener {
            val selectActivity = MainActivity()
            val intent = Intent(this, selectActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}