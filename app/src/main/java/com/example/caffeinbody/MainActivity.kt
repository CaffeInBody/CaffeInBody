package com.example.caffeinbody

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.FragmentTransaction
import com.example.caffeinbody.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener  {


    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(
            layoutInflater
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.bottomNavigationView.setOnNavigationItemSelectedListener(this)
        binding.bottomNavigationView.selectedItemId = R.id.menu_name

        // HOME as default tab
        supportFragmentManager.beginTransaction().add(
            R.id.frame_layout,
            HomeFragment(),"home"
        ).commit()
        // Launch app with HOME selected as default start tab
    }
    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()

        when(p0.itemId){
            R.id.menu_name ->{
                val fragmentA = HomeFragment()
                transaction.replace(R.id.frame_layout,fragmentA, "HOME")
            }
            R.id.menu_decaf -> {
                val fragmentB = SettingFragment()
                transaction.replace(R.id.frame_layout,fragmentB, "CHAT")
            }
            R.id.menu_report -> {
                val fragmentC = ReportFragment()
                transaction.replace(R.id.frame_layout,fragmentC, "MY")
            }


        }
        transaction.addToBackStack(null)
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        transaction.commit()

        return true
    }

}