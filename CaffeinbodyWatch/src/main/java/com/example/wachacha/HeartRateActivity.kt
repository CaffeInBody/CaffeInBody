/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.wachacha

import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.example.wachacha.databinding.ActivityHeartrateBinding
import com.google.android.gms.wearable.CapabilityClient
import com.google.android.gms.wearable.Node
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.Wearable
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

/**
 * Activity displaying the app UI. Notably, this binds data from [MainViewModel] to views on screen,
 * and performs the permission check when enabling measure data.
 */
@AndroidEntryPoint
class HeartRateActivity : AppCompatActivity() {
    private val messageClient by lazy { Wearable.getMessageClient(this) }
    private val capabilityClient by lazy { Wearable.getCapabilityClient(this) }
    private val dataClient by lazy { Wearable.getDataClient(this) }
    var average = 0f
    var count = 0

    private lateinit var binding: ActivityHeartrateBinding
    private lateinit var permissionLauncher: ActivityResultLauncher<String>

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHeartrateBinding.inflate(layoutInflater)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(binding.root)
        onQueryOtherDevicesClicked("hey: " + count++)

        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
                when (result) {
                    true -> {
                        Log.i(TAG, "Body sensors permission granted")
                        // Only measure while the activity is at least in STARTED state.
                        // MeasureClient provides frequent updates, which requires increasing the
                        // sampling rate of device sensors, so we must be careful not to remain
                        // registered any longer than necessary.
                        lifecycleScope.launchWhenStarted {
                            Log.e("heartrate2", "heartrateactivity")
                            binding.statusText.setText("측정 시작")
                            viewModel.measureHeartRate()
                            average = String.format("%.1f", viewModel.average/30).toFloat()
                            binding.statusText.setText("측정완료 : " + average)
                            onQueryOtherDevicesClicked(average.toString())
                        }
                    }
                    false -> Log.i(TAG, "Body sensors permission not granted")
                }
            }

        // Bind viewmodel state to the UI.
        lifecycleScope.launchWhenStarted {
            viewModel.uiState.collect {
                updateViewVisiblity(it)
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.heartRateAvailable.collect {
                binding.statusText.text = getString(R.string.measure_status, it)
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.heartRateBpm.collect {
		        binding.lastMeasuredValue.text = String.format("%.1f", it)
            }//한 30초 정도만 측정하게
        }
    }

    //전송 버튼 전체적인 작업 실행
    fun onQueryOtherDevicesClicked(watchMessage: String) {
        lifecycleScope.launch {
            var nodeId: String = ""
            try {
                val nodes = getCapabilitiesForReachableNodes()
                    /*.filterValues { "mobile" in it || "wear" in it }*/.keys
                displayNodes(nodes)
                nodes.map { node ->
                    nodeId = node.id
                }
                Log.e("nodeid2222222222: ", nodeId)//노드 아이디 구했다!
                sendHeartRate(nodeId, watchMessage)
            } catch (cancellationException: CancellationException) {
                throw cancellationException
            } catch (exception: Exception) {
                Log.e("HeartRateActivity", "Querying nodes failed: $exception")
            }
        }
    }
    //심박수 보내기
    fun sendHeartRate(nodeId: String, watchMessage: String){
        lifecycleScope.launch {
            try {
                val payload = watchMessage.toByteArray()
                messageClient.sendMessage(
                    nodeId,
                    DataLayerListenerService.DATA_ITEM_RECEIVED_PATH,
                    payload
                )
                    .await()
                Log.e("sendHeartRate", "Message sent successfully hahahahah")
            } catch (cancellationException: CancellationException) {
                throw cancellationException
            } catch (exception: Exception) {
                Log.e("sendHeartRate", "Message failed")
            }
        }
    }
    //사용가능한 노드 찾기
    suspend fun getCapabilitiesForReachableNodes(): Map<Node, Set<String>> =
        capabilityClient.getAllCapabilities(CapabilityClient.FILTER_REACHABLE)
            .await()
            // Pair the list of all reachable nodes with their capabilities 노드들과 가능한 기능을 매칭하는것
            .flatMap { (capability, capabilityInfo) ->
                capabilityInfo.nodes.map { it to capability }
            }
            // Group the pairs by the nodes
            .groupBy(
                keySelector = { it.first },
                valueTransform = { it.second }
            )
            // Transform the capability list for each node into a set
            .mapValues { it.value.toSet() }

    //스마트폰 노드 찾았는지 확인하고 토스트 띄움
    fun displayNodes(nodes: Set<Node>) {
        val message = if (nodes.isEmpty()) {
            getString(R.string.no_device)
        } else {
            getString(R.string.connected_nodes, nodes.joinToString(", ") { it.displayName }) + "전송 완료"
        }
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }


    override fun onStart() {
        super.onStart()
        permissionLauncher.launch(android.Manifest.permission.BODY_SENSORS)
        Log.e("tlwkr","dkldk")
    }

    private fun updateViewVisiblity(uiState: UiState) {
        (uiState is UiState.Startup).let {
            binding.progress.isVisible = it
        }
        // These views are visible when heart rate capability is not available.
        (uiState is UiState.HeartRateNotAvailable).let {
            binding.brokenHeart.isVisible = it
            binding.notAvailable.isVisible = it
        }
        // These views are visible when the capability is available.
        (uiState is UiState.HeartRateAvailable).let {
            binding.statusText.isVisible = it
            binding.lastMeasuredLabel.isVisible = it
            binding.lastMeasuredValue.isVisible = it
            binding.heart.isVisible = it
        }
    }
}
