package com.example.caffeinbody

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet.GONE
import androidx.recyclerview.widget.GridLayoutManager
import com.example.caffeinbody.database.Drinks
import com.example.caffeinbody.database.DrinksDatabase
import com.example.caffeinbody.databinding.ActivityCaffeineListBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class CaffeineListActivity : AppCompatActivity() {
    var arrayString = arrayOf<String>("카페음료","일반음료","기타")
    lateinit var caffeineadapter: CaffeineAdapter
    private lateinit var db: DrinksDatabase
    var datas = mutableListOf<Drinks>()

    private val binding: ActivityCaffeineListBinding by lazy {
        ActivityCaffeineListBinding.inflate(
            layoutInflater
        )
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        var num= intent.getIntExtra("listnum",1)
        if(num!=0) binding.coffeeBrand.visibility = View.GONE

        setSupportActionBar(binding.toolbar)
        //    supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setTitle((arrayString[num]))

        caffeineadapter = CaffeineAdapter(this)
        var category = true
        category = num==0
        val layoutManager = GridLayoutManager(this,2)

        CoroutineScope(Dispatchers.IO).launch {

                db = DrinksDatabase.getInstance(applicationContext)!!
                caffeineadapter.datas.addAll(db.drinksDao().selectiscafe(category))
                //  Log.d("tag", "Error - "+ db.drinksDao().getAll().toString())


            }
        binding.caffeinList.adapter = caffeineadapter
        caffeineadapter.notifyDataSetChanged()
        binding.caffeinList.layoutManager = layoutManager

        binding.coffeeBrandBtn.setOnCheckedChangeListener{ group, checkedId ->
            when(checkedId) {
                R.id.starbucks -> selectDrinkMadeBy(db,"스타벅스")
                R.id.ediya -> selectDrinkMadeBy(db,"이디야")
                R.id.twosome -> selectDrinkMadeBy(db,"투썸플레이스")
            }
        }


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(com.example.caffeinbody.R.menu.menu_search,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        android.R.id.home -> {
            finish()
            true
        }
        R.id.search-> {
            val intent = Intent(this, SearchDrinksActivity::class.java)
            startActivity(intent)
            true
        }
        R.id.home-> {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
            finish()
            true
        }

        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }
    fun selectDrinkMadeBy(db: DrinksDatabase, find: String){
        CoroutineScope(Dispatchers.Main).launch {
            val makers = CoroutineScope(Dispatchers.IO).async {
                db.drinksDao().selectDrinkMadeBy("%$find%")
            }.await()
            var count = 0
            for (maker in makers){
                Log.e("maker", "selectDrinkName: " + maker)
                //   datas.add(CaffeineData(1, maker.id, maker.drinkName, 0) )
                datas.add(maker)
            }
            initRecycler()
            datas.clear()
        }
    }

    private fun initRecycler(){
        caffeineadapter.datas.clear()
        caffeineadapter.datas.addAll(datas)
        caffeineadapter.notifyDataSetChanged()
    }
}