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
        addDrinksDatabaseTwosome(db)
        addDrinksDatabaseEdiya(db)
        addDrinksDatabasePaiks(db)
        addDrinksDatabaseTheVenti(db)
        addDrinksDatabaseGongCha(db)
        addEtc(db)
        /*
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

    fun addDrinksDatabaseTwosome(db: DrinksDatabase) { //TODO 사이즈별 요소를 추가하는게 아니라 한 요소에 추가 사이즈가 들어가야해여
        CoroutineScope(Dispatchers.IO).launch {
            db.drinksDao().insertAll(
                Drinks("스페니쉬 연유 카페라떼(Regular)", 355, 177, "커피", "투썸플레이스", "", true),
                Drinks("스페니쉬 연유 카페라떼(Large)", 355, 265, "커피", "투썸플레이스", "", true),
                Drinks("카라멜 마키아또(Regular)", 355, 177, "커피", "투썸플레이스", "", true),
                Drinks("카라멜 마키아또(Large)", 355, 265, "커피", "투썸플레이스", "", true),
                Drinks("바닐라 카페라떼(Regular)", 355, 177, "커피", "투썸플레이스", "", true),
                Drinks("바닐라 카페라떼(Large)", 355, 265, "커피", "투썸플레이스", "", true),
                Drinks("카페 모카(Regular)", 355, 197, "커피", "투썸플레이스", "", true),
                Drinks("카페 모카(Large)", 355, 295, "커피", "투썸플레이스", "", true),
                Drinks("카페라떼(Regular)", 355, 177, "커피", "투썸플레이스", "", true),
                Drinks("카페라떼(Large)", 355, 265, "커피", "투썸플레이스", "", true),
                Drinks("카푸치노(Regular)", 355, 177, "커피", "투썸플레이스", "", true),
                Drinks("카푸치노(Large)", 355, 265, "커피", "투썸플레이스", "", true),
                Drinks("숏 카페라떼(Regular)", 355, 177, "커피", "투썸플레이스", "", true),
                Drinks("숏 카페라떼(Large)", 355, 265, "커피", "투썸플레이스", "", true),
                Drinks("아메리카노(Regular)", 355, 177, "커피", "투썸플레이스", "", true),
                Drinks("아메리카노(Large)", 355, 265, "커피", "투썸플레이스", "", true),
                Drinks("롱블랙(Regular)", 355, 177, "커피", "투썸플레이스", "", true),
                Drinks("롱블랙(Large)", 355, 265, "커피", "투썸플레이스", "", true),
                Drinks("에스프레소(Regular)", 355, 89, "커피", "투썸플레이스", "", true),
                Drinks("에스프레소(Large)", 355, 177, "커피", "투썸플레이스", "", true),
                Drinks("에스프레소 콘 파냐(Regular)", 355, 89, "커피", "투썸플레이스", "", true),
                Drinks("에스프레소 콘 파냐(Large)", 355, 177, "커피", "투썸플레이스", "", true),
                Drinks("에스프레소 마키아또(Regular)", 355, 89, "커피", "투썸플레이스", "", true),
                Drinks("에스프레소 마키아또(Large)", 355, 177, "커피", "투썸플레이스", "", true),
                Drinks("콜드브루(Regular)", 355, 196, "커피", "투썸플레이스", "", true),
                Drinks("콜드브루(Large)", 355, 265, "커피", "투썸플레이스", "", true),
                Drinks("콜드브루 라떼(Regular)", 355, 196, "커피", "투썸플레이스", "", true),
                Drinks("콜드브루 라떼(Large)", 355, 265, "커피", "투썸플레이스", "", true),
                Drinks("1837 블랙티", 355, 45, "티", "투썸플레이스", "", true),
                Drinks("프렌치 얼그레이", 355, 62, "티", "투썸플레이스", "", true),
                Drinks("그나와 민트티", 355, 30, "티", "투썸플레이스", "", true),
                Drinks("허니 레몬티", 355, 48, "티", "투썸플레이스", "", true),
                Drinks("애플 민트티", 355, 30, "티", "투썸플레이스", "", true),
                Drinks("애플 민트 아이스티", 355, 33, "티", "투썸플레이스", "", true),
                Drinks("허니 레몬 아이스티", 355, 40, "티", "투썸플레이스", "", true),
                Drinks("로얄 밀크티(Regular)", 355, 101, "티 라떼", "투썸플레이스", "", true),
                Drinks("로얄 밀크티(Large)", 355, 133, "티 라떼", "투썸플레이스", "", true),
                Drinks("그린티 라떼(Regular)", 355, 55, "티 라떼", "투썸플레이스", "", true),
                Drinks("그린티 라떼(Large)", 355, 68, "티 라떼", "투썸플레이스", "", true),
                Drinks("아이스 버블 밀크티(Regular)", 355, 78, "티 라떼", "투썸플레이스", "", true),
                Drinks("아이스 버블 밀크티(Large)", 355, 98, "티 라떼", "투썸플레이스", "", true),
                Drinks("아이스 버블 그린티 라떼(Regular)", 355, 53, "티 라떼", "투썸플레이스", "", true),
                Drinks("아이스 버블 그린티 라떼(Large)", 355, 69, "티 라떼", "투썸플레이스", "", true),
                Drinks("아이스 로얄 밀크티(Regular)", 355, 128, "티 라떼", "투썸플레이스", "", true),
                Drinks("아이스 로얄 밀크티(Large)", 355, 171, "티 라떼", "투썸플레이스", "", true),
                Drinks("아이스 그린티 라떼(Regular)", 355, 53, "티 라떼", "투썸플레이스", "", true),
                Drinks("아이스 그린티 라떼(Large)", 355, 69, "티 라떼", "투썸플레이스", "", true),
                Drinks("바닐라 아포가토", 355, 89, "아이스크림", "투썸플레이스", "", true),
                Drinks("제주 말차 프라페(Regular)", 355, 106, "아이스 블랜디드", "투썸플레이스", "", true),
                Drinks("제주 말차 프라페(Large)", 355, 135, "아이스 블랜디드", "투썸플레이스", "", true),
                Drinks("모카칩 커피 프라페(Regular)", 355, 270, "아이스 블랜디드", "투썸플레이스", "", true),
                Drinks("모카칩 커피 프라페(Large)", 355, 307, "아이스 블랜디드", "투썸플레이스", "", true),
                Drinks("카라멜 커피 프라페(Regular)", 355, 284, "아이스 블랜디드", "투썸플레이스", "", true),
                Drinks("카라멜 커피 프라페(Large)", 355, 353, "아이스 블랜디드", "투썸플레이스", "", true),
                Drinks("로얄 밀크티 쉐이크(Regular)", 355, 84, "아이스 블랜디드", "투썸플레이스", "", true),
                Drinks("로얄 밀크티 쉐이크(Large)", 355, 110, "아이스 블랜디드", "투썸플레이스", "", true),
                Drinks("초코 밀크 쉐이크", 355, 80, "아이스 블랜디드", "투썸플레이스", "", true),
                Drinks("커피 밀크 쉐이크", 355, 182, "아이스 블랜디드", "투썸플레이스", "", true),
                Drinks("초콜릿 라떼(Regular)", 355, 16, "아이스 블랜디드", "투썸플레이스", "", true),
                Drinks("초콜릿 라떼(Large)", 355, 21, "아이스 블랜디드", "투썸플레이스", "", true),
                Drinks("아이스 초콜릿 라떼(Regular)", 355, 19, "아이스 블랜디드", "투썸플레이스", "", true),
                Drinks("아이스 초콜릿 라떼(Large)", 355, 25, "아이스 블랜디드", "투썸플레이스", "", true),
                Drinks("흑임자 카페라떼(Regular)", 355, 177, "뉴트로 테이스트", "투썸플레이스", "", true),
                Drinks("흑임자 카페라떼(Large)", 355, 265, "뉴트로 테이스트", "투썸플레이스", "", true),
                Drinks("달고나 카페라떼(Regular)", 355, 50, "뉴트로 테이스트", "투썸플레이스", "", true),
                Drinks("달고나 카페라떼(Large)", 355, 60, "뉴트로 테이스트", "투썸플레이스", "", true),
                Drinks("아이스 흑임자 카페라떼(Regular)", 355, 177, "뉴트로 테이스트", "투썸플레이스", "", true),
                Drinks("아이스 흑임자 카페라떼(Large)", 355, 265, "뉴트로 테이스트", "투썸플레이스", "", true),
                Drinks("아이스 달고나 카페라떼(Regular)", 355, 58, "뉴트로 테이스트", "투썸플레이스", "", true),
                Drinks("아이스 달고나 카페라떼(Large)", 355, 70, "뉴트로 테이스트", "", "", true)
            )
        }
    }

    fun addDrinksDatabaseEdiya(db: DrinksDatabase) {
        CoroutineScope(Dispatchers.IO).launch {
            db.drinksDao().insertAll(
                Drinks("아인슈페너", 384, 103, "커피", "이디야", "", true),
                Drinks("콜드브루 아인슈페너", 384, 130, "커피", "이디야", "", true),
                Drinks("디카페인 콜드브루 아인슈페너", 384, 9, "커피", "이디야", "", true),
                Drinks("블랙모카슈페너", 384, 107, "커피", "이디야", "", true),
                Drinks("디카페인 콜드브루 아메리카노", 384, 9, "커피", "이디야", "", true),
                Drinks("디카페인 콜드브루 라뗴", 384, 9, "커피", "이디야", "", true),
                Drinks("디카페인 콜드브루 화이트 비엔나", 384, 6, "커피", "이디야", "", true),
                Drinks("디카페인 콜드브루 니트로", 384, 10, "커피", "이디야", "", true),
                Drinks("디카페인 흑당 콜드브루", 384, 6, "커피", "이디야", "", true),
                Drinks("디카페인 흑당 콜드브루(EX)", 651, 9, "커피", "이디야", "", true),
                Drinks("디카페인 연유 콜드브루", 384, 9, "커피", "이디야", "", true),
                Drinks("디카페인 콜드브루 티라미수", 384, 9, "커피", "이디야", "", true),
                Drinks("디카페인 콜드브루 크림넛", 384, 9, "커피", "이디야", "", true),
                Drinks("디카페인 버블 흑당 콜드브루", 384, 6, "커피", "이디야", "", true),
                Drinks("콜드브루 티라미수", 384, 130, "커피", "이디야", "", true),
                Drinks("콜드브루 크림넛", 384, 130, "커피", "이디야", "", true),
                Drinks("흑당 콜드브루", 384, 65, "커피", "이디야", "", true),
                Drinks("흑당 콜드브루(EX)", 651, 130, "커피", "이디야", "", true),
                Drinks("버블 흑당 콜드브루", 384, 65, "커피", "이디야", "", true),
                Drinks("버블 흑당 콜드브루(EX)", 651, 130, "커피", "이디야", "", true),
                Drinks("연유 카페라떼", 384, 103, "커피", "이디야", "", true),
                Drinks("연유 콜드브루", 384, 130, "커피", "이디야", "", true),
                Drinks("콜드브루 아메리카노", 384, 130, "커피", "이디야", "", true),
                Drinks("아포가토 오리지널", 384, 103, "커피", "이디야", "", true),
                Drinks("아포가토 콜드브루 바닐라 모카", 384, 68, "커피", "이디야", "", true),
                Drinks("콜드브루 라떼", 384, 130, "커피", "이디야", "", true),
                Drinks("콜드브루 화이트 비엔나", 384, 65, "커피", "이디야", "", true),
                Drinks("콜드브루 니트로", 384, 155, "커피", "이디야", "", true),
                Drinks("에스프레소", 384, 103, "커피", "이디야", "", true),
                Drinks("에스프레소 마끼아또", 384, 103, "커피", "이디야", "", true),
                Drinks("에스프레소 콘파냐", 384, 103, "커피", "이디야", "", true),
                Drinks("카페 아메리카노", 384, 103, "커피", "이디야", "", true),
                Drinks("카페 아메리카노(EX)", 651, 206, "커피", "이디야", "", true),
                Drinks("카페라떼", 384, 103, "커피", "이디야", "", true),
                Drinks("카페라떼", 651, 206, "커피", "이디야", "", true),
                Drinks("카푸치노", 384, 103, "커피", "이디야", "", true),
                Drinks("카페모카", 384, 99, "커피", "이디야", "", true),
                Drinks("카페모카(EX)", 651, 197, "커피", "이디야", "", true),
                Drinks("카라멜 마끼아또", 384, 91, "커피", "이디야", "", true),
                Drinks("카라멜 마끼아또(EX)", 651, 206, "커피", "이디야", "", true),
                Drinks("바닐라라떼", 384, 115, "커피", "이디야", "", true),
                Drinks("바닐라라떼(EX)", 651, 229, "커피", "이디야", "", true),
                Drinks("화이트 초콜릿모카", 384, 103, "커피", "이디야", "", true),
                Drinks("민트모카", 384, 124, "커피", "이디야", "", true),
                Drinks("크림 달고나 밀크티", 384, 18, "라떼·초콜릿·티", "이디야", "", true),
                Drinks("달고나 밀크티", 384, 18, "라떼·초콜릿·티", "이디야", "", true),
                Drinks("버블 크림 밀크티", 384, 18, "라떼·초콜릿·티", "이디야", "", true),
                Drinks("아이스 초콜릿", 384, 7, "라떼·초콜릿·티", "이디야", "", true),
                Drinks("아이스 초콜릿(EX)", 651, 14, "라떼·초콜릿·티", "이디야", "", true),
                Drinks("핫 초콜릿", 384, 7, "라떼·초콜릿·티", "이디야", "", true),
                Drinks("핫 초콜릿(EX)", 651, 14, "라떼·초콜릿·티", "이디야", "", true),
                Drinks("녹차라떼", 384, 58, "라떼·초콜릿·티", "이디야", "", true),
                Drinks("녹차라떼(EX)", 651, 113, "라떼·초콜릿·티", "이디야", "", true),
                Drinks("민트초코", 384, 21, "라떼·초콜릿·티", "이디야", "", true),
                Drinks("토피넛 라떼", 384, 19, "라떼·초콜릿·티", "이디야", "", true),
                Drinks("토피넛 라떼(EX)", 651, 27, "라떼·초콜릿·티", "이디야", "", true),
                Drinks("복숭아 아이스티 ", 384, 11, "라떼·초콜릿·티", "이디야", "", true),
                Drinks("복숭아 아이스티 (EX)", 651, 21, "라떼·초콜릿·티", "이디야", "", true),
                Drinks("레몬 아이스티", 384, 14, "라떼·초콜릿·티", "이디야", "", true),
                Drinks("레몬 아이스티(EX)", 651, 28, "라떼·초콜릿·티", "이디야", "", true),
                Drinks("얼그레이 홍차", 384, 25, "라떼·초콜릿·티", "이디야", "", true),
                Drinks("로즈 자스민 티", 384, 25, "라떼·초콜릿·티", "이디야", "", true),
                Drinks("밀크티", 384, 56, "라떼·초콜릿·티", "이디야", "", true),
                Drinks("커피 플랫치노", 384, 103, "플랫치노", "이디야", "", true),
                Drinks("모카 플랫치노", 384, 107, "플랫치노", "이디야", "", true),
                Drinks("카라멜 플랫치노", 384, 103, "플랫치노", "이디야", "", true),
                Drinks("녹차 플랫치노", 384, 58, "플랫치노", "이디야", "", true),
                Drinks("초콜릿 칩 플랫치노", 384, 35, "플랫치노", "이디야", "", true),
                Drinks("민트 초콜릿 칩 플랫치노", 384, 44, "플랫치노", "이디야", "", true)
            )
        }
    }

    fun addDrinksDatabasePaiks(db: DrinksDatabase) {
        CoroutineScope(Dispatchers.IO).launch {
            db.drinksDao().insertAll(
                Drinks("디카페인 콜드브루라떼", 600, 15, "커피", "빽다방", "", true),
                Drinks("더블에스프레소", 60, 237, "커피", "빽다방", "", true),
                Drinks("앗!메리카노(hot)", 400, 237, "커피", "빽다방", "", true),
                Drinks("앗!메리카노(iced)", 625, 237, "커피", "빽다방", "", true),
                Drinks("원조커피(hot)", 375, 390, "커피", "빽다방", "", true),
                Drinks("원조커피(iced)", 625, 390, "커피", "빽다방", "", true),
                Drinks("달달연유라떼(hot)", 425, 237, "커피", "빽다방", "", true),
                Drinks("달달연유라떼(iced)", 600, 237, "커피", "빽다방", "", true),
                Drinks("빽's 카페라떼(hot)", 300, 237, "커피", "빽다방", "", true),
                Drinks("빽's 카페라떼(iced)", 625, 237, "커피", "빽다방", "", true),
                Drinks("블랙펄카페라떼(iced)", 634, 118, "커피", "빽다방", "", true),
                Drinks("바닐라라떼(hot)", 325, 237, "커피", "빽다방", "", true),
                Drinks("바닐라라떼(iced)", 600, 237, "커피", "빽다방", "", true),
                Drinks("카페모카(hot)", 350, 237, "커피", "빽다방", "", true),
                Drinks("카페모카(iced)", 625, 237, "커피", "빽다방", "", true),
                Drinks("카라멜마끼아또(hot)", 350, 237, "커피", "빽다방", "", true),
                Drinks("카라멜마끼아또(iced)", 625, 237, "커피", "빽다방", "", true),
                Drinks("코코넛카페라떼(HOT)", 335, 237, "커피", "빽다방", "", true),
                Drinks("코코넛카페라떼(ICED)", 615, 237, "커피", "빽다방", "", true),
                Drinks("코코넛커피스무디(ICED)", 549, 237, "커피", "빽다방", "", true),
                Drinks("아이스크림카페라떼(ICED)", 650, 237, "커피", "빽다방", "", true),
                Drinks("아이스크림바닐라라떼(ICED)", 570, 237, "커피", "빽다방", "", true),
                Drinks("아이스크림카페모카(ICED)", 570, 237, "커피", "빽다방", "", true),
                Drinks("콜드브루라떼", 600, 195, "커피", "빽다방", "", true),
                Drinks("디카페인 콜드브루라떼", 600, 15, "커피", "빽다방", "", true),
                Drinks("콜드브루", 600, 195, "커피", "빽다방", "", true),
                Drinks("디카페인 콜드브루", 600, 15, "커피", "빽다방", "", true),
                Drinks("콜드브루라떼(연유)", 600, 195, "커피", "빽다방", "", true),
                Drinks("디카페인 콜드브루라떼(연유)", 600, 15, "커피", "빽다방", "", true),
                Drinks("콜드브루라떼(흑당)", 600, 195, "커피", "빽다방", "", true),
                Drinks("디카페인 콜드브루라떼(흑당)", 600, 15, "커피", "빽다방", "", true),
                Drinks("아이스티샷추가(아샷추)", 600, 93, "커피", "빽다방", "", true),
                Drinks("빽사이즈 앗!메리카노(iced)", 950, 474, "커피", "빽다방", "", true),
                Drinks("빽사이즈 원조커피(iced)", 950, 597, "커피", "빽다방", "", true),
                Drinks("빽사이즈 빽's 카페라떼(iced)", 950, 474, "커피", "빽다방", "", true),
                Drinks("빽사이즈 아이스티샷추가(iced)", 900, 167, "커피", "빽다방", "", true),
                Drinks("녹차라떼(hot)", 400, 208, "음료", "빽다방", "", true),
                Drinks("녹차라떼(iced)", 600, 152, "음료", "빽다방", "", true),
                Drinks("토피넛라떼(hot)", 350, 51, "음료", "빽다방", "", true),
                Drinks("토피넛라떼(iced)", 650, 51, "음료", "빽다방", "", true),
                Drinks("밀크티(hot)", 375, 133, "음료", "빽다방", "", true),
                Drinks("밀크티(iced)", 625, 133, "음료", "빽다방", "", true),
                Drinks("블랙펄밀크티(iced)", 643, 77, "음료", "빽다방", "", true),
                Drinks("달콤아이스티", 600, 51, "음료", "빽다방", "", true),
                Drinks("피치우롱스위트티(hot)", 365, 65, "음료", "빽다방", "", true),
                Drinks("피치우롱스위트티(iced)", 593, 71, "음료", "빽다방", "", true),
                Drinks("우롱티(hot)", 400, 92, "음료", "빽다방", "", true),
                Drinks("우롱티(iced)", 600, 67, "음료", "빽다방", "", true),
                Drinks("레몬얼그레이티(hot)", 350, 72, "음료", "빽다방", "", true),
                Drinks("레몬얼그레이티(iced)", 600, 72, "음료", "빽다방", "", true),
                Drinks("오렌지자몽블랙티(hot)", 350, 90, "음료", "빽다방", "", true),
                Drinks("오렌지자몽블랙티(iced)", 600, 90, "음료", "빽다방", "", true),
                Drinks("빽사이즈 달콤아이스티(iced)", 950, 32, "음료", "빽다방", "", true),
                Drinks("원조빽스치노(basic)", 625, 327, "빽스치노", "빽다방", "", true),
                Drinks("원조빽스치노(soft)", 765, 327, "빽스치노", "빽다방", "", true),
                Drinks("녹차빽스치노(basic)", 625, 260, "빽스치노", "빽다방", "", true),
                Drinks("녹차빽스치노(soft)", 765, 260, "빽스치노", "빽다방", "", true),
                Drinks("쿠키크런치빽스치노(basic)", 628, 11, "빽스치노", "빽다방", "", true),
                Drinks("쿠키크런치빽스치노(soft)", 748, 11, "빽스치노", "빽다방", "", true),
                Drinks("피스타치오빽스치노(basic)", 625, 10, "빽스치노", "빽다방", "", true),
                Drinks("피스타치오빽스치노(soft)", 765, 10, "빽스치노", "빽다방", "", true),
                Drinks("퐁당치노(원조커피)", 592, 255, "빽스치노", "빽다방", "", true)
            )
        }
    }

    fun addDrinksDatabaseTheVenti(db: DrinksDatabase) {
        CoroutineScope(Dispatchers.IO).launch {
            db.drinksDao().insertAll(
                Drinks("아메리카노", 650, 215, "커피", "", "282", true),
                Drinks("(iced)아메리카노", 650, 215, "커피", "더벤티", "", true),
                Drinks("카페라떼(hot/ice)", 650, 264, "커피", "더벤티", "", true),
                Drinks("바닐라딥라떼(hot/ice)", 650, 264, "커피", "더벤티", "", true),
                Drinks("바닐라크림콜드브루", 650, 79, "커피", "더벤티", "", true),
                Drinks("디카페인바닐라크림콜드브루", 650, 1, "커피", "더벤티", "", true),
                Drinks("헤이즐넛딥라떼(hot/ice)", 650, 264, "커피", "더벤티", "", true),
                Drinks("헤이즐넛크림콜드브루", 650, 79, "커피", "더벤티", "", true),
                Drinks("디카페인헤이즐넛크림콜드브루", 650, 1, "커피", "더벤티", "", true),
                Drinks("더벤티사이즈아메리카노(32oz)", 1300, 480, "커피", "더벤티", "", true),
                Drinks("더벤티사이즈카페라떼(32oz)", 1300, 528, "커피", "더벤티", "", true),
                Drinks("더벤티사이즈믹스커피(32oz)", 1300, 165, "커피", "더벤티", "", true),
                Drinks("연유라떼(hot/ice)", 650, 264, "커피", "더벤티", "", true),
                Drinks("카페모카(hot/ice)", 650, 273, "커피", "더벤티", "", true),
                Drinks("아인슈페너(hot/ice)", 325, 264, "커피", "더벤티", "", true),
                Drinks("꿀다크리카노(hot/ice)", 650, 264, "커피", "더벤티", "", true),
                Drinks("바닐라 다크리카노(hot/ice)", 650, 264, "커피", "더벤티", "", true),
                Drinks("헤이즐넛 다크리카노(hot/ice)", 650, 264, "커피", "더벤티", "", true),
                Drinks("콜드브루", 650, 92, "커피", "더벤티", "", true),
                Drinks("콜드브루라떼", 650, 92, "커피", "더벤티", "", true),
                Drinks("디카페인콜드브루", 650, 1, "커피", "더벤티", "", true),
                Drinks("디카페인콜드브루라떼", 650, 1, "커피", "더벤티", "", true),
                Drinks("믹스커피(hot/ice)", 650, 110, "커피", "더벤티", "", true),
                Drinks("토피넛라떼(hot/ice)", 650, 132, "커피", "더벤티", "", true),
                Drinks("카라멜마끼야또", 650, 264, "커피", "더벤티", "", true),
                Drinks("로얄밀크티", 650, 75, "베버리지", "더벤티", "", true),
                Drinks("녹차라떼(ice/hot)", 650, 39, "베버리지", "더벤티", "", true),
                Drinks("초코라떼(ice/hot)", 650, 14, "베버리지", "더벤티", "", true),
                Drinks("말차초코칩프라페", 650, 214, "아이스블렌디드", "더벤티", "", true),
                Drinks("딸기초코칩프라페", 650, 7, "아이스블렌디드", "더벤티", "", true),
                Drinks("민트초코칩프라페", 650, 18, "아이스블렌디드", "더벤티", "", true),
                Drinks("토피넛초코칩프라페", 650, 80, "아이스블렌디드", "더벤티", "", true),
                Drinks("코코초코프라페", 650, 28, "아이스블렌디드", "더벤티", "", true),
                Drinks("자바칩프라페", 650, 97, "아이스블렌디드", "더벤티", "", true),
                Drinks("에스프레소쉐이크", 650, 264, "아이스블렌디드", "더벤티", "", true),
                Drinks("초코쉐이크", 650, 14, "아이스블렌디드", "더벤티", "", true),
                Drinks("로얄밀크티버블티", 650, 75, "버블티/티", "더벤티", "", true),
                Drinks("복숭아아이스티", 650, 20, "버블티/티", "더벤티", "", true),
                Drinks("더벤티사이즈복숭아아이스티(32oz)", 1300, 30, "버블티/티", "더벤티", "", true)
            )
        }
    }

    fun addDrinksDatabaseGongCha(db: DrinksDatabase) {
        CoroutineScope(Dispatchers.IO).launch {
            db.drinksDao().insertAll(
                Drinks("납작복숭아블루밍크러쉬", 454, 17, "시즌메뉴", "공차", "", true),
                Drinks("납작복숭아쥬얼리밀크티", 454, 21, "시즌메뉴", "공차", "", true),
                Drinks("납작복숭아타르트크러쉬", 454, 22, "시즌메뉴", "공차", "", true),
                Drinks("그릭요거트&밀크티크러쉬", 454, 40, "시즌메뉴", "공차", "", true),
                Drinks("그릭요거트&망고밀크티크러쉬", 454, 20, "시즌메뉴", "공차", "", true),
                Drinks("그릭요거트&딸기밀크티크러쉬", 454, 17, "시즌메뉴", "공차", "", true),
                Drinks("리얼딸기쥬얼리밀크티", 454, 19, "시즌메뉴", "공차", "", true),
                Drinks("슈크림딸기밀크티", 454, 55, "시즌메뉴", "공차", "", true),
                Drinks("딸기초코쿠키스무디", 454, 17, "시즌메뉴", "공차", "", true),
                Drinks("딸기듬뿍밀크티", 454, 24, "시즌메뉴", "공차", "", true),
                Drinks("베리베리스무디", 454, 29, "시즌메뉴", "공차", "", true),
                Drinks("초당옥수수밀크티+펄", 454, 24, "시즌메뉴", "공차", "", true),
                Drinks("초당옥수수팝핑스무디", 454, 28, "시즌메뉴", "공차", "", true),
                Drinks("블랙밀크티+펄(ice)", 454, 107, "베스트콤비네이션", "공차", "", true),
                Drinks("블랙밀크티+펄(hot)", 397, 147, "베스트콤비네이션", "공차", "", true),
                Drinks("피스타치오밀크티+펄(ice)", 454, 20, "베스트콤비네이션", "공차", "", true),
                Drinks("피스타치오밀크티+펄(hot)", 397, 21, "베스트콤비네이션", "공차", "", true),
                Drinks("제주그린밀크티+펄(ice)", 454, 49, "베스트콤비네이션", "공차", "", true),
                Drinks("제주그린티밀크티+펄(hot)", 397, 62, "베스트콤비네이션", "공차", "", true),
                Drinks("청포도그린티+알로에", 454, 88, "베스트콤비네이션", "공차", "", true),
                Drinks("블랙티(ice)", 454, 125, "오리지널티", "공차", "", true),
                Drinks("블랙티(hot)", 397, 172, "오리지널티", "공차", "", true),
                Drinks("얼그레이티(ice)", 454, 156, "오리지널티", "공차", "", true),
                Drinks("얼그레이티(hot)", 397, 215, "오리지널티", "공차", "", true),
                Drinks("우롱티(ice)", 454, 119, "오리지널티", "공차", "", true),
                Drinks("우롱티(hot)", 397, 164, "오리지널티", "공차", "", true),
                Drinks("자스민그린티(ice)", 454, 103, "오리지널티", "공차", "", true),
                Drinks("자스민그린티(hot)", 397, 142, "오리지널티", "공차", "", true),
                Drinks("블랙밀크티(ice)", 454, 59, "밀크티", "공차", "", true),
                Drinks("블랙밀크티(hot)", 397, 79, "밀크티", "공차", "", true),
                Drinks("얼그레이밀크티(ice)", 454, 77, "밀크티", "공차", "", true),
                Drinks("얼그레이밀크티(hot)", 397, 104, "밀크티", "공차", "", true),
                Drinks("우롱밀크티(ice)", 454, 41, "밀크티", "공차", "", true),
                Drinks("우롱밀크티(hot)", 397, 45, "밀크티", "공차", "", true),
                Drinks("자스민그린밀크티(ice)", 454, 44, "밀크티", "공차", "", true),
                Drinks("자스민그린밀크티(hot)", 397, 59, "밀크티", "공차", "", true),
                Drinks("초콜렛밀크티(ice)", 454, 12, "밀크티", "공차", "", true),
                Drinks("초콜렛밀크티(hot)", 397, 16, "밀크티", "공차", "", true),
                Drinks("피스타치오밀크티(ice)", 454, 19, "밀크티", "공차", "", true),
                Drinks("피스타치오밀크티(hot)", 397, 25, "밀크티", "공차", "", true),
                Drinks("제주그린밀크티(ice)", 454, 49, "밀크티", "공차", "", true),
                Drinks("제주그린밀크티(hot)", 397, 62, "밀크티", "공차", "", true),
                Drinks("하동호지밀크티(ice)", 454, 167, "밀크티", "공차", "", true),
                Drinks("하동호지밀크티(hot)", 397, 172, "밀크티", "공차", "", true),
                Drinks("딸기쥬얼리밀크티", 454, 13, "쥬얼리", "공차", "", true),
                Drinks("딸기쥬얼리요구르트크러쉬", 454, 8, "쥬얼리", "공차", "", true),
                Drinks("청포도그린티+알로에", 454, 58, "과일믹스", "공차", "", true),
                Drinks("자몽그린티", 454, 29, "과일믹스", "공차", "", true),
                Drinks("딸기쿠키스무디", 454, 21, "스무디", "공차", "", true),
                Drinks("제주그린티스무디", 454, 88, "스무디", "공차", "", true),
                Drinks("청포도스무디", 454, 10, "스무디", "공차", "", true),
                Drinks("초콜렛쿠키&크림스무디", 454, 28, "스무디", "공차", "", true),
                Drinks("초코바른피스타치오스무디", 454, 21, "스무디", "공차", "", true),
                Drinks("초코바른초코스무디", 454, 15, "스무디", "공차", "", true),
                Drinks("초코바른제주그린스무디", 454, 75, "스무디", "공차", "", true),
                Drinks("돌체크러쉬위드샷", 454, 109, "스무디", "공차", "", true),
                Drinks("아메리카노(ice)", 454, 102, "커피", "공차", "", true),
                Drinks("아메리카노(hot)", 397, 119, "커피", "공차", "", true),
                Drinks("카페라떼(ice)", 454, 78, "커피", "공차", "", true),
                Drinks("카페라떼(hot)", 397, 109, "커피", "공차", "", true),
                Drinks("돌체카페라떼(ice)", 454, 93, "커피", "공차", "", true),
                Drinks("돌체카페라떼(hot)", 397, 145, "커피", "공차", "", true),
                Drinks("카라멜카페라떼(ice)", 454, 82, "커피", "공차", "", true),
                Drinks("카라멜카페라떼(hot)", 397, 131, "커피", "공차", "", true),
                Drinks("바닐라카페라떼(ice)", 454, 88, "커피", "공차", "", true),
                Drinks("바닐라카페라떼(hot)", 397, 98, "커피", "공차", "", true),
                Drinks("모카카페라떼(ice)", 454, 86, "커피", "공차", "", true),
                Drinks("모카카페라떼(hot)", 397, 126, "커피", "공차", "", true)
            )
        }
    }

    fun addEtc(db: DrinksDatabase){
        CoroutineScope(Dispatchers.IO).launch {
            db.drinksDao().insertAll(
                Drinks("흑당카페라떼", 250, 75, "커피 및 커피우유", "푸르밀", "url", false),
                Drinks("카페베네마리아바닐라라떼", 300, 90, "커피 및 커피우유", "푸르밀", "url", false),
                Drinks("카페베네마리아스모키라떼", 300, 160, "커피 및 커피우유", "푸르밀", "url", false),
                Drinks("아카페라카라멜마끼아또", 240, 70, "커피 및 커피우유", "빙그레", "url", false),
                Drinks("아카페라바닐라라떼", 240, 65, "커피 및 커피우유", "빙그레", "url", false),
                Drinks("아카페라사이즈업바닐라라떼", 350, 90, "커피 및 커피우유", "빙그레", "url", false),
                Drinks("아카페라사이즈업아메리카노", 350, 70, "커피 및 커피우유", "빙그레", "url", false),
                Drinks("아카페라사이즈업카페라떼", 350, 90, "커피 및 커피우유", "빙그레", "url", false),
                Drinks("스페셜티토스티드카라멜마끼아또", 300, 100, "커피 및 커피우유", "빙그레", "url", false),
                Drinks("스페셜티멜로우바닐라라떼", 300, 100, "커피 및 커피우유", "빙그레", "url", false),
                Drinks("스페셜티에티오피아예가체프", 460, 190, "커피 및 커피우유", "빙그레", "url", false),
                Drinks("스페셜티탄자니아킬리만자로", 460, 200, "커피 및 커피우유", "빙그레", "url", false),
                Drinks("오늘의커피바닐라라떼", 250, 65, "커피 및 커피우유", "빙그레", "url", false),
                Drinks("카페라떼카라멜마끼야또", 220, 100, "커피 및 커피우유", "매일", "url", false),
                Drinks("카페라떼마일드", 220, 95, "커피 및 커피우유", "매일", "url", false),
                Drinks("카페라떼마일드로어슈거", 220, 95, "커피 및 커피우유", "매일", "url", false),
                Drinks("바리스타플라넬드립라떼", 325, 145, "커피 및 커피우유", "매일", "url", false),
                Drinks("바리스타마다가스카르바닐라빈라떼", 325, 105, "커피 및 커피우유", "매일", "url", false),
                Drinks("바리스타콜드브루블랙", 325, 165, "커피 및 커피우유", "매일", "url", false),
                Drinks("바리스타벨지엄쇼콜라모카", 325, 125, "커피 및 커피우유", "매일", "url", false),
                Drinks("바리스타스모키로스팅라떼", 250, 112, "커피 및 커피우유", "매일", "url", false),
                Drinks("바리스타모카프레소", 250, 95, "커피 및 커피우유", "매일", "url", false),
                Drinks("바리스타카라멜딥프레소", 250, 100, "커피 및 커피우유", "매일", "url", false),
                Drinks("바리스타에스프레소라떼", 250, 112, "커피 및 커피우유", "매일", "url", false),
                Drinks("바리스타로어슈거에스프레소라떼", 250, 105, "커피 및 커피우유", "매일", "url", false),
                Drinks("바리스타그란데라떼", 475, 150, "커피 및 커피우유", "매일", "url", false),
                Drinks("바리스타그란데아메리카노", 475, 190, "커피 및 커피우유", "매일", "url", false),
                Drinks("프렌치카페에스프레소라떼", 250, 110, "커피 및 커피우유", "남양", "url", false),
                Drinks("프렌치카페옐로우비번돌체라떼", 250, 105, "커피 및 커피우유", "남양", "url", false),
                Drinks("프렌치카페블랙글레이즈드라떼", 250, 95, "커피 및 커피우유", "남양", "url", false),
                Drinks("프렌치카페로스터리킬링샷라떼", 500, 130, "커피 및 커피우유", "남양", "url", false),
                Drinks("스타벅스라떼", 200, 92, "커피 및 커피우유", "서울우유", "url", false),
                Drinks("강릉커피라떼", 250, 142, "커피 및 커피우유", "서울우유", "url", false),
                Drinks("스페셜티카페라떼마일드", 250, 117, "커피 및 커피우유", "서울우유", "url", false),
                Drinks("스페셜티카페라떼모카", 250, 110, "커피 및 커피우유", "서울우유", "url", false),
                Drinks("커피빈아이스커피", 1000, 606, "커피 및 커피우유", "서울우유", "url", false),
                Drinks("오리진돌체라떼", 250, 120, "커피 및 커피우유", "덴마크", "url", false),
                Drinks("오리진카페라떼", 250, 130, "커피 및 커피우유", "덴마크", "url", false),
                Drinks("프라푸치노모카", 281, 82, "커피 및 커피우유", "스타벅스", "url", false),
                Drinks("레쓰비", 175, 74, "커피 및 커피우유", "롯데칠성", "url", false),
                Drinks("레쓰비그란데아메리카노", 500, 204, "커피 및 커피우유", "롯데칠성", "url", false),
                Drinks("레쓰비그란데라떼", 500, 190, "커피 및 커피우유", "롯데칠성", "url", false),
                Drinks("칸타타콘트라베이스블랙", 500, 179, "커피 및 커피우유", "롯데칠성", "url", false),
                Drinks("칸타타콘트라베이스콜드브루라떼", 500, 165, "커피 및 커피우유", "롯데칠성", "url", false),
                Drinks("칸타타프리미엄라떼", 275, 156, "커피 및 커피우유", "롯데칠성", "url", false),
                Drinks("칸타타스위트아메리카노", 275, 112, "커피 및 커피우유", "롯데칠성", "url", false),
                Drinks("칸타타라떼홀릭바닐라라떼", 240, 95, "커피 및 커피우유", "롯데칠성", "url", false),
                Drinks("칸타타라떼홀릭카페라떼", 240, 138, "커피 및 커피우유", "롯데칠성", "url", false),
                Drinks("조지아고티카빈티지라떼", 270, 109, "커피 및 커피우유", "코카콜라", "url", false),
                Drinks("조지아크래프트블랙", 470, 205, "커피 및 커피우유", "코카콜라", "url", false),
                Drinks("조지아크래프트카페라떼", 470, 229, "커피 및 커피우유", "코카콜라", "url", false),
                Drinks("조지아라떼니스타크리미라떼", 280, 110, "커피 및 커피우유", "코카콜라", "url", false),
                Drinks("조지아라떼니스타카라멜라떼", 280, 130, "커피 및 커피우유", "코카콜라", "url", false),
                Drinks("맥스웰콜롬비아스위트블랙", 500, 157, "커피 및 커피우유", "동서", "url", false),
                Drinks("맥스웰하우스마스터블랙", 500, 157, "커피 및 커피우유", "동서", "url", false),
                Drinks("맥심TOP스위트아메리카노", 275, 96, "커피 및 커피우유", "동서", "url", false),
                Drinks("맥심TOP더블랙", 275, 96, "커피 및 커피우유", "동서", "url", false),
                Drinks("맥심TOP마스터라떼", 275, 83, "커피 및 커피우유", "동서", "url", false),
                Drinks("맥심TOP심플리스무스스위트아메리카노", 240, 98, "커피 및 커피우유", "동서", "url", false),
                Drinks("맥심TOP심플리스무스블랙", 240, 98, "커피 및 커피우유", "동서", "url", false),
                Drinks("맥심TOP심플리스무스라떼", 240, 112, "커피 및 커피우유", "동서", "url", false),
                Drinks("스타벅스스키니라떼", 270, 139, "커피 및 커피우유", "동서", "url", false),
                Drinks("스타벅스카페라떼", 270, 124, "커피 및 커피우유", "동서", "url", false),
                Drinks("스타벅스카페라떼", 200, 92, "커피 및 커피우유", "동서", "url", false),
                Drinks("시그니처스위트아메리카노", 390, 150, "커피 및 커피우유", "홈플러스", "url", false),
                Drinks("시그니처카페라떼", 390, 142, "커피 및 커피우유", "홈플러스", "url", false),
                Drinks("시그니처블랙", 390, 136, "커피 및 커피우유", "홈플러스", "url", false),
                Drinks("카페라떼", 300, 280, "커피 및 커피우유", "이디야", "url", false),
                Drinks("토피넛시그니처라떼", 300, 210, "커피 및 커피우유", "이디야", "url", false),
                Drinks("돌체콜드브루", 300, 230, "커피 및 커피우유", "이디야", "url", false),
                Drinks("바닐라라떼", 300, 220, "커피 및 커피우유", "이디야", "url", false),
                Drinks("쇼콜라모카", 300, 225, "커피 및 커피우유", "이디야", "url", false),
                Drinks("85도씨소금커피", 300, 96, "커피 및 커피우유", "대만", "url", false),
                Drinks("오리진돌체라떼", 250, 120, "커피 및 커피우유", "동원", "url", false),
                Drinks("오리진카페라떼", 250, 130, "커피 및 커피우유", "동원", "url", false),
                Drinks("오리진바닐라라떼", 250, 170, "커피 및 커피우유", "동원", "url", false),
                Drinks("오리진모카라떼", 250, 150, "커피 및 커피우유", "동원", "url", false),
                Drinks("오리진아메리카노", 250, 180, "커피 및 커피우유", "동원", "url", false),
                Drinks("커피아이스아메리카노", 1000, 510, "커피 및 커피우유", "씨유", "url", false),
                Drinks("커피믹스커피", 1000, 380, "커피 및 커피우유", "씨유", "url", false),
                Drinks("커피", 500, 130, "커피 및 커피우유", "씨유", "url", false),
                Drinks("폴바셋돌체라떼", 330, 185, "커피 및 커피우유", "엠즈", "url", false),

                Drinks("게보린정", 0, 50, "해열·진통제", "삼진제약", "url", false),
                Drinks("펜잘큐정", 0, 50, "해열·진통제", "종근당", "url", false),
                Drinks("그날엔정", 0, 40, "해열·진통제", "정동제약", "url", false),
                Drinks("판피린큐액", 20, 30, "해열·진통제", "동아제약", "url", false),
                Drinks("판콜에스내복액", 0, 30, "해열·진통제", "동화약품", "url", false),

                Drinks("핫식스(오리지널, 자몽)", 250, 60, "에너지드링크", "롯데칠성", "url", false),
                Drinks("핫식스(오리지널,더킹펀치, 더킹포스, 더킹파워, 더킹스톰, 더킹러쉬)", 355, 86, "에너지드링크", "롯데칠성", "url", false),
                Drinks("핫식스 바이탈 에너지/무브업 에너지", 500, 120, "에너지드링크", "롯데칠성", "url", false),
                Drinks("레드불", 250, 62, "에너지드링크", "유한회사", "url", false),
                Drinks("몬스터(울트라/그린/시트라/펀치/파라다이스/망고)", 355, 100, "에너지드링크", "코카콜라", "url", false),
                Drinks("말표 (다크홀스/화이트)", 250, 100, "에너지드링크", "말표산업", "url", false),
                Drinks("메가포스 과라나(단종)", 250, 41, "에너지드링크", "", "url", false),
                Drinks("타브카(오리지널)", 355, 104, "에너지드링크", "오케이에프음료", "url", false),
                Drinks("타브카(버스트)", 355, 103, "에너지드링크", "오케이에프음료", "url", false),
                Drinks("에너지티", 5, 70, "에너지드링크", "티젠", "url", false),
                Drinks("백셀 에너지 드링크", 250, 60, "에너지드링크", "동화약품", "url", false),
                Drinks("지파크", 250, 60, "에너지드링크", "동화약품", "url", false),
                Drinks("빡텐션", 13, 175, "에너지드링크", "희망그린식품", "url", false),
                Drinks("야[약국용]", 100, 150, "에너지드링크", "삼성제약", "url", false),
                Drinks("야[편의점]", 250, 150, "에너지드링크", "삼성제약", "url", false),
                Drinks("야[하버드]", 100, 175, "에너지드링크", "삼성제약", "url", false),
                Drinks("번인텐스(블랙)(단종)", 250, 80, "에너지드링크", "코카콜라", "url", false),
                Drinks("번인텐스(파랑)(단종)", 250, 30, "에너지드링크", "코카콜라", "url", false),
                Drinks("닥터페퍼", 354, 41, "콜라", "코카콜라", "url", false),
                Drinks("김치 에너지 음료", 240, 70, "에너지드링크", "금강B&F", "url", false),
                Drinks("박카스f/d", 120, 30, "자양강장제", "동아제약", "url", false),
                Drinks("구론산 바몬드/스파클링", 150, 30, "자양강장제", "해태htb", "url", false),
                Drinks("오로나민C", 120, 14, "자양강장제", "동아오츠카", "url", false),
                Drinks("그린티", 100, 100, "아이스크림", "하겐다즈", "url", false),
                Drinks("그린티", 100, 73, "아이스크림", "배스킨라빈스", "url", false),
                Drinks("끌레도르 심플리 퓨어 그린티", 100, 59, "아이스크림", "빙그레", "url", false),
                Drinks("나뚜루 그린티비너스", 100, 59, "아이스크림", "롯데제과", "url", false),
                Drinks("달고나커피우유", 500, 37, "커피우유 및 기타음료", "연세대학교", "url", false),
                Drinks("스누피 더진한커피우유", 500, 237, "커피우유 및 기타음료", "GS25", "url", false),
                Drinks("커피맛우유", 240, 85, "커피우유 및 기타음료", "빙그레", "url", false),
                Drinks("커피에몽", 250, 85, "커피우유 및 기타음료", "남양", "url", false),
                Drinks("우유속에카페돌체", 310, 88, "커피우유 및 기타음료", "매일", "url", false),
                Drinks("우유속에모카치노", 310, 82, "커피우유 및 기타음료", "매일", "url", false),
                Drinks("커피포리200", 200, 45, "커피우유 및 기타음료", "서울우유", "url", false),
                Drinks("서울우유커피맛", 200, 42, "커피우유 및 기타음료", "서울우유", "url", false),
                Drinks("HEYROO커피우유", 500, 110, "커피우유 및 기타음료", "CU", "url", false),
                Drinks("덴마크커피커피우유", 300, 60, "커피우유 및 기타음료", "덴마크", "url", false),
                Drinks("홍루이젠로얄밀크티퀸", 250, 96, "커피우유 및 기타음료", "서강", "url", false),
                Drinks("홍루이젠로얄밀크티휴", 250, 59, "커피우유 및 기타음료", "서강", "url", false),
                Drinks("가나프리미어마일드", 70, 8, "초콜릿", "롯데", "url", false),
                Drinks("72%드림카카오", 86, 36, "초콜릿", "롯데", "url", false),
                Drinks("태블릿밀크멕시코", 79, 11, "초콜릿", "고디바", "url", false),
                Drinks("태블릿다크멕시코", 79, 17, "초콜릿", "고디바", "url", false),
                Drinks("밀크초콜릿", 100, 10, "초콜릿", "노브랜드", "url", false),
                Drinks("다크초콜릿", 100, 11, "초콜릿", "노브랜드", "url", false),
                Drinks("밀크초콜릿", 43, 4, "초콜릿", "도브", "url", false),
                Drinks("스포트알파인밀크초콜릿", 100, 8, "초콜릿", "리터", "url", false),
                Drinks("알프스밀크", 100, 8, "초콜릿", "밀카", "url", false),
                Drinks("심플러스벨지안밀크초콜릿", 100, 14, "초콜릿", "홈플러스", "url", false),
                Drinks("심플러스벨지안다크초콜릿", 100, 16, "초콜릿", "홈플러스", "url", false),
                Drinks("온리프라이스벨기에밀크초콜릿", 100, 13, "초콜릿", "벨지안초콜릿그룹", "url", false),
                Drinks("온리프라이스벨기에다크초콜릿", 100, 25, "초콜릿", "벨지안초콜릿그룹", "url", false),
                Drinks("데어리밀크초콜릿", 165, 17, "초콜릿", "캐드버리", "url", false),
                Drinks("스위스밀크초콜릿", 100, 12, "초콜릿", "토블론", "url", false),
                Drinks("스위스다크초콜릿", 100, 29, "초콜릿", "토블론", "url", false),
                Drinks("밀크초콜릿자이언트바", 198, 31, "초콜릿", "허쉬", "url", false),
                Drinks("인텐스다크86%카카오", 117, 20, "초콜릿", "기라델리", "url", false),
                Drinks("다크초콜릿", 100, 44, "초콜릿", "까쉐우간다", "url", false),
                Drinks("엑셀런트다크90%", 100, 18, "초콜릿", "린트", "url", false),
                Drinks("마켓오초콜릿오리지널", 36, 9, "초콜릿", "오리온", "url", false),
                Drinks("파인다크초콜릿코코아85%", 100, 22, "초콜릿", "바인리히", "url", false),
                Drinks("시모아다크초콜릿", 80, 48, "초콜릿", "시모아", "url", false),
                Drinks("약콩초콜릿", 40, 12, "초콜릿", "밥스누(서울대학교)", "url", false),

                )
        }
    }

}