package com.example.caffeinbody

import android.content.Context
import android.content.SharedPreferences
import org.json.JSONArray
import java.util.Arrays.toString

class MySharedPreferences(context: Context) {

    private val prefsFilename = "prefs"
    private val prefsKeyEdt = "myEditText"
    private val prefs: SharedPreferences = context.getSharedPreferences(prefsFilename, 0)

    var todayCaf: Int?
        get() = prefs.getInt(prefsKeyEdt, 0 )
        set(value) = prefs.edit().putInt(prefsKeyEdt, value!!).apply()

    var date: String?
        get() = prefs.getString("drinkTime", null)
        set(value) = prefs.edit().putString("drinkTime", value).apply()

    var todayCafJson: String?
        get() = prefs.getString("drinkCafJson", null)
        set(value) = prefs.edit().putString("drinkCafJson", value).apply()
}