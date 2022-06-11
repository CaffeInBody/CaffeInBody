package com.example.wachacha.databaseWatch

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavFunctions {

    companion object{
        fun addFav(db: FavoritesDatabase, tmp: String){
            CoroutineScope(Dispatchers.IO).launch {
                db.favoritesDao().insert(Favorites("", 0, 0, "", "", tmp))
            }
        }
        fun selectFav(db: FavoritesDatabase): String{
            var tmp = "!!"
            CoroutineScope(Dispatchers.Main).launch {
                val users = CoroutineScope(Dispatchers.IO).async {
                    db.favoritesDao().selectTest()
                }.await()

                for(user in users){
                    tmp = user.tmp
                    Log.e("favorite tmp12", tmp)

                }
                Log.e("favorite tmp 마지막", tmp)
            }
            return tmp
        }
    }
}