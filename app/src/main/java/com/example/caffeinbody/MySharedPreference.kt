package com.example.caffeinbody

import android.content.Context
import android.content.SharedPreferences
import org.json.JSONArray
import java.util.Arrays.toString

class MySharedPreferences(context: Context) {

    private val prefsFilename = "prefs"
    private val prefsKeyCaf = "mycaffeine"
    private val prefsKeyDrink = "mydrink"
    private val prefs: SharedPreferences = context.getSharedPreferences(prefsFilename, 0)

    var todayCaf: Int?//섭취한 카페인 누계
        get() = prefs.getInt(prefsKeyCaf, 0 )
        set(value) = prefs.edit().putInt(prefsKeyCaf, value!!).apply()

    /*var todayDrink: String?
        get() = prefs.getString(prefsKeyDrink, "" )
        set(value) = prefs.edit().putString(prefsKeyDrink, value).apply()

    var date: String?
        get() = prefs.getString("drinkTime", null)
        set(value) = prefs.edit().putString("drinkTime", value).apply()

    var todayCafJson: String?
        get() = prefs.getString("drinkCafJson", null)
        set(value) = prefs.edit().putString("drinkCafJson", value).apply()*/

    var sensetivity: String?//1회섭취량
        get() = prefs.getString("sensitivity", null)
        set(value) = prefs.edit().putString("sensitivity", value!!).apply()

    //surveydatas
    var age: String?
        get() = prefs.getString("age", null)
        set(value) = prefs.edit().putString("age", value).apply()

    var isPregnant: Boolean
        get() = prefs.getBoolean("isPregnant", false)
        set(value) = prefs.edit().putBoolean("isPregnant", value).apply()

    var heartbeat: Boolean
        get() = prefs.getBoolean("heartbeat", false)
        set(value) = prefs.edit().putBoolean("heartbeat", value).apply()

    var headache: Boolean
        get() = prefs.getBoolean("headache", false)
        set(value) = prefs.edit().putBoolean("headache", value).apply()
    //
    var currentcaffeine: String?//현재 기준 섭취 가능 카페인 양
        get() = prefs.getString("currentCaf", null)
        set(value) = prefs.edit().putString("currentCaf", value!!).apply()

    /*var oncecaffeine: String?
        get() = prefs.getString("oncecaffeine", null)
        set(value) = prefs.edit().putString("oncecaffeine", value!!).apply()*/

    var dayCaffeine: String?
        get() = prefs.getString("dayCaffeine", null)
        set(value) = prefs.edit().putString("dayCaffeine", value!!).apply()

    var weekCafJson: String?//요일별 카페인 섭취 수치
        get() = prefs.getString("drinkCafJson", null)
        set(value) = prefs.edit().putString("drinkCafJson", value).apply()

    var multiply: Float? //민감도 곱하는 수치
        get() = prefs.getFloat("multiply", 0.0f)
        set(value) = prefs.edit().putFloat("multiply", value!!).apply()

    var heartrateAvg: String? //심박수 평균
        get() = prefs.getString("heartrateAvg", null)
        set(value) = prefs.edit().putString("heartrateAvg", value).apply()

    var registeredDate: Int?//카페인 등록 날짜
        get() = prefs.getInt("todaydate", 0 )
        set(value) = prefs.edit().putInt("todaydate", value!!).apply()

    var registeredTime: Float?//카페인 등록 시간
        get() = prefs.getFloat("registeredTime", 0.0f)
        set(value) = prefs.edit().putFloat("registeredTime", value!!).apply()
}