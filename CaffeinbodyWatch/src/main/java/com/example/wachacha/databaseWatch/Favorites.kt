package com.example.wachacha.databaseWatch

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Favorites(
    var drinkName: String,
    var size: Int,
    var caffeine: Int,
    var category: String,
    var madeBy: String,
    var tmp: String
){
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}