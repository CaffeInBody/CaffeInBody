package com.example.caffeinbody

import android.content.Context
import android.content.SharedPreferences

class MySharedPreferences(context: Context) {

    private val prefsFilename = "prefs"
    private val prefsKeyEdt = "myEditText"
    private val prefs: SharedPreferences = context.getSharedPreferences(prefsFilename, 0)

    var todayCaf: Int?
        get() = prefs.getInt(prefsKeyEdt, 0 )
        set(value) = prefs.edit().putInt(prefsKeyEdt, value!!).apply()
}