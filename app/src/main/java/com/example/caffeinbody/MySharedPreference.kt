package com.example.caffeinbody

import android.content.Context
import android.content.SharedPreferences
import org.json.JSONArray
import org.json.JSONException
import java.util.Arrays.toString

class MySharedPreferences(context: Context) {

    private val prefsFilename = "prefs"
    private val prefsKeyCaf = "mycaffeine"
    private val prefsKeyDrink = "mydrink"
    private val prefs: SharedPreferences = context.getSharedPreferences(prefsFilename, 0)

    var todayCaf: Int?//섭취한 카페인 누계
        get() = prefs.getInt(prefsKeyCaf, 0 )
        set(value) = prefs.edit().putInt(prefsKeyCaf, value!!).apply()
    var remainCaf: Int?//todayCaf 하루 지날 시 초기화 방지를 위한 체내 남은 카페인량 저장
        get() = prefs.getInt("remainCaf", 0 )
        set(value) = prefs.edit().putInt("remainCaf", value!!).apply()
    var sensetivity: String?//1회 카페인 섭취 권고량
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

    var dayCaffeine: String?//하루 카페인 섭취 권고량
        get() = prefs.getString("dayCaffeine", null)
        set(value) = prefs.edit().putString("dayCaffeine", value!!).apply()

    var weekCafJson: ArrayList<String>//요일별 카페인 섭취 수치
        get() {
            val json = prefs.getString("weekCafJson", null)
            val weekCaf = ArrayList<String>()
            if (json != null) {
                try {
                    val jsonArray = JSONArray(json)
                    for (i in 0 until jsonArray.length()) weekCaf.add(jsonArray.optString(i))
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
            return weekCaf
        }
        set(values) {
            val editor = prefs.edit()
            val jsonArray = JSONArray()
            for (value in values) {
                jsonArray.put(value)
            }
            if (values.isNotEmpty()) {
                editor.putString("weekCafJson", jsonArray.toString())
            } else {
                editor.putString("weekCafJson", null)
            }
            editor.apply()
        }

    var monthCafJson: String?//월별 카페인 섭취 수치
        get() = prefs.getString("monthCafJson", null)
        set(value) = prefs.edit().putString("monthCafJson", value!!).apply()

    var multiply: Float? //민감도 곱하는 수치
        get() = prefs.getFloat("multiply", 0.0f)
        set(value) = prefs.edit().putFloat("multiply", value!!).apply()

    var heartrateAvg: String? //심박수 평균
        get() = prefs.getString("heartrateAvg", null)
        set(value) = prefs.edit().putString("heartrateAvg", value).apply()

    var registeredDate: Int?//카페인 등록 날짜
        get() = prefs.getInt("todaydate", 0 )
        set(value) = prefs.edit().putInt("todaydate", value!!).apply()

    var registeredMonth: Int?//카페인 등록 월
        get() = prefs.getInt("registeredMonth", 0 )
        set(value) = prefs.edit().putInt("registeredMonth", value!!).apply()

    var registeredYear: Int?//카페인 등록 일
    get() = prefs.getInt("registeredYear", 0 )
        set(value) = prefs.edit().putInt("registeredYear", value!!).apply()

    var registeredTime: Float?//카페인 등록 시간
        get() = prefs.getFloat("registeredTime", 0.0f)
        set(value) = prefs.edit().putFloat("registeredTime", value!!).apply()
}