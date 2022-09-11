package com.example.caffeinbody

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.caffeinbody.database.Drinks
import com.example.caffeinbody.database.DrinksDatabase
import com.example.caffeinbody.database.Functions

class CaffeineViewModel(application: Application): AndroidViewModel(application) {
    lateinit var livedata: LiveData<List<Drinks>>
    lateinit var dataNoLive: List<Drinks>
    var functions= Functions(application)
    val data = arrayListOf<Drinks>()
    val tag = "CaffeineViewModel"
    //lateinit var db: DrinksDatabase

    fun getAll(iscafe: Boolean): LiveData<List<Drinks>>{
        livedata = functions.selectAll(iscafe)
        Log.e(tag, livedata.toString())
        return livedata
    }

    fun getAll2(iscafe: Boolean, adapter: CaffeineAdapter): List<Drinks>{
        dataNoLive = functions.selectAll2(iscafe, adapter)
        Log.e(tag, dataNoLive.toString())
        return dataNoLive
    }


    fun getFilteredMadeby(iscafe: Boolean, madeby: String, adapter: CaffeineAdapter): LiveData<List<Drinks>>{
        livedata = functions.getFilteredMadeby(iscafe, madeby, adapter)
        Log.e(tag, livedata.toString())
        return livedata
    }

    fun getFilteredMadeby2(iscafe: Boolean, madeby: String, adapter: CaffeineAdapter): List<Drinks>{
        dataNoLive = functions.getFilteredMadeby2(iscafe, madeby, adapter)
        Log.e(tag, dataNoLive.toString())
        return dataNoLive
    }
}