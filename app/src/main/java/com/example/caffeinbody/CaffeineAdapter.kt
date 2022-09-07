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


class CaffeineAdapter (private val context: Context, type:CaffeinCase) : RecyclerView.Adapter<CaffeineAdapter.ViewHolder>() {
    var type: CaffeinCase? = type
    var datas = mutableListOf<Drinks>()
    var style: Int? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if(type== CaffeinCase.SMALL) style = R.layout.item_rec_caffeine
        else if(type == CaffeinCase.LARGE) style = R.layout.item_caffeine

        val view = LayoutInflater.from(context).inflate(style!!,parent,false)
        return ViewHolder(view)

    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val article : Drinks = datas.get(position)
        holder.bind(article)
        holder.star.setOnCheckedChangeListener{ buttonView, _ ->
            CoroutineScope(Dispatchers.Main).launch {
                val temp: Deferred<Boolean> = async(Dispatchers.IO) { // async 로 결과
                    var db = DrinksDatabase.getInstance(context)!!// 를 반환

                    if (article.favorite) { // 이미 즐겨찾기 되어있으면 삭제
                        db!!.drinksDao().updateFavorite(!article.favorite,article.id)
                        false
                    } else { // 없으면 즐겨찾기 저장
                        db!!.drinksDao().updateFavorite(!article.favorite,article.id)
                        true
                    }
                }
            }


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
                Glide.with(itemView).load(article.imgurl).placeholder(R.drawable.logo)
                    .override(500).into(img)

            } else if(article.imgurl.startsWith("$cacheDir/")){

                val bm = BitmapFactory.decodeFile(article.imgurl)
                Glide.with(itemView).load(bm).placeholder(R.drawable.logo)
                    .override(600).into(img)

                }
            else {
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

    fun updateFavorite(article: Drinks , boolean: Boolean, star : ToggleButton){
        //TODO 좋아요버튼
        var db = DrinksDatabase.getInstance(context)!!

        CoroutineScope(Dispatchers.Main).launch {
            CoroutineScope(Dispatchers.IO).async {

             if(boolean)db.drinksDao().updateFavorite(false,article.id)
             else if(!boolean) db.drinksDao().updateFavorite(true,article.id)

                var find =db.drinksDao().selectDrinkName(article.drinkName)
                star?.isChecked = article.favorite == true
                Log.e("dd",find[0].favorite.toString())
            }.await()


            }

        }


}