package com.example.caffeinbody

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.example.caffeinbody.database.CafeDatas
import com.example.caffeinbody.database.CafeDatas.Companion.testRoom
import com.example.caffeinbody.database.DrinksDatabase
import com.example.caffeinbody.databinding.ActivityDetailBinding
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import org.json.JSONArray
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.pow

//라인차트 그리기
class DetailActivity : AppCompatActivity() {
    val basicTime = 5
    private lateinit var db: DrinksDatabase

    private val lineChartData = ArrayList<Entry>()
    private val binding: ActivityDetailBinding by lazy {
        ActivityDetailBinding.inflate(
            layoutInflater
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        db = DrinksDatabase.getInstance(applicationContext)!!
        testRoom(db)
        CafeDatas.hello()
        Log.e("testDetail", "한글 깨지나")

        setSupportActionBar(binding.toolbar)
        //    supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        calculateHalfLife(1.0)

        var chartWeekLine = binding.linechart
        setWeekLine(chartWeekLine)

     //   binding.textView11.setText(App.prefs.todayCaf.toString() + "mg")
        binding.textView11.setText(App.prefs.currentcaffeine + "mg")

    }
    //새 카페인이 추가되면 총 마신 카페인량이 저장됨
    fun calculateHalfLife(sensitivity: Double){
        binding.textView11.setText(App.prefs.currentcaffeine + "mg")
        var caffeineVolume = App.prefs.todayCaf
        var halfTime = basicTime * (2 - sensitivity)//-> 민감도 반영 반감기 시간
        var timeI = 0
        //var nowTime = getTime()

        if (caffeineVolume != 0) {
            var blank = App.prefs.date
            var a = JSONArray(blank)
            var times = ArrayList<Float>()

            var blank2 = App.prefs.todayCafJson
            var a2 = JSONArray(blank2)
            var caffeines = ArrayList<Float>()

            var count = 0
            var currentTime = 0f

            for (i in 0 .. a.length() - 1){//원래는 -1 해야 함
                times.add(a.optString(i).toFloat()/60)
                caffeines.add(a2.optString(i).toFloat())
                Log.e("time", "저장된 카페인 데이터 times: " + times[i]+ " Caffeines: " + caffeines[i] + " " + i + "번째")
            }//여기까지가 입력한 시간과 양 어레이로 가져오기
            times.add(0f)//공백 넣어줌


            //첫번째 점 그려줌
            var caffeineRemain = caffeines[timeI]!!
            //putCurrentCaffeine(caffeineRemain)
            currentTime = times[timeI] + (halfTime * count++).toFloat()
            lineChartData.add(Entry(currentTime, caffeineRemain))

            while (caffeineRemain!! >= 10){
                Log.e("time", "남은 카페인 : $caffeineRemain")
                if (times[timeI + 1] != 0f){//더 추가된 카페인 데이터가 있으면
                    if (currentTime+ halfTime < times[timeI + 1]){//5시간 뒤보다 후에 추가됐으면
                        currentTime = times[timeI] + (halfTime * count).toFloat()
                        caffeineRemain = caffeineRemain!!/(2.0).toFloat()
                        lineChartData.add(Entry(currentTime, caffeineRemain))
                        Log.e("time", "one")
                    }else{//그래프 중간에 끼워야 할 때
                        val timeGap = times[timeI + 1] - currentTime//일수도 있고
                        currentTime = times[timeI + 1]
                        caffeineRemain = caffeineRemain - caffeineRemain/2*(timeGap/halfTime.toFloat()) + caffeines[timeI + 1]//시간이 흘러 줄어든 후 남은 카페인 양
                        lineChartData.add(Entry(currentTime, caffeineRemain))

                        timeI++
                        count = 0
                        Log.e("time", "two")

                    }
                }else{//이게 끝일 때
                    currentTime = times[timeI] + (halfTime * count).toFloat()
                    caffeineRemain = caffeineRemain!!/(2.0).toFloat()
                    lineChartData.add(Entry(currentTime, caffeineRemain))
                    Log.e("time", "three")
                }
                count++


            }/////여기까지만

        } else{
            lineChartData.add(Entry(0.toFloat(), 0.toFloat()))
        }


    }


    private fun setWeekLine(lineChart: LineChart) {
        val lineData = LineData()
        val set1 = LineDataSet(lineChartData, "체내 남은 카페인 양")
        lineData.addDataSet(set1)
        lineChart.setData(lineData)

        val xAxis = lineChart.xAxis
        xAxis.setDrawLabels(true)

        //xAxis.labelCount = 60
        //제일 처음 마신 시간을 그래프의 가장 앞쪽에
        val xAxisVals = ArrayList<String>(Arrays.asList("0시", "1시", "2시", "3시", "4시", "5시", "6시", "7시", "8시", "9시", "10시", "11시", "12시", "13시", "14시", "15시", "16시", "17시", "18시", "19시", "20시", "21시", "22시", "23시", "다음날", "1시", "2시", "3시", "4시", "5시", "6시", "7시", "8시", "9시", "10시", "11시", "12시", "13시", "14시", "15시", "16시"))
        xAxis.valueFormatter = IndexAxisValueFormatter(xAxisVals)
        xAxis.granularity = 1f
        //xAxis.granularity = 1f
        xAxis.textColor = Color.BLACK
        xAxis.position = XAxis.XAxisPosition.BOTTOM  // x축 라벨 위치
        xAxis.setDrawLabels(true)  // Grid-line 표시
        xAxis.setDrawAxisLine(true)  // Axis-Line 표시
        //왼쪽 y축
        val yLAxis = lineChart.axisLeft
        yLAxis.axisMinimum = 0f  // y축 최소값(고정)

        // 오른쪽 y축 값
        val yRAxis = lineChart.axisRight
        yRAxis.setDrawLabels(false)
        yRAxis.setDrawAxisLine(false)
        yRAxis.setDrawGridLines(false)

        lineChart.description.isEnabled = false
        lineChart.setVisibleXRangeMaximum(35f)
        lineChart.invalidate()
    }


    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        android.R.id.home -> {
            finish()
            true
        }
        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }
}