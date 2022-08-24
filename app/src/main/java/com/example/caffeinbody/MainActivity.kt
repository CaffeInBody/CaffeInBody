package com.example.caffeinbody

import android.app.ActivityManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.util.Log
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.example.caffeinbody.databinding.ActivityMainBinding
import com.google.android.gms.wearable.Wearable
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener  {
    private val dataClient by lazy { Wearable.getDataClient(this) }
    private val messageClient by lazy { Wearable.getMessageClient(this) }
    private val capabilityClient by lazy { Wearable.getCapabilityClient(this) }
    //private val clientDataViewModel by viewModels<ClientDataViewModel>()

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(
            layoutInflater
        )
    }

    private val receiver = object: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (Intent.ACTION_DATE_CHANGED == intent!!.action) {
                App.prefs.weekCafJson.add(App.prefs.todayCaf.toString())
                App.prefs.todayCaf = 0f
                Log.e("AlarmCheck", "리셋완료"+App.prefs.todayCaf+" "+App.prefs.weekCafJson)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        Log.e("test", "1: " + Build.VERSION.SDK_INT + " 2: " + Build.VERSION_CODES.O)

        binding.bottomNavigationView.setOnNavigationItemSelectedListener(this)
        binding.bottomNavigationView.selectedItemId = R.id.menu_name

        // HOME as default tab
        supportFragmentManager.beginTransaction().add(
            R.id.frame_layout,
            HomeFragment(),"home"
        ).commit()
        // Launch app with HOME selected as default start tab

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val powerManager = getSystemService(POWER_SERVICE) as PowerManager
            if (powerManager.isIgnoringBatteryOptimizations(packageName) == false) {
                val dialog = PhBatteryDialog()
                dialog.show(supportFragmentManager, "dialog")
            }
        }
        val serviceClass = ForegroundService::class.java
        val it = Intent(this, serviceClass)

        if (!this.isServiceRunning(serviceClass)) {
            Log.e("MainActivity", "Service is not running - START SERVICE")
            startService(it)
            Log.e("MainActivity", "서비스 생성됨")
        }

    }
    fun Context.isServiceRunning(serviceClass: Class<*>): Boolean {
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

        for (service in activityManager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                Log.e("isServiceRunning", "Service is running")
                return true
            }
        }
        return false
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
    }}
    ///////////리스너 등록/제거 부분
    /*override fun onResume() {
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
        var filter = IntentFilter()
        filter.addAction(Intent.ACTION_DATE_CHANGED)
        registerReceiver(receiver, filter)
        Log.e("receiver", "자정 receiver")
    }

    override fun onPause() {
        super.onPause()
        dataClient.removeListener(clientDataViewModel)
        messageClient.removeListener(clientDataViewModel)
        capabilityClient.removeListener(clientDataViewModel)
        Log.e("MainActivity", "리스너 제거됨")

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
    }*/