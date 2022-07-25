package com.example.caffeinbody

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.graphics.Color.Companion.Red
import com.example.caffeinbody.databinding.FragmentReportBinding
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.*
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ReportFragment : Fragment() {
    private val binding: FragmentReportBinding by lazy {
        FragmentReportBinding.inflate(
            layoutInflater
        )
    }

    private var weekCafArray : ArrayList<Int?> = arrayListOf(0,0,0,0,0,0,0)
    private val nowTime = Calendar.getInstance().getTime()
    private val weekdayFormat = SimpleDateFormat("EE", Locale.getDefault())
    private val weekDay = weekdayFormat.format(nowTime)
    private val curTimeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    private val curTime = curTimeFormat.format(nowTime)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        setChartView(binding.root)
        return binding.root
    }

    fun resetAlarm() {
        val resetAlarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val resetIntent = Intent(context, ResetTodayCaf::class.java)
        val resetSender = PendingIntent.getBroadcast(context, 0, resetIntent, PendingIntent.FLAG_IMMUTABLE)

        // 자정 시간
        val resetCal = Calendar.getInstance()
        resetCal.timeInMillis = System.currentTimeMillis()
        resetCal[Calendar.HOUR_OF_DAY] = 0
        resetCal[Calendar.MINUTE] = 0
        resetCal[Calendar.SECOND] = 0

        //다음날 0시에 맞추기 위해 24시간을 뜻하는 상수인 AlarmManager.INTERVAL_DAY를 더해줌.
        resetAlarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP, resetCal.timeInMillis
                    + AlarmManager.INTERVAL_DAY, AlarmManager.INTERVAL_DAY, resetSender
        )
        val format = SimpleDateFormat("MM/dd kk:mm:ss")
        val setResetTime = format.format(Date(resetCal.timeInMillis + AlarmManager.INTERVAL_DAY))
        Log.d("resetAlarm", "ResetHour : $setResetTime")
    }

    fun setWeeks(){

        Log.e("time: 요일", weekDay)
        Log.e("curTime: 시간", curTime)

        when(weekDay){
            "월" -> {
                if (curTime <= "23:59:50") {
                    //App.prefs.weekCafJson.set(0,App.prefs.todayCaf.toString())
                    weekCafArray.set(0, App.prefs.todayCaf)
                    Log.e("mon", App.prefs.weekCafJson.toString())
                }
                else {
                    App.prefs.weekCafJson= arrayListOf(weekCafArray.toString())
                    Log.e("weekCafJson", App.prefs.weekCafJson.toString())
                    App.prefs.todayCaf = 0
                }
                App.prefs.weekCafJson
            }
            "화" -> {
                if (curTime <= "23:59:30") {
                    weekCafArray.set(1, App.prefs.todayCaf)
                    Log.e("tue", weekCafArray.toString())
                }
                else {
                    App.prefs.weekCafJson.add(App.prefs.todayCaf.toString())
                    App.prefs.todayCaf = 0
                }
            }
            "수" -> {
                if (curTime <= "23:59:30") {
                    weekCafArray.set(2, App.prefs.todayCaf)
                    Log.e("wed", weekCafArray.toString())
                }
                else {
                    App.prefs.weekCafJson.add(App.prefs.todayCaf.toString())
                    App.prefs.todayCaf = 0
                }
            }
            "목" -> {
                if (curTime <= "23:59:30") {
                    weekCafArray.set(3, App.prefs.todayCaf)
                    Log.e("thu", weekCafArray.toString())
                }
                else {
                    App.prefs.weekCafJson.add(App.prefs.todayCaf.toString())
                   App.prefs.todayCaf = 0
                }
            }
            "금" -> {
                if (curTime <= "23:59:30") {
                    weekCafArray.set(4, App.prefs.todayCaf)
                    Log.e("fri", weekCafArray.toString())
                }
                else {
                    App.prefs.weekCafJson.add(App.prefs.todayCaf.toString())
                    App.prefs.todayCaf = 0
                }
            }
            "토" -> {
                if (curTime <= "23:59:30") {
                    weekCafArray.set(5, App.prefs.todayCaf)
                    Log.e("sat", weekCafArray.toString())
                }
                else {
                    App.prefs.weekCafJson.add(App.prefs.todayCaf.toString())
                    App.prefs.todayCaf = 0
                }
            }
            "일" -> {
                if (curTime <= "23:59:30") {
                    weekCafArray.set(6, App.prefs.todayCaf)
                    Log.e("sun", weekCafArray.toString())
                }
                else {
                    App.prefs.weekCafJson.add(App.prefs.todayCaf.toString())
                    App.prefs.todayCaf = 0
                }
            }
        }
    }

    /*fun initWeekJson(){//room과 함께 처음 실행할 때만 생성하기
        var blank = App.prefs.weekCafJson
        if (blank != null){
            var a = JSONArray(blank)
            for (i in 0..7){
                a.put("")
            }
            App.prefs.date = a.toString()
            Log.e("json length", " // " + a.length())
        }else{
            Log.e("initWeekJson", "error")
        }
    }*/



    private fun setChartView(view: View) {
        var chartWeek = binding.barchart
        //var chartWeekLine = binding.linechart
        setWeek(chartWeek)
        //setWeekLine(chartWeekLine)
    }

    private fun initBarDataSet(barDataSet: BarDataSet) {
        //Changing the color of the bar
        barDataSet.color = Color.parseColor("#304567")
        //Setting the size of the form in the legend
        barDataSet.formSize = 15f
        //showing the value of the bar, default true if not set
        barDataSet.setDrawValues(false)
        //setting the text size of the value of the bar
        barDataSet.valueTextSize = 12f
    }

    private fun setWeek(barChart: BarChart) {
//        for (i in 0..6) {
//            weekCafArray.add(0)
//        }
        setWeeks()
        resetAlarm()
        initBarChart(barChart)

        barChart.setScaleEnabled(false) //Zoom In/Out

        val valueList = ArrayList<Int?>()
        val entries: ArrayList<BarEntry> = ArrayList()
        val title = "요일별 카페인 섭취량"

        for (i in 0..6) {
            //여기에 요일별 카페인 수치 들어감
            valueList.add(weekCafArray.get(i))
            Log.e("weekCafArray", weekCafArray.toString())
            Log.e("weekCafJson", App.prefs.weekCafJson.toString())
            Log.e("dayCaffeine", App.prefs.todayCaf.toString())
//            if (App.prefs.todayCaf==null){
//                valueList.add(0)
//            }
//            else {
//                valueList.add(App.prefs.todayCaf)
//            }
        }


        //fit the data into a bar
        for (i in 1 until valueList.size + 1) {
            Log.e("size", valueList.size.toString())
            val barEntry = BarEntry(i.toFloat(), valueList[i - 1]!!.toFloat())//월화수목금토일
            entries.add(barEntry)
        }
        val barDataSet = BarDataSet(entries, title)
        val data = BarData(barDataSet)
        barChart.data = data
        barChart.invalidate()
    }

    private fun initBarChart(barChart: BarChart) {
        //hiding the grey background of the chart, default false if not set
        barChart.setDrawGridBackground(false)
        //remove the bar shadow, default false if not set
        barChart.setDrawBarShadow(false)
        //remove border of the chart, default false if not set
        barChart.setDrawBorders(false)

        //remove the description label text located at the lower right corner
        val description = Description()
        description.setEnabled(false)
        barChart.setDescription(description)

        //X, Y 바의 애니메이션 효과
        barChart.animateY(1000)
        barChart.animateX(1000)


        //바텀 좌표 값
        val xAxis: XAxis = barChart.getXAxis()
        //change the position of x-axis to the bottom
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        //set the horizontal distance of the grid line
        xAxis.granularity = 1f
        xAxis.textColor = Color.DKGRAY
        //hiding the x-axis line, default true if not set
        xAxis.setDrawAxisLine(false)
        //hiding the vertical grid lines, default true if not set
        xAxis.setDrawGridLines(false)
        xAxis.valueFormatter = MyXAxisFormatter()
        xAxis.textSize = 14f


        //좌측 값 hiding the left y-axis line, default true if not set
        val leftAxis: YAxis = barChart.getAxisLeft()
        leftAxis.setDrawAxisLine(false)
        leftAxis.textColor = Color.DKGRAY


        //우측 값 hiding the right y-axis line, default true if not set
        val rightAxis: YAxis = barChart.getAxisRight()
       // rightAxis.setDrawAxisLine(false)
       // rightAxis.set
        rightAxis.isEnabled = false

        //바차트의 타이틀
        val legend: Legend = barChart.getLegend()
        //setting the shape of the legend form to line, default square shape
        legend.form = Legend.LegendForm.LINE
        //setting the text size of the legend
        legend.textSize = 11f
        legend.textColor = Color.DKGRAY
        //setting the alignment of legend toward the chart
        legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        //setting the stacking direction of legend
        legend.orientation = Legend.LegendOrientation.HORIZONTAL
        //setting the location of legend outside the chart, default false if not set
        legend.setDrawInside(false)
    }

    inner class MyXAxisFormatter : ValueFormatter(){
        private val days = arrayOf("월","화","수","목","금","토","일")
        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            return days.getOrNull(value.toInt()-1) ?: value.toString()
        }
    }
}