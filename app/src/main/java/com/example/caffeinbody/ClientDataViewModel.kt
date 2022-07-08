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
import com.google.android.gms.wearable.CapabilityClient
import com.google.android.gms.wearable.CapabilityInfo
import com.google.android.gms.wearable.DataClient
import com.google.android.gms.wearable.DataEvent
import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.MessageEvent
import com.google.android.material.internal.ContextUtils.getActivity

/**
 * A state holder for the client data.
 */
class ClientDataViewModel :
    ViewModel(),
    DataClient.OnDataChangedListener,
    MessageClient.OnMessageReceivedListener,
    CapabilityClient.OnCapabilityChangedListener {

    lateinit var mainActivity: MainActivity

    private var _events = mutableStateListOf<Event>()
    /**The list of events from the clients.**/
    val events: List<Event> = _events

    /***The currently captured image (if any), available to send to the wearable devices.*/
    var image by mutableStateOf<Bitmap?>(null)
        private set

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
        //_events = mutableStateListOf<Event>()//초기화 필요한가 안필요한가 음
        _events.add(
            Event(
                title = R.string.message_from_watch,
                //text = messageEvent.toString()
                text = messageEvent.data.decodeToString()//bytearray를 디코드하기
            )
        )
        events.forEach {
                event -> Log.e("메시지 왔음: ", event.text)
        }
        Log.e("메시지 왔음: ", messageEvent.data.decodeToString() )

    }

    override fun onCapabilityChanged(capabilityInfo: CapabilityInfo) {
        _events.add(
            Event(
                title = R.string.capability_changed,
                text = capabilityInfo.toString()
            )
        )
    }

    fun onPictureTaken(bitmap: Bitmap?) {
        image = bitmap ?: return
    }
}

/**
 * A data holder describing a client event.
 */
data class Event(
    @StringRes val title: Int,
    val text: String
)
