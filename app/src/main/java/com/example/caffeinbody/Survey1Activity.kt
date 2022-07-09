package com.example.caffeinbody

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.caffeinbody.database.CafeDatas
//import com.example.caffeinbody.database.CafeDatas.Companion.addDrinksDatabaseEdiya
//import com.example.caffeinbody.database.CafeDatas.Companion.addDrinksDatabaseGongCha
//import com.example.caffeinbody.database.CafeDatas.Companion.addDrinksDatabasePaiks
//import com.example.caffeinbody.database.CafeDatas.Companion.addDrinksDatabaseTheVenti
//import com.example.caffeinbody.database.CafeDatas.Companion.addDrinksDatabaseTwosome
import com.example.caffeinbody.database.Drinks
import com.example.caffeinbody.database.DrinksDatabase
import com.example.caffeinbody.databinding.FragmentSurvey1Binding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.NullPointerException

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

        //첫 번째 설문조사 때만 저장하자..!(or 한 번 실행 후 함수 주석처리해아함)
        db = DrinksDatabase.getInstance(applicationContext)!!
        //var datas = CafeDatas()
        addDrinksDatabaseStarbucks(db)
        addDrinksDatabaseHalis(db)
        /*addDrinksDatabaseTwosome(db)
        addDrinksDatabaseEdiya(db)
        addDrinksDatabasePaiks(db)
        addDrinksDatabaseTheVenti(db)
        addDrinksDatabaseGongCha(db)*/

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

    fun addDrinksDatabaseStarbucks(db: DrinksDatabase){
        CoroutineScope(Dispatchers.IO).launch{
            db.drinksDao().insertAll(
                Drinks("롤린 민트 초코 콜드 브루", 355, 131, "콜드브루커피", "스타벅스", "", true),
                Drinks("나이트로 바닐라 크림", 355, 232, "콜드브루커피", "스타벅스", "", true),
                Drinks("나이트로 콜드 브루", 355, 245, "콜드브루커피", "스타벅스", "", true),
                Drinks("돌체 콜드 브루", 355, 150, "콜드브루커피", "스타벅스", "", true),
                Drinks("바닐라 크림 콜드 브루", 355, 150, "콜드브루커피", "스타벅스", "", true),
                Drinks("벨벳 다크 모카 나이트로", 355, 190, "콜드브루커피", "스타벅스", "", true),
                Drinks("시그니처 더 블랙 콜드 브루", 500, 680, "콜드브루커피", "스타벅스", "", true),
                Drinks("제주 비자림 콜드 브루", 473, 105, "콜드브루커피", "스타벅스", "", true),
                Drinks("콜드 브루", 355, 150, "콜드브루커피", "스타벅스", "", true),
                Drinks("콜드 브루 몰트", 355, 190, "콜드브루커피", "스타벅스", "", true),
                Drinks("콜드 브루 오트 라떼", 355, 65, "콜드브루커피", "스타벅스", "", true),
                Drinks("콜드 브루 플로트", 355, 190, "콜드브루커피", "스타벅스", "", true),
                Drinks("프렌치 애플 타르트 나이트로", 355, 190, "콜드브루커피", "스타벅스", "", true),
                Drinks("아이스 커피", 355, 140, "브루드 커피", "스타벅스", "", true),
                Drinks("오늘의 커피", 355, 260, "브루드 커피", "스타벅스", "", true),
                Drinks("에스프레소 콘 파나", 22, 75, "에스프레소", "스타벅스", "", true),
                Drinks("에스프레소 마키아또", 22, 75, "에스프레소", "스타벅스", "", true),
                Drinks("아이스 카페 아메리카노", 355, 150, "에스프레소", "스타벅스", "", true),
                Drinks("카페 아메리카노", 355, 150, "에스프레소", "스타벅스", "", true),
                Drinks("아이스 카라멜 마키아또", 355, 75, "에스프레소", "스타벅스", "", true),
                Drinks("카라멜 마키아또", 355, 75, "에스프레소", "스타벅스", "", true),
                Drinks("아이스 카푸치노", 355, 75, "에스프레소", "스타벅스", "", true),
                Drinks("카푸치노", 355, 75, "에스프레소", "스타벅스", "", true),
                Drinks("럼 샷 코르타도", 273, 160, "에스프레소", "스타벅스", "", true),
                Drinks("바닐라 빈 라떼", 355, 210, "에스프레소", "스타벅스", "", true),
                Drinks("사케라또 비안코 오버 아이스", 355, 315, "에스프레소", "스타벅스", "", true),
                Drinks("스타벅스 돌체 라떼", 355, 150, "에스프레소", "스타벅스", "", true),
                Drinks("아이스 바닐라 빈 라떼", 355, 210, "에스프레소", "스타벅스", "", true),
                Drinks("아이스 스타벅스 돌체 라떼", 355, 150, "에스프레소", "스타벅스", "", true),
                Drinks("아이스 카페 라떼", 355, 75, "에스프레소", "스타벅스", "", true),
                Drinks("카페 라떼", 355, 75, "에스프레소", "스타벅스", "", true),
                Drinks("아이스 카페 모카", 355, 95, "에스프레소", "스타벅스", "", true),
                Drinks("아이스 화이트 초콜릿 모카", 355, 75, "에스프레소", "스타벅스", "", true),
                Drinks("카페 모카", 355, 95, "에스프레소", "스타벅스", "", true),
                Drinks("화이트 초콜릿 모카", 355, 75, "에스프레소", "스타벅스", "", true),
                Drinks("바닐라 플랫 화이트", 355, 260, "에스프레소", "스타벅스", "", true),
                Drinks("바닐라 스타벅스 더블 샷", 207, 150, "에스프레소", "스타벅스", "", true),
                Drinks("블론드 바닐라 더블 샷 마키아또", 355, 170, "에스프레소", "스타벅스", "", true),
                Drinks("사케라또 아포가토", 355, 210, "에스프레소", "스타벅스", "", true),
                Drinks("스파클링 시트러스 에스프레소", 355, 105, "에스프레소", "스타벅스", "", true),
                Drinks("아이스 블론드 바닐라 더블 샷 마키아또", 355, 170, "에스프레소", "스타벅스", "", true),
                Drinks("에스프레소", 22, 75, "에스프레소", "스타벅스", "", true),
                Drinks("커피 스타벅스 더블 샷", 207, 150, "에스프레소", "스타벅스", "", true),
                Drinks("클래식 아포가토", 355, 210, "에스프레소", "스타벅스", "", true),
                Drinks("헤이즐넛 스타벅스 더블 샷", 207, 150, "에스프레소", "스타벅스", "", true),
                Drinks("더블 에스프레소 칩 프라푸치노", 355, 130, "프라푸치노", "스타벅스", "", true),
                Drinks("모카 프라푸치노", 355, 90, "프라푸치노", "스타벅스", "", true),
                Drinks("에스프레소 프라푸치노", 355, 120, "프라푸치노", "스타벅스", "", true),
                Drinks("자바 칩 프라푸치노", 355, 100, "프라푸치노", "스타벅스", "", true),
                Drinks("카라멜 프라푸치노", 355, 85, "프라푸치노", "스타벅스", "", true),
                Drinks("화이트 초콜릿 모카 프라푸치노", 355, 85, "프라푸치노", "스타벅스", "", true),
                Drinks("제주 유기농 말차로 만든 크림 프라푸치노", 355, 60, "프라푸치노", "스타벅스", "", true),
                Drinks("초콜릿 크림 칩 프라푸치노", 355, 10, "프라푸치노", "스타벅스", "", true),
                Drinks("레드 파워 스매시 블렌디드", 473, 17, "블렌디드", "스타벅스", "", true),
                Drinks("망고 패션 프루트 블렌디드", 355, 35, "블렌디드", "스타벅스", "", true),
                Drinks("블랙 티 레모네이드 피지오", 355, 30, " 스타벅스 피지오", "스타벅스", "", true),
                Drinks("쿨 라임 피지오", 355, 110, " 스타벅스 피지오", "스타벅스", "", true),
                Drinks("포멜로 플로우 그린티", 355, 3, "티(티바나)", "스타벅스", "", true),
                Drinks("별궁 오미자 유스베리 티", 473, 25, "티(티바나)", "스타벅스", "", true),
                Drinks("아이스 얼 그레이 티", 355, 50, "티(티바나)", "스타벅스", "", true),
                Drinks("아이스 유스베리 티", 355, 20, "티(티바나)", "스타벅스", "", true),
                Drinks("아이스 잉글리쉬 브렉퍼스트 티", 355, 40, "티(티바나)", "스타벅스", "", true),
                Drinks("아이스 제주 유기 녹차", 355, 16, "티(티바나)", "스타벅스", "", true),
                Drinks("얼 그레이 티", 355, 70, "티(티바나)", "스타벅스", "", true),
                Drinks("유스베리 티", 355, 20, "티(티바나)", "스타벅스", "", true),
                Drinks("잉글리쉬 브렉퍼스트 티", 355, 70, "티(티바나)", "스타벅스", "", true),
                Drinks("자몽 허니 블랙 티", 355, 70, "티(티바나)", "스타벅스", "", true),
                Drinks("제주 유기 녹차", 355, 16, "티(티바나)", "스타벅스", "", true),
                Drinks("아이스 별궁 오미자 유스베리 티", 473, 15, "티(티바나)", "스타벅스", "", true),
                Drinks("아이스 자몽 허니 블랙 티", 355, 30, "티(티바나)", "스타벅스", "", true),
                Drinks("제주 키위 오션 그린티", 473, 10, "티(티바나)", "스타벅스", "", true),
                Drinks("돌체 블랙 밀크 티", 355, 60, "티(티바나)", "스타벅스", "", true),
                Drinks("아이스 돌체 블랙 밀크 티", 355, 35, "티(티바나)", "스타벅스", "", true),
                Drinks("아이스 제주 유기농 말차로 만든 라떼", 355, 60, "티(티바나)", "스타벅스", "", true),
                Drinks("아이스 차이 티 라떼", 355, 70, "티(티바나)", "스타벅스", "", true),
                Drinks("아이스 허니 얼 그레이 밀크 티", 473, 52, "티(티바나)", "스타벅스", "", true),
                Drinks("제주 유기농 말차로 만든 라떼", 355, 60, "티(티바나)", "스타벅스", "", true),
                Drinks("차이 티 라떼", 355, 70, "티(티바나)", "스타벅스", "", true),
                Drinks("허니 얼 그레이 밀크 티", 473, 70, "티(티바나)", "스타벅스", "", true),
                Drinks("레드 파워 스매셔", 473, 25, "기타 제조 음료", "스타벅스", "", true),
                Drinks("시그니처 핫 초콜릿", 355, 15, "기타 제조 음료", "스타벅스", "", true),
                Drinks("아이스 시그니처 초콜릿", 355, 15, "기타 제조 음료", "스타벅스", "", true)
            )
            Log.e("testRoom", "스타벅스 starbucks")
        }
    }

    fun addDrinksDatabaseHalis(db: DrinksDatabase){
        CoroutineScope(Dispatchers.IO).launch{
            db.drinksDao().insertAll(
                Drinks("디카페인 아메리카노", 354, 7, "커피", "할리스", "", true),
                Drinks("디카페인 카페라떼", 354, 7, "커피", "할리스", "", true),
                Drinks("디카페인 바닐라 딜라이트", 354, 20, "커피", "할리스", "", true),
                Drinks("프리미엄 블렌드 아메리카노", 354, 233, "커피", "할리스", "", true),
                Drinks("콜드브루 딜라이트", 354, 148, "커피", "할리스", "", true),
                Drinks("더블샷 바닐라 딜라이트", 354, 169, "커피", "할리스", "", true),
                Drinks("프리미엄 블렌드 딥라떼", 354, 111, "커피", "할리스", "", true),
                Drinks("바닐라 딜라이트", 354, 80, "커피", "할리스", "", true),
                Drinks("리스트레또 딜라이트", 354, 93, "커피", "할리스", "", true),
                Drinks("콜드브루 라떼", 354, 148, "커피", "할리스", "", true),
                Drinks("콜드브루", 354, 137, "커피", "할리스", "", true),
                Drinks("카라멜 마키아또", 354, 127, "커피", "할리스", "", true),
                Drinks("카페모카", 354, 132, "커피", "할리스", "", true),
                Drinks("카푸치노", 354, 127, "커피", "할리스", "", true),
                Drinks("카페라떼", 354, 127, "커피", "할리스", "", true),
                Drinks("아메리카노", 354, 114, "커피", "할리스", "", true),
                Drinks("아포가토", 150, 93, "커피", "할리스", "", true),
                Drinks("에스프레소", 25, 61, "커피", "할리스", "", true),
                Drinks("민트초코", 354, 1, "라떼·초콜릿·티", "할리스", "", true),
                Drinks("그린티라떼", 354, 50, "라떼·초콜릿·티", "할리스", "", true),
                Drinks("밀크티라떼", 354, 72, "라떼·초콜릿·티", "할리스", "", true),
                Drinks("핫초코", 354, 8, "라떼·초콜릿·티", "할리스", "", true),
                Drinks("화이트초코", 354, 23, "라떼·초콜릿·티", "할리스", "", true),
                Drinks("복숭아 얼그레이", 354, 33, "라떼·초콜릿·티", "할리스", "", true),
                Drinks("해남 녹차", 354, 25, "라떼·초콜릿·티", "할리스", "", true),
                Drinks("얼그레이", 354, 17, "라떼·초콜릿·티", "할리스", "", true),
                Drinks("민트 초코칩 할리치노", 354, 1, "할리치노·빙수", "할리스", "", true),
                Drinks("그린티 할리치노", 354, 99, "할리치노·빙수", "할리스", "", true),
                Drinks("다크초코칩 할리치노", 354, 59, "할리치노·빙수", "할리스", "", true),
                Drinks("콜드브루 할리치노", 354, 74, "할리치노·빙수", "할리스", "", true),
                Drinks("데이드림 HOT", 283, 235, "MD식품", "할리스", "", true),
                Drinks("데이드림 ICED", 340, 177, "MD식품", "할리스", "", true),
                Drinks("이클립스 HOT", 283, 235, "MD식품", "할리스", "", true),
                Drinks("이클립스 ICED", 340, 177, "MD식품", "할리스", "", true),
                Drinks("과테말라 싱글오리진 HOT", 283, 237, "MD식품", "할리스", "", true),
                Drinks("과테말라 싱글오리진 ICED", 340, 151, "MD식품", "할리스", "", true),

                )
        }
    }

}