package com.example.caffeinbody

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
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


        setSupportActionBar(binding.toolbar)
        //    supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setTitle((arrayString[num]))

        caffeineadapter = CaffeineAdapter(this, CaffeinCase.LARGE)

        val layoutManager = GridLayoutManager(this,2)

        db = DrinksDatabase.getInstance(applicationContext)!!



        if(num==0) {

            caffeineadapter.notifyDataSetChanged()
            binding.starbucks.setOnClickListener(this)
            binding.ediya.setOnClickListener(this)
            binding.twosome.setOnClickListener(this)
            binding.hollys.setOnClickListener(this)
            binding.hollys.setOnClickListener(this)
            binding.paiks.setOnClickListener(this)
            binding.theventi.setOnClickListener(this)
            binding.gongcha.setOnClickListener(this)

            db.drinksDao().selectiscafe(true).observe(this, Observer {
                binding.caffeinList.adapter = caffeineadapter
                Log.e("sortedList",it.toString())
                //TODO 좋아요 기준 정렬
              //  var sortedList = datas.sortedByDescending { it.favorite}.reversed()
              ///////  Log.e("sortedList",sortedList.toString())
                caffeineadapter.datas.addAll(it)
            })
        }
        else if(num == 1){
            binding.coffeeBrand.visibility = View.GONE
            db.drinksDao().selectiscafe(false).observe(this, Observer {
                binding.caffeinList.adapter = caffeineadapter
                for (maker in it){
                    //   datas.add(CaffeineData(1, maker.id, maker.drinkName, 0) )
                    if(maker.caffeine?.caffeine1 !=0) datas.add(maker) //카페인 함량이 0인것은 제외
                    initRecycler()
                }

            })

        }else {
            var string = "해열·진통제"
            db.drinksDao().getDrinkCategory(string).observe(this, Observer {
                binding.caffeinList.adapter = caffeineadapter
                caffeineadapter.datas.addAll(it)
            })
            binding.coffeeBrand.visibility = View.GONE
        }

        binding.caffeinList.layoutManager = layoutManager


    }
    override fun onClick(v: View?) {
        var checked=0
        datas.clear()
        when(v?.id){
            binding.starbucks.id -> {
             //   if(binding.starbucks.isChecked)
                    selectDrinkMadeBy(db, "스타벅스")
              //  else addAll(db)
            }
            binding.ediya.id -> {
                   // binding.ediya.borderWidth = 4
                    selectDrinkMadeBy(db, "이디야")
            }
            binding.twosome.id -> {
                    selectDrinkMadeBy(db, "투썸플레이스")
            }
            binding.hollys.id -> {
                 //   binding.hollys.borderWidth = 4
                    selectDrinkMadeBy(db, "할리스")
            }
            binding.paiks.id -> {
              //      binding.hollys.borderWidth = 4
                    selectDrinkMadeBy(db, "빽다방")

            }
            binding.theventi.id -> {
               //     binding.hollys.borderWidth = 4
                    selectDrinkMadeBy(db, "더벤티")

            }
            binding.gongcha.id -> {
                    selectDrinkMadeBy(db, "공차")

            }
      }

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(com.example.caffeinbody.R.menu.menu_search,menu)
        return super.onCreateOptionsMenu(menu)
    }



    fun selectDrinkMadeBy(db: DrinksDatabase, find: String){
        CoroutineScope(Dispatchers.Main).launch {
            val makers = CoroutineScope(Dispatchers.IO).async {
                db.drinksDao().selectDrinkMadeBy(find)
            }.await()
            var count = 0
            for (maker in makers){
                Log.e("maker", "selectDrinkName: " + maker)
                //   datas.add(CaffeineData(1, maker.id, maker.drinkName, 0) )
                if(maker.iscafe) datas.add(maker)
                initRecycler()
            }

           datas.clear()
        }
    }
    fun addAll(db: DrinksDatabase, find: Boolean){
        db.drinksDao().selectiscafe(find).observe(this, Observer {
            binding.caffeinList.adapter = caffeineadapter
            caffeineadapter.datas.addAll(it)
        })
    }

    private fun initRecycler(){
        caffeineadapter.datas.clear()
        caffeineadapter.datas.addAll(datas)
        caffeineadapter.notifyDataSetChanged()
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
            true
        }

        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }
}