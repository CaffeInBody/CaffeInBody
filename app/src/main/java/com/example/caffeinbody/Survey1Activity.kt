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

        binding.buttonFemale.setOnClickListener(ClickListener())
        binding.buttonMale.setOnClickListener(ClickListener())
        binding.buttonPregnant.setOnClickListener(ClickListener())
        binding.buttonNotPregnant.setOnClickListener(ClickListener())
        Log.e("id: ", binding.btnGenderLayout.getId().toString())

    }

    inner class ClickListener: View.OnClickListener {
            override fun onClick(v:View?){
                if (v != null) {
                    when(v.getId()){
                        binding.buttonFemale.getId() -> {gender = "여자"
                            binding.buttonFemale.isSelected = true
                            binding.buttonMale.isSelected = false
                        }
                        binding.buttonMale.getId() -> {gender =" 남자"
                            binding.buttonFemale.isSelected = false
                            binding.buttonMale.isSelected = true
                        }
                        binding.buttonPregnant.getId()-> {ispregnant="True"
                            binding.buttonPregnant.isSelected = true
                            binding.buttonNotPregnant.isSelected = false
                            }
                        binding.buttonNotPregnant.getId()-> {ispregnant="False"
                            binding.buttonPregnant.isSelected = false
                            binding.buttonNotPregnant.isSelected = true
                        }
                        else-> Log.e("error: ", v.getId().toString() + " " + binding.buttonFemale.getId().toString())
                    }
                }else{
                    Log.e("none", binding.buttonFemale.getId().toString())
                }
            }
    }
}