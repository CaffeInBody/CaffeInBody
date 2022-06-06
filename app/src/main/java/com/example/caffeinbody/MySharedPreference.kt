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
}