package com.example.caffeinbody.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface DrinksDao {
    @Insert
    fun insert(drink: Drinks)

    @Insert
    fun insertAll(vararg drink: Drinks)

    @Update
    fun update(drink: Drinks)

    @Delete
    fun delete(drink: Drinks)

    @Query("SELECT * FROM Drinks")
    fun getAll(): LiveData<List<Drinks>>

    @Query("select * from Drinks where madeBy like :something")
    fun selectDrinkMadeBy(something: String): List<Drinks>

    @Query("select count(*) from Drinks")
    fun selectCount(): Int

    @Query("select * from Drinks where drinkName like :something")
    fun selectDrinkName(something: String): List<Drinks>

    @Query("select * from Drinks where category like :something")
    fun selectDrinkCategory(something: String): List<Drinks>

    @Query("select * from Drinks where iscafe like :something")
    fun selectiscafe(something: Boolean): LiveData<List<Drinks>>

    @Query("select * from Drinks where caffeine_caffeine1 BETWEEN 0 AND :something")
    fun recommendcaffeine(something: Double): List<Drinks>

    @Query("select * from Drinks where favorite = :something")//favorite 항목만 반환(true/false)
    fun selectFavorite(something: Boolean): LiveData<List<Drinks>>

    @Query("update Drinks set favorite = :something where drinkName = :name")//favorite 항목만 변경(true/false)
    fun updateFavorite(something: Boolean, name: String)

    //favorite update 함수
}