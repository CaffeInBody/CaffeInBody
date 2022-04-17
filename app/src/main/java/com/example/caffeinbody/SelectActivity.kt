package com.example.caffeinbody

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.caffeinbody.databinding.ActivitySelectBinding

class SelectActivity : AppCompatActivity() {
    var num=1

    private val binding: ActivitySelectBinding by lazy {
        ActivitySelectBinding.inflate(
            layoutInflater
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select)

        binding.caffebtn.setOnClickListener{

            val caffeineListActivity =  CaffeineListActivity()
            val intent = Intent(this, caffeineListActivity::class.java)
            intent.putExtra("listnum",num)
            startActivity(intent)
            finish()
        }
        binding.generalbtn.setOnClickListener {onClick(binding.generalbtn)}
        binding.etcbtn.setOnClickListener{onClick(binding.etcbtn)}

    }

    private fun onClick(v: View) = View.OnClickListener {

        when(v){
            binding.caffebtn-> {
                num=1
            }
            binding.generalbtn -> {
                num=2
            }
            binding.etcbtn -> {
                num=3
            }

        }
        val caffeineListActivity =  CaffeineListActivity()
        val intent = Intent(this, caffeineListActivity::class.java)
        intent.putExtra("listnum",num)
        startActivity(intent)
        finish()
    }
}