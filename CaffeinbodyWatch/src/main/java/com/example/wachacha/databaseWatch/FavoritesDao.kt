package com.example.wachacha.databaseWatch

import androidx.room.*

@Dao
interface FavoritesDao {
    @Insert
    fun insert(fav: Favorites)

    @Update
    fun update(fav: Favorites)

    @Delete
    fun delete(fav: Favorites)

    @Query("select * from Favorites")
    fun selectTest(): List<Favorites>
}