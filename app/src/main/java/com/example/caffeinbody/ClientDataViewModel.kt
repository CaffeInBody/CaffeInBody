package com.example.caffeinbody

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.google.android.gms.wearable.*
import com.google.android.material.internal.ContextUtils.getActivity
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import org.json.JSONObject
import java.util.jar.Manifest

/**
 * A state holder for the client data.
 */
class ClientDataViewModel :
    ViewModel(),
    DataClient.OnDataChangedListener,
    MessageClient.OnMessageReceivedListener,
    CapabilityClient.OnCapabilityChangedListener {

    private var _events = mutableStateListOf<Event>()
    /**The list of events from the clients.**/
    val events: List<Event> = _events


    override fun onDataChanged(dataEvents: DataEventBuffer) {
        Log.e("데이터 추가됨", "-------------------------------")
        _events.addAll(
            dataEvents.map { dataEvent ->
                val title = when (dataEvent.type) {
                    DataEvent.TYPE_CHANGED -> R.string.data_item_changed
                    DataEvent.TYPE_DELETED -> R.string.data_item_deleted
                    else -> R.string.data_item_unknown
                }

                Event(
                    title = title,
                    text = dataEvent.dataItem.toString()
                )
            }
        )
        Log.e("ClientDataViewModel", "뭔가가 추가됐음")
    }

    override fun onMessageReceived(messageEvent: MessageEvent) {
        val uri = messageEvent.path
        //Log.e("ClientDataViewModel", "uri: $uri")
        when (uri) {
            "/heartrate" -> {
                App.prefs.heartrateAvg = messageEvent.data.decodeToString()
                Log.e("ClientDataViewModel", "메시지 왔음: " + App.prefs.heartrateAvg.toString())
            }

            "/currentInfos" -> {
                var jsonObject = JSONObject()
                jsonObject.put("onceRecommended", App.prefs.sensetivity!!)
                jsonObject.put("onceDrinkable", App.prefs.currentcaffeine!!)
                jsonObject.put("dayRecommended", App.prefs.dayCaffeine!!)
                jsonObject.put("dayDrinked", App.prefs.todayCaf!!.toString())
                Log.e("ClientDataViewModel", jsonObject.toString())
                sendCaffeineDatas(jsonObject.toString())
            }
            else ->{
                Log.e("ClientDataViewModel", "none, path: " + uri)
            }
        }



    }

    override fun onCapabilityChanged(capabilityInfo: CapabilityInfo) {
        _events.add(
            Event(
                title = R.string.capability_changed,
                text = capabilityInfo.toString()
            )
        )
    }

    private fun sendCaffeineDatas(msg: String) {
        Log.e("CDVM", "hi")
        viewModelScope.launch{
            Log.e("CDVM", "hi2")
            val dataClient  = Wearable.getDataClient(App.context())
            try {
                val request = PutDataMapRequest.create("/currentInfos").apply {
                    dataMap.putString("caffeineDatas", msg)
                }
                    .asPutDataRequest()
                    .setUrgent()

                val result = dataClient.putDataItem(request).await()
                Log.e("ClientDataViewModel", "DataItem saved: $result")
            } catch (cancellationException: CancellationException) {
                Log.e("ClientDataViewModel", "캔슬됨")
            } catch (exception: Exception) {
                Log.d("ClientDataViewModel", "Saving DataItem failed: $exception")
            }
        }
    }
}

/**
 * A data holder describing a client event.
 */
data class Event(
    @StringRes val title: Int,
    val text: String
)
