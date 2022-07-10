package com.example.caffeinbody

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.caffeinbody.database.CafeDatas
import com.example.caffeinbody.database.DrinksDatabase
import com.example.caffeinbody.databinding.ActivitySearchBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class SearchDrinksActivity: AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    private lateinit var db: DrinksDatabase
    lateinit var caffeineadapter: CaffeineAdapter
    var datas = mutableListOf<CaffeineData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        caffeineadapter = CaffeineAdapter(this)
        binding.caffeinList.adapter = caffeineadapter

        binding.resultText.setMovementMethod(ScrollingMovementMethod())

        binding.searchBtn.setOnClickListener {
            var find = binding.editText2.text.toString()
            binding.resultText.setText(null)
            db = DrinksDatabase.getInstance(applicationContext)!!
            selectDrinkName(db, find)
        }

        binding.searchBtn2.setOnClickListener {
            var find = binding.editText3.text.toString()
            binding.resultText.setText(null)
            db = DrinksDatabase.getInstance(applicationContext)!!
            selectDrinkMadeBy(db, find)
        }

        binding.searchBtn3.setOnClickListener {
            var find = binding.editText4.text.toString()
            binding.resultText.setText(null)
            db = DrinksDatabase.getInstance(applicationContext)!!
            selectDrinkCategory(db, find)
        }

        val layoutManager = GridLayoutManager(this,2)
        binding.caffeinList.layoutManager = layoutManager
    }

    fun selectDrinkName(db: DrinksDatabase, find: String){
        CoroutineScope(Dispatchers.Main).launch {
            val drinkNames = CoroutineScope(Dispatchers.IO).async {
                db.drinksDao().selectDrinkName("%$find%")
            }.await()
            var count = 0
            drinkNames.count()
            for (names in drinkNames){
                Log.e("NameNew", "selectDrinkName: " + names)
                datas.add(CaffeineData(1, names.id, names.drinkName, 0) )
                binding.resultText.append((++count).toString() + ")" + names.toString() + "\n")
            }
            Log.e("datas", datas.count().toString())
            initRecycler()
            datas.clear()
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
                datas.add(CaffeineData(1, maker.id, maker.drinkName, 0) )
                binding.resultText.append((++count).toString() + ")" + maker.toString() + "\n")
            }
            initRecycler()
            datas.clear()
        }
    }

    fun selectDrinkCategory(db: DrinksDatabase, find: String){
        CoroutineScope(Dispatchers.Main).launch {
            val categories = CoroutineScope(Dispatchers.IO).async {
                db.drinksDao().selectDrinkCategory("%$find%")
            }.await()
            var count = 0
            for (category in categories){
                Log.e("maker", "selectDrinkName: " + category)
                datas.add(CaffeineData(1, category.id, category.drinkName, 0) )
                binding.resultText.append((++count).toString() + ")" + category.toString() + "\n")
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