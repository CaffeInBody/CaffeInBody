package com.example.caffeinbody

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.caffeinbody.SurveyResultActivity.Companion.setRecommendDayCaffeine
import com.example.caffeinbody.databinding.ActivityHeartrateBinding

class HeartrateCalculation : AppCompatActivity() {
    val tag = "HeartrateCalculation"
    private val binding: ActivityHeartrateBinding by lazy {
        ActivityHeartrateBinding.inflate(
            layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.getResultBtn.setOnClickListener {
            var normalHeartRate = binding.normalHeartrateEditText.text.toString()

            if (normalHeartRate == ""){
                Log.e(tag, "심박수 입력 안함")
            }else if(normalHeartRate.toFloat() <= 50){
                Log.e(tag, "심박수가 너무 낮음")
            }else{
                Log.e(tag, "입력한 평균 심박수: " + normalHeartRate)
                App.prefs.normalHeartRate = normalHeartRate.toFloat()
                getResult(normalHeartRate.toFloat())
            }
        }

        binding.buttonCheck.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
            finish()
        }
    }

    override fun onResume() {
        var heartrate = App.prefs.heartrateAvg
        var normalHeartrate = App.prefs.normalHeartRate.toString()
        Log.e(tag, "hi")
        if (heartrate !== null){
            binding.heartrateTV.setText(heartrate)
        }
        if (normalHeartrate !== null){
            binding.normalHeartrateEditText.setText(normalHeartrate)
        }

        super.onResume()
    }

    fun getResult(normalHeartrate: Float){
        var heartrate = App.prefs.heartrateAvg?.toFloat()
        if (heartrate == null){
            Log.e(tag, "prefs에 빈 값")
        }else{
            var heartrateGap = heartrate - normalHeartrate!!

            if (heartrateGap>0){
                heartrateGap = heartrateGap/100//ex 0.08 올랐으면 0.92 곱하기, 0.1 올랐으면 0.9곱하기-> 곱하는 값이 민감도 곱하기 전 원래 값이어야 한다
                Log.e(tag, "heartrateGap: $heartrateGap")

                var recommendDayCaffeine = setRecommendDayCaffeine(App.prefs.age!!, App.prefs.isPregnant)

                val dayCaffeine = (1- heartrateGap)* recommendDayCaffeine
                binding.tvRecommendDayCaffeine.setText(dayCaffeine.toString())
                binding.tvRecommendOnceCaffeine.setText(App.prefs.sensetivity)
                binding.resultLayout.visibility = View.VISIBLE
                App.prefs.dayCaffeine = dayCaffeine.toString()
            }else{
                val textView = TextView(this)
                textView.text = "평온한 상태에서 심박수 측정을 다시 진행해 주세요"
                binding.linearLayout6.addView(textView)
                //심박수 측정을 다시 진행해 주세요(동적으로 추가/보이게 할지/setText할지)
            }
        }
    }
    //심박수 결과 받으면(평균 심박수 입력)
}