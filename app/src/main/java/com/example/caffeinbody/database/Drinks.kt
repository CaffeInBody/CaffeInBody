package com.example.caffeinbody.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Drinks(
    var drinkName: String,
    var size: Int,
    var caffeine: Int,
    var category: String,
    var madeBy: String,
    var url: String,
    var isCoffee: Boolean
){
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}
