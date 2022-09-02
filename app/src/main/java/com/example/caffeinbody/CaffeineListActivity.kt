package com.example.caffeinbody

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet.GONE
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.caffeinbody.database.Drinks
import com.example.caffeinbody.database.DrinksDatabase
import com.example.caffeinbody.databinding.ActivityCaffeineListBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class CaffeineListActivity : AppCompatActivity(), View.OnClickListener {
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

        db = DrinksDatabase.getInstance(applicationContext)!!
        db.drinksDao().selectiscafe(category).observe(this, Observer {
            binding.caffeinList.adapter = caffeineadapter
            caffeineadapter.datas.addAll(it)
        })


        binding.caffeinList.layoutManager = layoutManager
        caffeineadapter.notifyDataSetChanged()
        binding.starbucks.setOnClickListener(this)
        binding.ediya.setOnClickListener(this)
        binding.twosome.setOnClickListener(this)
        binding.hollys.setOnClickListener(this)
        binding.hollys.setOnClickListener(this)
        binding.paiks.setOnClickListener(this)
        binding.theventi.setOnClickListener(this)
        binding.gongcha.setOnClickListener(this)



    }
    override fun onClick(v: View?) {
        var checked=0
        when(v?.id){
            binding.starbucks.id -> {
                if(checked != 1) {
                 //   binding.starbucks.borderWidth = 4
                    selectDrinkMadeBy(db, "스타벅스")
                    checked=1
                } else checked = 0
            }
            binding.ediya.id -> {
                if(checked != 2) {
                   // binding.ediya.borderWidth = 4
                    selectDrinkMadeBy(db, "이디야")
                    checked=2
                } else checked = 0

            }
            binding.twosome.id -> {
                if(checked != 3) {
                //    binding.twosome.borderWidth = 4
                    selectDrinkMadeBy(db, "투썸플레이스")
                    checked=3
                } else checked = 0
            }
            binding.hollys.id -> {
                if(checked != 4) {
                 //   binding.hollys.borderWidth = 4
                    selectDrinkMadeBy(db, "할리스")
                    checked=4
                } else checked = 0
            }
            binding.paiks.id -> {
                if(checked != 5) {
              //      binding.hollys.borderWidth = 4
                    selectDrinkMadeBy(db, "빽다방")
                    checked=5
                } else checked = 0
            }
            binding.theventi.id -> {
                if(checked != 6) {
               //     binding.hollys.borderWidth = 4
                    selectDrinkMadeBy(db, "더벤티")
                    checked=6
                } else checked = 0
            }
            binding.gongcha.id -> {
                if(checked != 7) {
             //       binding.hollys.borderWidth = 4
                    selectDrinkMadeBy(db, "공차")
                    checked=7
                } else checked = 0
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
            val intent = Intent(this, PlusDrinkActivity::class.java)
           // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
           // intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
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