package com.example.caffeinbody

import android.app.*
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.wearable.CapabilityClient
import com.google.android.gms.wearable.Wearable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

//한 번만 실행하게 하기

class ForegroundService : Service() {
    val tag = "ForegroundService"

    override fun onCreate() {
        Log.e(tag, "foregroundservice created")
        CoroutineScope(Dispatchers.Main).launch{
            val dataClient by lazy { Wearable.getDataClient(application) }
            val messageClient by lazy { Wearable.getMessageClient(application) }
            //val capabilityClient by lazy { Wearable.getCapabilityClient(application) }
            //val clientDataViewModel by lazy { ViewModelProvider(ForegroundService()).get(ClientDataViewModel::class.java)

            dataClient.addListener(ClientDataViewModel())
            messageClient.addListener(ClientDataViewModel())
            Log.e(tag, "foreground에 리스너 등록")
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        /*if(intent == null){
            Log.e(tag, "null인가 아닌가")
            return START_STICKY
        }//재시작하게

        Log.e(tag, "onstart됨")*/
        //return super.onStartCommand(intent, flags, startId);

        return START_STICKY

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e(tag, "service destroyed")
        setAlarmTimer()
    }

    private fun setAlarmTimer() {
        val c: Calendar = Calendar.getInstance()
        c.timeInMillis = System.currentTimeMillis()
        c.add(Calendar.SECOND, 1)
        val intent = Intent(this, AlarmReceiver::class.java)
        val sender = PendingIntent.getBroadcast(this, 0, intent, 0)

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.set(AlarmManager.RTC_WAKEUP, c.timeInMillis, sender)
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }
}
