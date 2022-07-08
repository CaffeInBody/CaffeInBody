package com.example.caffeinbody

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.caffeinbody.databinding.ActivityMainBinding
import com.google.android.gms.wearable.CapabilityClient
import com.google.android.gms.wearable.Wearable
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener  {
    private val dataClient by lazy { Wearable.getDataClient(this) }
    private val messageClient by lazy { Wearable.getMessageClient(this) }
    private val capabilityClient by lazy { Wearable.getCapabilityClient(this) }
    private val nodeClient by lazy { Wearable.getNodeClient(this) }
    private val clientDataViewModel by viewModels<ClientDataViewModel>()

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
    ///////////리스너 등록/제거 부분
    override fun onResume() {
        super.onResume()
        dataClient.addListener(clientDataViewModel)
        messageClient.addListener(clientDataViewModel)
        capabilityClient.addListener(
            clientDataViewModel,
            Uri.parse("wear://"),
            CapabilityClient.FILTER_REACHABLE
        )

        lifecycleScope.launch {
            try {
                capabilityClient.addLocalCapability("camera").await()
            } catch (cancellationException: CancellationException) {
                throw cancellationException
            } catch (exception: Exception) {
                Log.e("MainActivity", "Could not add capability: $exception")
            }
        }
    }

    override fun onPause() {
        super.onPause()
        dataClient.removeListener(clientDataViewModel)
        messageClient.removeListener(clientDataViewModel)
        capabilityClient.removeListener(clientDataViewModel)

        lifecycleScope.launch {
            // This is a judicious use of NonCancellable.
            // This is asynchronous clean-up, since the capability is no longer available.
            // If we allow this to be cancelled, we may leave the capability in-place for other
            // nodes to see.
            withContext(NonCancellable) {
                try {
                    capabilityClient.removeLocalCapability("camera").await()
                } catch (exception: Exception) {
                    Log.e("MainActivity", "Could not remove capability: $exception")
                }
            }
        }
    }

}