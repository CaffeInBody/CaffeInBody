package com.example.wachacha

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import com.google.android.gms.wearable.*
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class DataLayerListenerService : WearableListenerService() {//wear에서 데이터 영역 이벤트 처리
    private var favorite: String = ""
    private val messageClient by lazy { Wearable.getMessageClient(this) }

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    override fun onDataChanged(dataEvents: DataEventBuffer) {
        super.onDataChanged(dataEvents)
        dataEvents.forEach { dataEvent ->
            val uri = dataEvent.dataItem.uri
            Log.e("DLLS", "data changed")
            when (uri.path) {
                FAVORITE_PATH -> {
                    Log.e("favorite: ", "this is favorite" + uri.toString())
                    favorite = DataMapItem.fromDataItem(dataEvent.dataItem)
                        .dataMap
                        .getString(DataLayerListenerService.FAVORITE_KEY)
                    Log.e("스마트폰에서 온 favorite", favorite)
                    App.prefs.favorite = favorite
                    var prefsFav = App.prefs.favorite
                    Log.e("favorite prefs", prefsFav.toString())
                }
                else ->{
                    Log.e("DLLS", "none")
                }
            }
        }
    }

    override fun onMessageReceived(messageEvent: MessageEvent) {
        super.onMessageReceived(messageEvent)
        Log.e("메시지옴", "1")
        when (messageEvent.path) {
            START_ACTIVITY_PATH -> {
                Log.e("메시지옴", "2")
                startActivity(
                    Intent(this, MainActivityWearOS::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }

    companion object {
        private const val TAG = "DLLS"

        private const val START_ACTIVITY_PATH = "/start-activity3"
        const val DATA_ITEM_RECEIVED_PATH = "/data-item-received"
        const val IMAGE_PATH = "/image"
        const val IMAGE_KEY = "photo"
        const val FAVORITE_PATH = "/favorite"
        const val FAVORITE_KEY = "favorite"
    }
}
