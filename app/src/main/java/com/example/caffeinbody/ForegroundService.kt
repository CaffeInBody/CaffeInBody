package com.example.caffeinbody

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
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

//한 번만 실행하게 하기

class ForegroundService : Service() {
    val tag = "ForegroundService"

    override fun onCreate() {
        Log.e(tag, "foregroundservice created")
        createNotification()


        CoroutineScope(Dispatchers.Main).launch{
            val dataClient by lazy { Wearable.getDataClient(application) }
            val messageClient by lazy { Wearable.getMessageClient(application) }
            //val capabilityClient by lazy { Wearable.getCapabilityClient(application) }
            //val clientDataViewModel by lazy { ViewModelProvider(ForegroundService()).get(ClientDataViewModel::class.java)

            dataClient.addListener(ClientDataViewModel())
            messageClient.addListener(ClientDataViewModel())
            Log.e(tag, "foreground 리스너 등록")
            /*capabilityClient.addListener(
                clientDataViewModel,
                Uri.parse("wear://"),
                CapabilityClient.FILTER_REACHABLE
            )*/
        }
        /*startForeground(NOTIFICATION_DOWNLOAD_ID, createDownloadingNotification(0))
        //비동기 코드 넣기
        stopForeground(true)
        stopSelf()
        */
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if(intent == null){
            Log.e(tag, "null인가 아닌가")
            return START_STICKY
        }//재시작하게



        return super.onStartCommand(intent, flags, startId);

    }

    fun f() {
        for (i in 0..9) {
            try {
                Thread.sleep(1000)
                Log.d("test", "count: $i")
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            stopSelf()
        }
    }


    fun createNotification(){
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
        builder.setSmallIcon(R.mipmap.ic_launcher)
        builder.setContentTitle("Foreground Service")
        builder.setContentText("리스너 실행중")

        val notificationIntent = Intent(this, MainActivity::class.java)//splash?
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent,
            PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
        builder.setContentIntent(pendingIntent) // 알림 클릭 시 이동
        val notificationManager = this.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(CHANNEL_ID, "타이틀", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(serviceChannel)
        }
        notificationManager.notify(1, builder.build())
        val notification = builder.build()
        startForeground(1, notification)
    }
    /*private fun updateProgres(@IntRange(from = 0L, to = 100L) progress: Int) {
        notificationManager.notify(NOTIFICATION_DOWNLOAD_ID, createDownloadingNotification(progress))
    }

    private fun registerDefaultNotificationChannel() {
        if (Build.VERSION.SDK_INT >= VERSION_CODES.O) {
            notificationManager.createNotificationChannel(createDefaultNotificationChannel())
        }
    }

    @RequiresApi(VERSION_CODES.O)
    private fun createDefaultNotificationChannel() =
        NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_LOW).apply {
            description = CHANNEL_DESCRIPTION
            this.setShowBadge(true)
            this.lockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC
        }
*/
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    companion object {
        private const val NOTIFICATION_DOWNLOAD_ID = 1
        private const val NOTIFICATION_COMPLETE_ID = 2
        private const val CHANNEL_ID = "my_channel"
        private const val CHANNEL_NAME = "default"
        private const val CHANNEL_DESCRIPTION = "This is default notification channel"
    }
}
