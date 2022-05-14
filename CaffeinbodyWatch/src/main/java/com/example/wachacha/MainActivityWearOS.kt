package com.example.wachacha

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.wachacha.databinding.ActivityMainWearOsBinding
import com.google.android.gms.wearable.CapabilityClient
import com.google.android.gms.wearable.Node
import com.google.android.gms.wearable.Wearable
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class MainActivityWearOS : ComponentActivity() {
    private val dataClient by lazy { Wearable.getDataClient(this) }
    private val messageClient by lazy { Wearable.getMessageClient(this) }
    private val capabilityClient by lazy { Wearable.getCapabilityClient(this) }
    private val clientDataViewModel by viewModels<ClientDataViewModel>()

    private lateinit var binding: ActivityMainWearOsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainWearOsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.sendMessage.setOnClickListener{
            Log.e("btn pressed", "버튼 눌림")
            val watchMessage = binding.testMessageEdit.text.toString()//입력한 텍스트 가져와서 보내기
            onQueryOtherDevicesClicked(watchMessage)
        }
    }
    //전송 버튼 전체적인 작업 실행
    private fun onQueryOtherDevicesClicked(watchMessage: String) {
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
                Log.d(TAG, "Querying nodes failed: $exception")
            }
        }
    }
    //심박수 보내기
    private fun sendHeartRate(nodeId: String, watchMessage: String){
        lifecycleScope.launch {
            try {
                val payload = watchMessage.toByteArray()
                messageClient.sendMessage(
                    nodeId,
                    DataLayerListenerService.DATA_ITEM_RECEIVED_PATH,
                    payload
                )
                    .await()
                Log.e("MainActiityWearOs", "Message sent successfully hahahahah")
            } catch (cancellationException: CancellationException) {
                throw cancellationException
            } catch (exception: Exception) {
                Log.e("MainActiityWearOs", "Message failed")
            }
        }
    }
    //사용가능한 노드 찾기
    private suspend fun getCapabilitiesForReachableNodes(): Map<Node, Set<String>> =
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
    private fun displayNodes(nodes: Set<Node>) {
        val message = if (nodes.isEmpty()) {
            getString(R.string.no_device)
        } else {
            getString(R.string.connected_nodes, nodes.joinToString(", ") { it.displayName }) + "전송 완료"
        }
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }



    //////////////////////////////////리스너 등록하고 제거하는 부분
    override fun onResume() {
        super.onResume()
        dataClient.addListener(clientDataViewModel)
        messageClient.addListener(clientDataViewModel)
        capabilityClient.addListener(
            clientDataViewModel,
            Uri.parse("wear://"), //스마트폰도 워치도 모두 이 부분을 등록해야 서로의 노드 찾을 수 있음
            CapabilityClient.FILTER_REACHABLE
        )
        Log.e("capability", capabilityClient.toString())
    }

    override fun onPause() {
        super.onPause()
        dataClient.removeListener(clientDataViewModel)
        messageClient.removeListener(clientDataViewModel)
        capabilityClient.removeListener(clientDataViewModel)
    }


    companion object {
        private const val TAG = "MainActivity"

        private const val CAMERA_CAPABILITY = "camera"
        private const val WEAR_CAPABILITY = "wear"
        private const val MOBILE_CAPABILITY = "mobile"
    }
}