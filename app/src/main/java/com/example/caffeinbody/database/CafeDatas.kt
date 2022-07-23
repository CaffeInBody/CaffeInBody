package com.example.caffeinbody.database

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class CafeDatas {
    fun hello(){
        Log.e("testRoom", "this is hi 하이")
    }
    // private var db = DrinksDatabase.getInstance(applicationContext)

    fun testRoom(db: DrinksDatabase){
        CoroutineScope(Dispatchers.Main).launch {
            val countdb = CoroutineScope(Dispatchers.IO).async {
                db.drinksDao().selectCount()
            }.await()

            Log.e("roomDataCount: ", countdb.toString())
            Log.e("testRoom", "피니시1finish1")

        }
        Log.e("testRoom", "피니시finish2")
        hello()
    }



}