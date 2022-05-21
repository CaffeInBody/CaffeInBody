package com.example.caffeinbody

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.caffeinbody.databinding.FragmentSurvey1Binding


class Survey1Activity  : AppCompatActivity() {
    /*lateinit */var gender: String = ""
    /*lateinit */var ispregnant: String =""

    private val binding: FragmentSurvey1Binding by lazy {
        FragmentSurvey1Binding.inflate(
            layoutInflater
        )
    }

    //사용자 정보 앱내 저장
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.buttonNext.setOnClickListener {
            val shared = getSharedPreferences("result_survey", Context.MODE_PRIVATE)
            val editor = shared.edit()//sharedpreferences 값 확인해보기
            editor.putString("age", binding.editTextSurvey1Age.text.toString())
            editor.putString("weight", binding.editTextSurvey1Weight.text.toString())
            editor.putString("gender", gender)
            editor.putString("ispregnant", ispregnant)
            //editor.apply()

            val selectActivity =  Survey2Activity()
            val intent = Intent(this, selectActivity::class.java)
            startActivity(intent)
            finish()

        }

        Log.e("id: ", binding.btnGenderLayout.getId().toString())

    }

}