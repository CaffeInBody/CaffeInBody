package com.example.caffeinbody

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.caffeinbody.database.Drinks

class CaffeineAdapter (private val context: Context) : RecyclerView.Adapter<CaffeineAdapter.ViewHolder>() {

    var type: CaffeinCase? = null
    var datas = mutableListOf<Drinks>()
    var style: Int? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if(type== CaffeinCase.SMALL) style = R.layout.item_caffeine
        else style = R.layout.item_caffeine

        val view = LayoutInflater.from(context).inflate(style!!,parent,false)
        return ViewHolder(view)

    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val article : Drinks = datas.get(position)
        holder.bind(article)


        holder.itemView.setOnClickListener{
            val intent = Intent(holder.itemView.context, DrinkCaffeineActivity::class.java)
            intent.putExtra("id",position)

            //서버있으면지워도됨
            intent.putExtra("name",article.drinkName)
            intent.putExtra("img", article.imgurl)
            intent.putExtra("caffeine", article.caffeine?.caffeine1)
            ContextCompat.startActivity(holder.itemView.context, intent, null)
            //카페인 마시기로 이동
        }}

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val txt: TextView = itemView.findViewById(R.id.txt_name)
        private val img: ImageView = itemView.findViewById(R.id.img_caffeine)
     //   private val caf: TextView = itemView.findViewById(R.id.txt_caffeine)
        private val caf: TextView = itemView.findViewById(R.id.caf_name)
        private val drink: TextView = itemView.findViewById(R.id.txt_caffeine)

        fun bind(article: Drinks){
           // txt.text = article.drinkName
          //  caf.text = article.caffeine?.caffeine1.toString() +"mg"
            txt.text = article.madeBy
            drink.text = article.drinkName
            caf.text= article.caffeine?.caffeine1.toString() +"mg"

            if(article.iscafe == true) {
                if(article.imgurl.startsWith("https")  )Glide.with(itemView).load(article.imgurl).override(500,).into(img)
                else if(article.madeBy == "스타벅스") Glide.with(itemView).load(R.drawable.starbucks_logo).override(300,).into(img)
                else if(article.madeBy == "이디야") Glide.with(itemView).load(R.drawable.ediya_logo).override(300,).into(img)
                else if(article.madeBy == "투썸플레이스") Glide.with(itemView).load(R.drawable.twosome_logo).override(300,).into(img)
                else if(article.madeBy == "할리스") Glide.with(itemView).load(R.drawable.hollys_logo).override(300,).into(img)
                else  Glide.with(itemView).load(R.drawable.coffee_sample).into(img)
            }
            else if ( article.iscafe == false) Glide.with(itemView).load(R.drawable.cola_sample).into(img)
            else Glide.with(itemView).load(article.imgurl).placeholder(R.drawable.starbucks_logo).override(500,).into(img)


        }

    }


}