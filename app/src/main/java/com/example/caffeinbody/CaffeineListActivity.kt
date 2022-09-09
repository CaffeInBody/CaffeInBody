package com.example.caffeinbody

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet.GONE
import androidx.lifecycle.LifecycleOwner
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
//todo 원을 클릭하면 별표
class CaffeineListActivity : AppCompatActivity(), View.OnClickListener {
    var arrayString = arrayOf<String>("카페음료","일반음료","기타")
    lateinit var caffeineadapter: CaffeineAdapter
    private lateinit var db: DrinksDatabase
    var datas = mutableListOf<Drinks>()
    var num:Int ?=null
    var isitcafe =true
    var first = true
    var activity = this

    private val binding: ActivityCaffeineListBinding by lazy {
        ActivityCaffeineListBinding.inflate(
            layoutInflater
        )
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.e("adapter", "크리에이트")
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        var num= intent.getIntExtra("listnum",1)
        //TODO 필터링 누르고 다시 전체화면 볼 수 있는 기능

        setSupportActionBar(binding.toolbar)
        //    supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setTitle((arrayString[num]))

        caffeineadapter = CaffeineAdapter(this, CaffeinCase.LARGE)
        caffeineadapter.contextParent = activity
        binding.caffeinList.adapter = caffeineadapter
        val layoutManager = GridLayoutManager(this,2)

        db = DrinksDatabase.getInstance(applicationContext)!!

        caffeineadapter.contextParent= applicationContext

        /*CoroutineScope(Dispatchers.Main).launch {
            val cafes = CoroutineScope(Dispatchers.IO).async {
                db.drinksDao().selectFavorite(true)
            }.await()

            for (cafe in cafes) {
                //Log.e("maker", "selectDrinkName: " + cafe)
                //   datas.add(CaffeineData(1, maker.id, maker.drinkName, 0) )
                if (cafe.iscafe) datas.add(cafe)

            }
            caffeineadapter.datas.addAll(datas)
            caffeineadapter.notifyDataSetChanged()
            datas.clear()
        }*/
        if(num==0) {
            isitcafe= true
            binding.starbucks.setOnClickListener(this)
            binding.ediya.setOnClickListener(this)
            binding.twosome.setOnClickListener(this)
            binding.hollys.setOnClickListener(this)
            binding.hollys.setOnClickListener(this)
            binding.paiks.setOnClickListener(this)
            binding.theventi.setOnClickListener(this)
            binding.gongcha.setOnClickListener(this)

            first = false
           selectEverything(isitcafe, "%%",)//이거 지우면 적어도 필터에 전체 값이 나오진 않음
            first = true
        }
        else if(num == 1){
            isitcafe = false
            binding.coffeeBrand.visibility = View.GONE
            selectEverything(isitcafe, "%%",)
        }else {
            isitcafe = false
            var string = "해열·진통제"
            db.drinksDao().getDrinkCategory(string).observe(this, Observer {
                caffeineadapter.datas.clear()
                caffeineadapter.datas.addAll(it)
                caffeineadapter.notifyDataSetChanged()
            })
            binding.coffeeBrand.visibility = View.GONE

        }

        binding.caffeinList.layoutManager = layoutManager


    }
    override fun onClick(v: View?) {
        first = false
        Log.e("CaffeineListActivity", "!" +v.toString())
        var checked=0
        //TODO 이부분 왠지 필터 버튼 누를 때마다 텍스트는 잘 나오는데
        //  이미지 몇개가 바로바로 잘 안나오는데 한 번 봐주세요
        when(v?.id){
            binding.starbucks.id -> {Log.e("ba", "!!!스")
             //   if(binding.starbucks.isChecked)
                selectEverything(isitcafe, "스타벅스")

              //  else addAll(db)
            }
            binding.ediya.id -> {Log.e("ba", "!!!이")
                   // binding.ediya.borderWidth = 4
                selectEverything(isitcafe, "이디야")
            }
            binding.twosome.id -> {Log.e("ba", "!!!튜")
                selectEverything(isitcafe, "투썸플레이스")
            }
            binding.hollys.id -> {Log.e("ba", "!!!할")
                 //   binding.hollys.borderWidth = 4
                selectEverything(isitcafe, "할리스")
            }
            binding.paiks.id -> {Log.e("ba", "!!!빽")
              //      binding.hollys.borderWidth = 4
                selectEverything(isitcafe, "빽다방")

            }
            binding.theventi.id -> {Log.e("ba", "!!!더")
               //     binding.hollys.borderWidth = 4
                selectEverything(isitcafe, "더벤티")
            }
            binding.gongcha.id -> {
                selectEverything(isitcafe, "공차")

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
            intent.putExtra("name", num)
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

    fun selectEverything(iscafe: Boolean, madeby: String){//왜 필터링 할 때마다 늘엉나지
        if (first == true){
        db = DrinksDatabase.getInstance(applicationContext)!!
            db.drinksDao().selectAllConditions(iscafe, madeby).observe(this, Observer {
                Log.e("ba1", it[0].madeBy)//todo 계속 하다 보면 호출 횟/ 점점 늘어남(카테고리 별로 star 누를 때마다 늘어나서 꼬임)
                caffeineadapter = CaffeineAdapter(this, CaffeinCase.LARGE)
                binding.caffeinList.adapter = caffeineadapter
                caffeineadapter.datas.clear()
                caffeineadapter.datas.addAll(it)
                caffeineadapter.notifyDataSetChanged()
            })
        } else{
        CoroutineScope(Dispatchers.Main).launch {
            val makers = CoroutineScope(Dispatchers.IO).async {
                db.drinksDao().selectDrinkMadeBy(madeby)
            }.await()

            for (maker in makers){
                Log.e("maker", "selectDrinkName: " + maker)
                //   datas.add(CaffeineData(1, maker.id, maker.drinkName, 0) )
                if(maker.iscafe) datas.add(maker)

            }
            Log.e("ba2", datas[0].madeBy)

            caffeineadapter.datas.clear()
            caffeineadapter.datas.addAll(datas)
            Log.e("sorted", datas.toString())
            caffeineadapter.notifyDataSetChanged()
            datas.clear()
    }}
        }
}