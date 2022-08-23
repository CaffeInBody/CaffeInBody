package com.example.caffeinbody

//import com.example.caffeinbody.database.CafeDatas.Companion.addDrinksDatabaseEdiya
//import com.example.caffeinbody.database.CafeDatas.Companion.addDrinksDatabaseGongCha
//import com.example.caffeinbody.database.CafeDatas.Companion.addDrinksDatabasePaiks
//import com.example.caffeinbody.database.CafeDatas.Companion.addDrinksDatabaseTheVenti
//import com.example.caffeinbody.database.CafeDatas.Companion.addDrinksDatabaseTwosome

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.caffeinbody.database.DrinksDatabase
import com.example.caffeinbody.database.Functions
import com.example.caffeinbody.databinding.FragmentSurvey1Binding


class Survey1Activity  : AppCompatActivity() {
    /*lateinit */var gender: String = "female"
    /*lateinit */var ispregnant: Boolean = false
    var age:String = "minor"
    var weight = 0.0
    var caffeine = 0.0
    var coefficient = 0.0

    private lateinit var db: DrinksDatabase
    //val cafeDatas = CafeDatas


    private val binding: FragmentSurvey1Binding by lazy {
        FragmentSurvey1Binding.inflate(
            layoutInflater
        )
    }

    //사용자 정보 앱내 저장
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if (App.prefs.sensetivity != null && App.prefs.dayCaffeine != null) else{
            initialDataSet()
        }

        binding.btnAgeLayout.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId) {
                R.id.btn_age_minor -> {age = "minor"; Log.e("tag", "minor")}
                R.id.btn_age_adult -> {age = "adult"; Log.e("tag", "adult")}
                R.id.btn_age_senior -> {age = "senior"; Log.e("tag", "senior")}
            }
        }

        binding.btnGenderLayout.setOnCheckedChangeListener{ group, checkedId ->
            when(checkedId) {
                R.id.button_male -> gender = "male"
                R.id.button_female -> gender = "female"
            }
        }

        binding.btnPregLayout.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId) {
                R.id.button_pregnant -> {ispregnant = true }
                R.id.button_notPregnant -> {ispregnant = false}
            }
        }

        binding.buttonNext.setOnClickListener {
            val shared = getSharedPreferences("result_survey", Context.MODE_PRIVATE)
            val editor = shared.edit()//sharedpreferences 값 확인해보기

            weight = binding.editTextSurvey1Weight.text.toString().toDouble()

            when(age){
                "minor" -> coefficient = 2.5
                "adult" -> coefficient = 3.0
                "senior" -> coefficient = 2.5
            }

            caffeine = weight * coefficient

            editor.putString("age", age)
            editor.putString("weight", weight.toString())
            editor.putString("gender", gender)
            editor.putBoolean("ispregnant", ispregnant)
            //editor.apply()

            App.prefs.age = age
            App.prefs.isPregnant = ispregnant

            val selectActivity = Survey2Activity()
            val intent = Intent(this, selectActivity::class.java)
            intent.putExtra("caffeine", caffeine)
            startActivity(intent)

            Log.e("tag", "나이: $age, 무게: $weight, 성별: $gender, 임신여부: $ispregnant, 추천카페인: $caffeine")
        }

        Log.e("id: ", binding.btnGenderLayout.getId().toString())
    }

    fun initialDataSet(){
        db = DrinksDatabase.getInstance(applicationContext)!!
        var funcs = Functions()

        funcs.addDrinksDatabaseStarbucks(db)
        funcs.addDrinksDatabaseHollys(db)
        funcs.addDrinksDatabaseTwosome(db)
        funcs.addDrinksDatabaseEdiya(db)
        funcs.addDrinksDatabasePaiks(db)
        funcs.addDrinksDatabaseTheVenti(db)
        funcs.addDrinksDatabaseGongCha(db)
        funcs.addEtc(db)/**/
        Log.e("survey1", "끝남 한글임")
    }



}