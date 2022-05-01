package com.example.caffeinbody

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.caffeinbody.databinding.FragmentHomeBinding
import com.example.caffeinbody.databinding.FragmentSettingBinding

class SettingFragment : Fragment() {
    private val binding: FragmentSettingBinding by lazy {
        FragmentSettingBinding.inflate(
            layoutInflater
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //   initRecycler() }
        return binding.root
    }



}