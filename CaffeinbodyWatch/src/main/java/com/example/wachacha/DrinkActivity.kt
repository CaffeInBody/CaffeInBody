package com.example.wachacha

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.wachacha.databaseWatch.FavFunctions.Companion.selectFav
import com.example.wachacha.databaseWatch.FavoritesDatabase
import com.example.wachacha.databinding.ActivityDrinkregisterBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class DrinkActivity: AppCompatActivity() {
    private lateinit var db: FavoritesDatabase

    private val binding: ActivityDrinkregisterBinding by lazy {
        ActivityDrinkregisterBinding.inflate(
            layoutInflater
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        db = FavoritesDatabase.getInstance(applicationContext)!!


        var favorite = ""

        CoroutineScope(Dispatchers.Main).launch {
            val users = CoroutineScope(Dispatchers.IO).async {
                db.favoritesDao().selectTest()
            }.await()

            for(user in users){
                favorite = user.tmp
                Log.e("favorite tmp12", favorite)
            }
            binding.messageFav.setText(favorite)
            Log.e("favorite", "마지막")
        }


    }

}