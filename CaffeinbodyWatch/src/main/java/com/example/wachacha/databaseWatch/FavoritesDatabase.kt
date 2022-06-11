package com.example.wachacha.databaseWatch

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Favorites::class], version = 1)
abstract class FavoritesDatabase: RoomDatabase() {
    abstract fun favoritesDao(): FavoritesDao

    companion object {
        private var instance: FavoritesDatabase? = null

        @Synchronized
        fun getInstance(context: Context): FavoritesDatabase? {
            if (instance == null){
                synchronized(FavoritesDatabase::class){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        FavoritesDatabase::class.java,
                        "favorite-database"
                    ).build()
                }
            }
            return instance
        }
    }
}