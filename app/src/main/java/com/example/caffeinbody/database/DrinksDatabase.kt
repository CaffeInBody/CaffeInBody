package com.example.caffeinbody.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Drinks::class], version = 1)
abstract class DrinksDatabase: RoomDatabase() {
    abstract fun drinksDao(): DrinksDao
}