package com.example.wachacha


import android.content.Context
import android.content.SharedPreferences
import org.json.JSONArray
import java.util.Arrays.toString

class MySharedPreferences(context: Context) {

    private val prefsFilename = "prefs"
    private val prefsKeyCaf = "mycaffeine"
    private val prefsKeyDrink = "mydrink"
    private val prefs: SharedPreferences = context.getSharedPreferences(prefsFilename, 0)

    var favorite: String?
        get() = prefs.getString(prefsKeyDrink, "" )
        set(value) = prefs.edit().putString(prefsKeyDrink, value).apply()
}