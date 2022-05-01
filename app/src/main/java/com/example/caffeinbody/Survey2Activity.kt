package com.example.caffeinbody

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.caffeinbody.databinding.FragmentSettingBinding
import com.example.caffeinbody.databinding.FragmentSurvey1Binding
import com.example.caffeinbody.databinding.FragmentSurvey2Binding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Survey1Fragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class Survey2Activity  : AppCompatActivity() {

    private val binding: FragmentSurvey2Binding by lazy {
        FragmentSurvey2Binding.inflate(
            layoutInflater
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.buttonNext.setOnClickListener {

            val selectActivity =  Survey3Activity()
            val intent = Intent(this, selectActivity::class.java)
            startActivity(intent)
            finish()
        }
        setContentView(binding.root)
    }




}