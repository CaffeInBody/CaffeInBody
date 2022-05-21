package com.example.caffeinbody

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.caffeinbody.databinding.FragmentSettingBinding
import com.example.caffeinbody.databinding.FragmentSurvey1Binding
import com.example.caffeinbody.databinding.FragmentSurvey3Binding

class Survey3Activity  : AppCompatActivity() {
    var sensitivity: String =""
    var headache: String = ""
    var quantity: String = ""
    var before: Int = 0

    private val binding: FragmentSurvey3Binding by lazy {
        FragmentSurvey3Binding.inflate(
            layoutInflater
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.progressBar.incrementProgressBy(66)
        binding.seekBar.setProgress(0)
        binding.buttonNext.setOnClickListener {
            val shared = getSharedPreferences("result_survey", Context.MODE_PRIVATE)
            val editor = shared.edit()//sharedpreferences 값 확인해보기
            editor.putString("sensitivity", sensitivity)
            editor.putString("headache", headache)
            editor.putString("quantity", quantity)
            //editor.apply()

            val selectActivity = MainActivity()
            val intent = Intent(this, selectActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.button5.setOnClickListener{
            val selectActivity = MainActivity()
            val intent = Intent(this, selectActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                binding.seekBar.setProgress((progress / 150) * 150)
                if (progress<100){
                    binding.seekBar.setProgress(0)
                    quantity = 0.toString()}
                else if (progress >= 100 && progress <=200){
                    binding.seekBar.setProgress(150)
                    quantity = 150.toString()}
                else{
                    binding.seekBar.setProgress(300)
                    quantity = 0.toString()}

                Log.e("quantity: ", progress.toString())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {//변경 하기 위해 터치 시
            }
            override fun onStopTrackingTouch(seekBar: SeekBar?) {//변경한 후
            }
        })
        

        setContentView(binding.root)
    }





}