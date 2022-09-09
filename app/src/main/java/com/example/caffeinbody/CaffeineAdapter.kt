package com.example.caffeinbody

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.caffeinbody.database.Drinks
import com.example.caffeinbody.database.DrinksDatabase
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main


class CaffeineAdapter (private val context: Context, type:CaffeinCase) : RecyclerView.Adapter<CaffeineAdapter.ViewHolder>() {
    var type: CaffeinCase? = type
    var datas = mutableListOf<Drinks>()
    var style: Int? = null
    lateinit var contextParent: Context


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if(type== CaffeinCase.SMALL) style = R.layout.item_rec_caffeine
        else if(type == CaffeinCase.LARGE) style = R.layout.item_caffeine

        val view = LayoutInflater.from(context).inflate(style!!,parent,false)
        return ViewHolder(view)

    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val article : Drinks = datas.get(position)//한 번에 6개씩 가져오는거였어
        //Log.e("반복", "$position" + article.madeBy)
        holder.bind(article)

        holder.star.setOnClickListener{
            Log.e("CaffeineAdapter2 sorted?", datas.toString())
            CoroutineScope(Dispatchers.IO).launch {var int = 0
                var db = DrinksDatabase.getInstance(context)!!// 를 반환

                Log.e("CaffeineAdapter2b", article.favorite.toString() + article.toString())
                db!!.drinksDao().updateFavorite(!article.favorite,article.id)
                //val result = db.drinksDao().selectOne(article.drinkName)
                //Log.e("CaffeineAdapter3", result.favorite.toString() + result.toString())

                val result2 = db.drinksDao().selectAllConditionsNoLive(article.iscafe, article.madeBy)
                datas.clear()
                datas.addAll(result2)
                CoroutineScope(Main).launch{
                    Log.e("CaffeineAdapter23", "hi")
                    notifyDataSetChanged()
                }
            }
            //notifyDataSetChanged()


        }


        holder.itemView.setOnClickListener{
            if(article.caffeine?.caffeine1 != 0) {
                val intent = Intent(holder.itemView.context, DrinkCaffeineActivity::class.java)
                //서버있으면지워도됨
                intent.putExtra("name", article.id)

                ContextCompat.startActivity(holder.itemView.context, intent, null)
                //카페인 마시기로 이동}
        }}
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val txt: TextView = itemView.findViewById(R.id.txt_name)
        private val img: ImageView = itemView.findViewById(R.id.img_caffeine)
        private val logo: ImageView = itemView.findViewById(R.id.logo)
     //   private val caf: TextView = itemView.findViewById(R.id.txt_caffeine)
        private val caf: TextView = itemView.findViewById(R.id.caf_name)
        private val drink: TextView = itemView.findViewById(R.id.txt_caffeine)
        val star: ToggleButton = itemView.findViewById(R.id.star)

        private val cafcard: CardView = itemView.findViewById(R.id.caffeine)

        fun bind(article: Drinks){
           // txt.text = article.drinkName
          //  caf.text = article.caffeine?.caffeine1.toString() +"mg"
            txt.text = article.madeBy
            drink.text = article.drinkName
            caf.text= article.caffeine?.caffeine1.toString() +"mg"


            //TODO 좋아요버튼 왠지 작동이 안됨
            star.isChecked = article.favorite == true



            var cacheDir = context.cacheDir
            if(article.caffeine?.caffeine1 == 0 ){
                star.visibility = GONE
                cafcard.visibility = GONE
            }

            if (article.imgurl.startsWith("https:")) {
                //Log.e("CaffeineAdapter", "1" + article.toString())
                Glide.with(itemView).load(article.imgurl).placeholder(R.drawable.logo)
                    .override(500).into(img)

            } else if(article.imgurl.startsWith("$cacheDir/")){
                //Log.e("CaffeineAdapter", "2" + article.toString())
                val bm = BitmapFactory.decodeFile(article.imgurl)
                Glide.with(itemView).load(bm).placeholder(R.drawable.logo)
                    .override(600).into(img)

            }
            else {
                //Log.e("CaffeineAdapter", "3" + article.toString())
                if (article.madeBy == "스타벅스") Glide.with(itemView)
                    .load(R.drawable.starbucks_logo).override(250).into(logo)
                else if (article.madeBy == "이디야") Glide.with(itemView)
                    .load(R.drawable.ediya_logo).override(250).into(logo)
                else if (article.madeBy == "투썸플레이스") Glide.with(itemView)
                    .load(R.drawable.twosome_logo).override(250).into(logo)
                else if (article.madeBy == "할리스") Glide.with(itemView)
                    .load(R.drawable.hollys_logo).override(250).into(logo)
                else if (article.madeBy == "빽다방") Glide.with(itemView)
                    .load(R.drawable.paiks_logo).override(250).into(logo)
                else if (article.madeBy == "더벤티") Glide.with(itemView)
                    .load(R.drawable.theventi_logo).override(250).into(logo)
                else if (article.madeBy == "공차") Glide.with(itemView)
                    .load(R.drawable.gongcha_logo).override(250).into(logo)
                else Glide.with(itemView).load(R.drawable.logo).into(logo)
                img.visibility = GONE
                logo.visibility = VISIBLE
            }




        }

    }

    /*fun sortDb(madeby: String){
        val db = DrinksDatabase.getInstance(context)!!

        CoroutineScope(Dispatchers.Main).launch {
            val makers = CoroutineScope(Dispatchers.IO).async {
                db.drinksDao().selectDrinkMadeBy(madeby)
            }.await()

            for (maker in makers){
                Log.e("maker", "selectDrinkName: " + maker)
                //   datas.add(CaffeineData(1, maker.id, maker.drinkName, 0) )
                if(maker.iscafe) datas.add(maker)

            }
            Log.e("ba", datas[0].madeBy)

            datas.clear()
            datas.addAll(datas)
            Log.e("sorted", datas.toString())
            notifyDataSetChanged()
        }*/


}