
package com.example.caffeinbody

import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.caffeinbody.DetailActivity.Companion.calculateCaffeinLeft
import com.example.caffeinbody.databinding.FragmentHomeBinding
import com.github.mikephil.charting.data.Entry
import com.google.android.gms.wearable.CapabilityClient
import com.google.android.gms.wearable.NodeClient
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.Wearable
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import org.json.JSONArray
import java.lang.Math.E
import java.time.Duration
import java.util.*
import kotlin.collections.ArrayList


class HomeFragment : Fragment() {
    var count = 1

    // TODO: Rename and change types of parameters
    private val dataClient by lazy { Wearable.getDataClient(activity) }
    private val messageClient by lazy { Wearable.getMessageClient(getActivity()) }
    private val capabilityClient by lazy { Wearable.getCapabilityClient(getActivity()) }
    private val nodeClient by lazy { Wearable.getNodeClient(getActivity()) }
    private val clientDataViewModel by viewModels<ClientDataViewModel>()
    //getActivity()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUI()
        arguments?.let {

        }

        lateinit var mainActivity: MainActivity
        mainActivity = context as MainActivity
        clientDataViewModel.mainActivity = mainActivity
    }

    private val binding: FragmentHomeBinding by lazy {
        FragmentHomeBinding.inflate(
            layoutInflater
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //   initRecycler()
        setUI()

        binding.addBeverageBtn.setOnClickListener{
            sendFavorite()
            activity?.let{
            val selectActivity =  DrinkTypeActivity()
            val intent = Intent(context, selectActivity::class.java)
            startActivity(intent)
        }}
        binding.showDetailText.setOnClickListener{
            activity?.let{
                val selectActivity =  DetailActivity()
                val intent = Intent(context, selectActivity::class.java)
                startActivity(intent)
            }
        }
        binding.myPageBtn.setOnClickListener{
            val recommendActivity =  RecommendActivity()
            val intent = Intent(context, recommendActivity::class.java)
            startActivity(intent)
        }
        binding.checkWatchBtn.setOnClickListener{
            startWearableActivity()
            Log.e("폰에서 워치앱 열기 on HomeFrag", "하자!!!")
        }
        return binding.root
    }

////////////////워치로 열기 버튼 누르면 워치로 메시지 보내서 앱 열게 함
    private fun startWearableActivity() {
        lifecycleScope.launch {
            try {
                val nodes = nodeClient.connectedNodes.await()//노드 검색
                Log.e("nodes: ", nodes[0].toString())//노드 검색은 된다
                // Send a message to all nodes in parallel
                nodes.map { node ->//워치에서 메시지 받게
                    async {
                        messageClient.sendMessage(node.id, START_ACTIVITY_PATH, byteArrayOf())
                            .await()
                    }
                }.awaitAll()

                Log.d(TAG, "Starting activity requests sent successfully")
            } catch (cancellationException: CancellationException) {
                Log.e(TAG, "실패1")
                throw cancellationException
            } catch (exception: Exception) {
                Log.d(TAG, "실패2")
            }
        }
    }
    ///////////////즐겨찾기 데이터 보내기(동기화 시점은...?)
    private fun sendFavorite() {
        Log.e("보내짐", "")
        lifecycleScope.launch {
            Log.e("TAG", "안녕")
            try {
                val request = PutDataMapRequest.create(FAVORITE_PATH).apply {
                    dataMap.putString(FAVORITE_KEY, "받은 메시지: " + (++count).toString())
                    //dataMap.putString(FAVORITE_KEY, (++count).toString())//메시지가 변경돼야 전송됨.
                    //dataMap.putStringArrayList(FAVORITE_KEY, 리스트)//즐겨찾기 리스트
                }
                    .asPutDataRequest()
                    .setUrgent()

                val result = dataClient.putDataItem(request).await()
                Log.e(TAG, "DataItem saved: $result")
            } catch (cancellationException: CancellationException) {
                Log.e(TAG, "캔슬됨")
            } catch (exception: Exception) {
                Log.d(TAG, "Saving DataItem failed: $exception")
            }
        }
    }

    private fun setUI(){
        val todayCaf = App.prefs.todayCaf
        binding.intakenCaffeineText.setText(todayCaf.toString())
        App.prefs.dayCaffeine?.let { binding.maximumADayText.setText(it) }

        var nowTime = DetailActivity.getTime()
        var registeredTime = App.prefs.registeredTime//가장 최근 카페인 섭취한 시간
        var halfTime =
            DetailActivity.calHalfTime(getString(R.string.basicTime).toInt(), App.prefs.multiply!!)//-> 민감도 반영 반감기 시간
        Log.e("home", getString(R.string.basicTime).toInt().toString())
        var leftCaffeine = calculateCaffeinLeft(todayCaf!!.toFloat(), nowTime- registeredTime!!, halfTime, 0.5f)
        putCurrentCaffeine(leftCaffeine)
        val servingsize = App.prefs.currentcaffeine//체내 남은 카페인량 sensitivity와 currentcaffeine의 용도 차이

        var percent = 1.0

        if(servingsize!= null) {
            binding.AvailableCaffeineText.setText(servingsize)
            percent = servingsize!!.toDouble() / App.prefs.sensetivity!!.toDouble()
        }else{
            binding.AvailableCaffeineText.setText(App.prefs.sensetivity)
            App.prefs.currentcaffeine = App.prefs.sensetivity
        }
        binding.heart.start()
        binding.heart.waveHeightPercent = (percent)!!.toFloat()
    //  percent?.let { binding.heart.setProgress(it.toInt()) }
    }


    fun putCurrentCaffeine(caffeineLeft: Float){
        //---------------섭취권고량 설정-----------------
        //TODO 소현
        var servingsize = App.prefs.sensetivity?.toDouble()//나의 적정하루 섭취권고량
        if (servingsize != null) {
            if((servingsize - caffeineLeft!!) > 0 ) {
                var current = servingsize - caffeineLeft
                App.prefs.currentcaffeine = "%.2f".format(current)
            }
            else App.prefs.currentcaffeine = "0"
        }
        //----------------------------------------
    }



    companion object {
        private const val TAG = "HomeFragment"

        private const val START_ACTIVITY_PATH = "/start-activity3"
        private const val FAVORITE_PATH = "/favorite"
        private const val FAVORITE_KEY = "favorite"
    }

}