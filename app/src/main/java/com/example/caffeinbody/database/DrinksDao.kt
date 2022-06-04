package com.example.caffeinbody.database

import androidx.room.*

@Dao
interface DrinksDao {
    @Insert
    fun insert(drink: Drinks)

    @Update
    fun update(drink: Drinks)

    @Delete
    fun delete(drink: Drinks)

    @Query("select * from Drinks where drinkName = :something")
    fun selectDrinkName(something: String): List<Drinks>

}