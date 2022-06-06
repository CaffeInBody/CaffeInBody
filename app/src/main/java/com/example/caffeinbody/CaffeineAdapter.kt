package com.example.caffeinbody

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class CaffeineAdapter (private val context: Context) : RecyclerView.Adapter<CaffeineAdapter.ViewHolder>() {


    var datas = mutableListOf<CaffeineData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_caffeine,parent,false)
        return ViewHolder(view)

    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val article : CaffeineData = datas.get(position)
        holder.bind(article)


        holder.itemView.setOnClickListener{
            val intent = Intent(holder.itemView?.context, DrinkCaffeineActivity::class.java)
            intent.putExtra("id",position)

            //서버있으면지워도됨
            intent.putExtra("name",article.name)
            intent.putExtra("img", article.img)

            ContextCompat.startActivity(holder.itemView.context, intent, null)
            //카페인 마시기로 이동
        }}

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val txt: TextView = itemView.findViewById(R.id.txt_caffeine)
        private val img: ImageView = itemView.findViewById(R.id.img_caffeine)

        fun bind(article: CaffeineData){
            txt.text = article.name

            if(article.img==null)
                Glide.with(itemView).load(R.drawable.coffee_sample).into(img)
            else Glide.with(itemView).load(article.img).centerCrop().into(img)

        }

    }


}