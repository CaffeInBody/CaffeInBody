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

    var todayCaf: Int?
        get() = prefs.getInt(prefsKeyCaf, 0 )
        set(value) = prefs.edit().putInt(prefsKeyCaf, value!!).apply()

    var todayDrink: String?
        get() = prefs.getString(prefsKeyDrink, "" )
        set(value) = prefs.edit().putString(prefsKeyDrink, value).apply()

    var date: String?
        get() = prefs.getString("drinkTime", null)
        set(value) = prefs.edit().putString("drinkTime", value).apply()

    var todayCafJson: String?
        get() = prefs.getString("drinkCafJson", null)
        set(value) = prefs.edit().putString("drinkCafJson", value).apply()

    var sensetivity: String?
        get() = prefs.getString("sensitivity", null)
        set(value) = prefs.edit().putString("sensitivity", value!!).apply()

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


    var currentcaffeine: String?
        get() = prefs.getString("currentCaf", null)
        set(value) = prefs.edit().putString("currentCaf", value!!).apply()

    var oncecaffeine: String?
        get() = prefs.getString("oncecaffeine", null)
        set(value) = prefs.edit().putString("oncecaffeine", value!!).apply()
    var dayCaffeine: String?
        get() = prefs.getString("dayCaffeine", null)
        set(value) = prefs.edit().putString("dayCaffeine", value!!).apply()

    var weekCafJson: String?
        get() = prefs.getString("drinkCafJson", null)
        set(value) = prefs.edit().putString("drinkCafJson", value).apply()

    var multiply: Float?
        get() = prefs.getFloat("multiply", 0.0f)
        set(value) = prefs.edit().putFloat("multiply", value!!).apply()

    var heartrateAvg: String?
        get() = prefs.getString("heartrateAvg", null)
        set(value) = prefs.edit().putString("heartrateAvg", value).apply()
}