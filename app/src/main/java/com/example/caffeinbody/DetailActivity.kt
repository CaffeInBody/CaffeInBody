package com.example.caffeinbody

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.caffeinbody.databinding.ActivityDetailBinding
import com.example.caffeinbody.databinding.FragmentReportBinding
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*

class DetailActivity : AppCompatActivity() {
    private val lineChartData = ArrayList<Entry>()
    private val binding: ActivityDetailBinding by lazy {
        ActivityDetailBinding.inflate(
            layoutInflater
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        // Inflate the layout for this fragment
        lineChartData.add(Entry(3.toFloat(), 10.toFloat()));
        lineChartData.add(Entry(5.toFloat(), 1.toFloat()));
        lineChartData.add(Entry(7.toFloat(), 20.toFloat()));
        setChartView(binding.root)

    }



    private fun setChartView(view: View) {
      //  var chartWeek = binding.barchart
        var chartWeekLine = binding.linechart
     //   setWeek(chartWeek)
        setWeekLine(chartWeekLine)
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

    private fun setWeekLine(lineChart: LineChart) {


        val chartData = LineData()
        val set1 = LineDataSet(lineChartData, "첫번째")
        chartData.addDataSet(set1)
        lineChart.setData(chartData)
        lineChart.invalidate()
    }

    private fun setWeek(barChart: BarChart) {


        barChart.setScaleEnabled(false) //Zoom In/Out

        val valueList = ArrayList<Double>()
        val entries: ArrayList<BarEntry> = ArrayList()
        val title = "걸음 수"

        //input data
        for (i in 0..7) {
            valueList.add(i * 100.1)
        }

        //fit the data into a bar
        for (i in 0 until valueList.size) {
            val barEntry = BarEntry(i.toFloat(), valueList[i].toFloat())//월화수목금토일
            entries.add(barEntry)
        }
        val barDataSet = BarDataSet(entries, title)
        val data = BarData(barDataSet)
        barChart.data = data
        barChart.invalidate()
    }

    /*private fun setWeekLine(lineChart: LineChart) {
        initBarChart(lineChart)

        lineChart.setScaleEnabled(false) //Zoom In/Out

        val valueList = ArrayList<Double>()
        val entries: ArrayList<BarEntry> = ArrayList()
        val title = "걸음 수"

        //input data
        for (i in 0..5) {
            valueList.add(i * 100.1)
        }

        //fit the data into a bar
        for (i in 0 until valueList.size) {
            val barEntry = BarEntry(i.toFloat(), valueList[i].toFloat())
            entries.add(barEntry)
        }
        val barDataSet = BarDataSet(entries, title)
        val data = BarData(barDataSet)
        lineChart.data = data
        lineChart.invalidate()
    }*/



}