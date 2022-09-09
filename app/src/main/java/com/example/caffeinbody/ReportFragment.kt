package com.example.caffeinbody

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.caffeinbody.DetailActivity.Companion.getMonth
import com.example.caffeinbody.DetailActivity.Companion.getYear
import com.example.caffeinbody.databinding.FragmentReportBinding
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.*
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.CalendarMode
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.format.ArrayWeekDayFormatter
import com.prolificinteractive.materialcalendarview.format.MonthArrayTitleFormatter
import org.json.JSONArray
import org.json.JSONObject
import org.threeten.bp.DayOfWeek
import java.text.SimpleDateFormat
import java.util.*

class ReportFragment:Fragment() {
    private val binding: FragmentReportBinding by lazy {
        FragmentReportBinding.inflate(
            layoutInflater
        )
    }
    private var weekCafArray : Array<Float> = Array(7){0f}
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
        initCalendarView()
        initPersonal()
        initPieChart(binding.piechart)

        setCaffeineColorsDates(getMonth(), getYear())

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
    }

    fun saveWeekCafJson(date: Int, caffeine: Float, week: Int){
        val prefs = App.prefs.weekCafJson
        var tmpJsonRead = JSONArray()

        if (prefs == null){
            val tmpJsonObjectSave = JSONObject()
            for (i in 0..6){
                if (date - 1==i)
                    tmpJsonRead.put(caffeine)
                else
                    tmpJsonRead.put(0)
            }
            tmpJsonObjectSave.put(getMonth().toString(), tmpJsonRead)
        }
    }

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

        val valueList = ArrayList<Float?>()
        val entries: ArrayList<BarEntry> = ArrayList()
        val title = "요일별 카페인 섭취량"
        val dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)

        if(App.prefs.monCaf!=0f) weekCafArray[0] = App.prefs.monCaf!!
        if(App.prefs.tueCaf!=0f) weekCafArray[1] = App.prefs.tueCaf!!
        if(App.prefs.wedCaf!=0f) weekCafArray[2] = App.prefs.wedCaf!!
        if(App.prefs.thuCaf!=0f) weekCafArray[3] = App.prefs.thuCaf!!
        if(App.prefs.friCaf!=0f) weekCafArray[4] = App.prefs.friCaf!!
        if(App.prefs.satCaf!=0f) weekCafArray[5] = App.prefs.satCaf!!
        if(App.prefs.sunCaf!=0f) weekCafArray[6] = App.prefs.sunCaf!!

        when(dayOfWeek) {
            2 -> weekCafArray[0] = App.prefs.todayCaf!!
            3 -> weekCafArray[1] = App.prefs.todayCaf!!
            4 -> weekCafArray[2] = App.prefs.todayCaf!!
            5 -> weekCafArray[3] = App.prefs.todayCaf!!
            6 -> weekCafArray[4] = App.prefs.todayCaf!!
            7 -> weekCafArray[5] = App.prefs.todayCaf!!
            1 -> weekCafArray[6] = App.prefs.todayCaf!!
        }

        for (i in 0..6) {
            //여기에 요일별 카페인 수치 들어감
            valueList.add(weekCafArray.get(i))
            Log.e("weekCafArray", weekCafArray.toString())
            Log.e("weekCafJson", App.prefs.weekCafJson.toString())
            Log.e("dayCaffeine", App.prefs.todayCaf.toString())
        }


        //fit the data into a bar
        for (i in 1 until valueList.size + 1) {
            val barEntry = BarEntry(i.toFloat(), valueList[i - 1]!!.toFloat())//월화수목금토일
            entries.add(barEntry)
        }
        val barDataSet = BarDataSet(entries, title)
        val data = BarData(barDataSet)
        barChart.data = data
        barChart.invalidate()
    }


    private fun initPieChart(pieChart: PieChart){
        //TODO 일주일 기준
        //  적정 섭취량에 맞게 잘 마시고 있으면 카페인을 잘 섭취하고 있어요
        //  적정 섭취량을 넘었으면 다음부터는 조금만 마셔요
        //  파이차트 백분율 기준 잘 마셨으면 만점, 적정 섭취량을 넘었을 때마다 감점
        //  데이터가 쌓였으면 binding.piecharttext visible = GONE
        //  반대로 데이터가 없으면 binding.piechart visible = GONE

    }

    private fun initPersonal(){
        var age = "나이"
        var gender = "성별"
        var isPregnant = "임신여부"
        //TODO 현재 나이,상태 등 + 섭취습관(설문조사 내용 sharedpreference에 넣고 여기에 반영
        when(App.prefs.age){
            "minor" -> age = "미성년자"
            "adult" -> age = "성인"
            "senior" -> age = "노인"
        }
        when(App.prefs.gender){
            "male" -> gender = "남성"
            "female" -> gender = "여성"
        }
        when(App.prefs.isPregnant){
            true -> isPregnant = "임신 중"
            false -> isPregnant = "임신하지 않음"
        }
        binding.textView3.text = age + " / " + gender + " / " + isPregnant
        binding.awakenumber.text = App.prefs.awakenumber.toString()
        binding.habitnumber.text = App.prefs.habitnumber.toString()
        binding.tastenumber.text = App.prefs.tastenumber.toString()
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

    fun initCalendarView(){
        var test = App.prefs.monthCafJson
        Log.e("report", "year "+ getYear() + "month" + getMonth() + test.toString())
         binding.calendarview.state().edit()
            //.setMinimumDate(CalendarDay.from(getYear(), getMonth(), 1))
             //.setMaximumDate(CalendarDay.from(getYear(), getMonth(), 31))
            //.setFirstDayOfWeek(DayOfWeek.of(Calendar.SUNDAY))
            .commit()

        //캘린더 헤더, 날짜, 요일 부분 디자인 설정
        binding.calendarview.setHeaderTextAppearance(R.style.CalendarWidgetHeader)
        binding.calendarview.setDateTextAppearance(R.style.CalenderViewDateCustomText)
        binding.calendarview.setWeekDayTextAppearance(R.style.CalenderViewWeekCustomText)
        //binding.calendarview.setWeekDayFormatter(ArrayWeekDayFormatter(resources.getTextArray(R.array.custom_weekdays)))
        //binding.calendarview.setTitleFormatter(MonthArrayTitleFormatter(getResources().getTextArray(R.array.custom_months)))
        //binding.calendarview.setWeekDayFormatter(ArrayWeekDayFormatter(getResources().getTextArray(R.array.custom_weekdays)))


        // 요일 선택 시 내가 정의한 드로어블이 적용되도록 함
        binding.calendarview.setOnRangeSelectedListener({
                widget, dates ->
                val startDay = dates[0].date.toString()
                val endDay = dates[dates.size - 1].date.toString()
                Log.e("Calendar", "시작일 : $startDay, 종료일 : $endDay")
        })

        binding.calendarview.setOnMonthChangedListener { widget, date ->
            //Log.e("ReportFragment", "현재 캘린더의 month: " + date.month + " year: "+ date.year)
            setCaffeineColorsDates(date.month, date.year)
        }

        /*binding.calendarview.addDecorators(DayDecoratorGreen(context, CalendarDay.from(2022, 7, 20)))
        binding.calendarview.addDecorators(DayDecoratorRed(context, CalendarDay.from(2022, 7, 23)))*/
    }

    /* 선택된 요일의 background를 설정하는 Decorator 클래스 */
    private class DayDecoratorGreen(context: Context?, currentDay: CalendarDay) : DayViewDecorator {
        private val drawable: Drawable?
        private var myDay = currentDay
        override fun shouldDecorate(day: CalendarDay): Boolean {
            return myDay == day
        }

        override fun decorate(view: DayViewFacade) {
            view.setSelectionDrawable(drawable!!)
        }

        init {
            drawable = ContextCompat.getDrawable(context!!, R.drawable.calendar_caffeine_color1)
        }
    }

    private class DayDecoratorYellow(context: Context?, currentDay: CalendarDay) : DayViewDecorator {
        private val drawable: Drawable?
        private var myDay = currentDay
        override fun shouldDecorate(day: CalendarDay): Boolean {
            return myDay == day
        }

        override fun decorate(view: DayViewFacade) {
            view.setSelectionDrawable(drawable!!)
        }

        init {
            drawable = ContextCompat.getDrawable(context!!, R.drawable.calendar_caffeine_color2)
        }
    }

    private class DayDecoratorRed(context: Context?, currentDay: CalendarDay) : DayViewDecorator {
        private val drawable: Drawable?
        private var myDay = currentDay
        override fun shouldDecorate(day: CalendarDay): Boolean {
            return myDay == day
        }

        override fun decorate(view: DayViewFacade) {
            view.setSelectionDrawable(drawable!!)
        }

        init {
            drawable = ContextCompat.getDrawable(context!!, R.drawable.calendar_caffeine_color3)
        }
    }

    fun setCaffeineColorsDates(month: Int, year: Int){//날짜별 저장된 카페인 색상 정보 가져와 달력에 표시하기
        if (getMonthCafJson(1, month, year) == "error"){
            return
        }
        for (i in 1.. 31){
            if (getMonthCafJson(i, month, year).toInt() == 1){
                binding.calendarview.addDecorators(DayDecoratorGreen(context, (CalendarDay.from(year, month, i))))
                Log.e("ReportFragment", "green $i")
            }else if (getMonthCafJson(i, month, year).toInt() == 2){
                binding.calendarview.addDecorators(DayDecoratorYellow(context, (CalendarDay.from(year, month, i))))
                Log.e("ReportFragment", "yellow $i")
            }else if(getMonthCafJson(i, month, year).toInt() == 3){
                binding.calendarview.addDecorators(DayDecoratorRed(context, (CalendarDay.from(year, month, i))))
                Log.e("ReportFragment", "red $i")
            }else//아직 저장 안된 값
                Log.e("ReportFragment", "monthCafJson에 아직 값이 저장 안됨 $i")
        }
    }


    companion object{
        fun saveMonthCafJson(date: Int, color: Int, month: Int){//특정 날짜에 해당하는 색상 저장하기
            //val caffeineColors: MutableList<Int> = mutableListOf<Int>(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)
            val prefs = App.prefs.monthCafJson
            var tmpJsonRead = JSONArray()

            if (prefs == null){//아예 처음 저장
                val tmpJsonObjectSave = JSONObject()
                for (i in 0.. 30) {
                    if (date - 1 == i)
                        tmpJsonRead.put(color)
                    else
                        tmpJsonRead.put(0)
                }
                tmpJsonObjectSave.put(getYear().toString()+"_"+getMonth().toString(), tmpJsonRead)
                App.prefs.monthCafJson = tmpJsonObjectSave.toString()
                Log.e("ReportFragment", "monthCafJson비어있음")
            }else{
                val tmpJsonObjectRead = JSONObject(prefs)
                var tmpJsonArray2 = tmpJsonObjectRead.optString(getYear().toString()+"_"+getMonth().toString())
                var tmpJsonArraySave = JSONArray()//저장용
                if (tmpJsonArray2 == ""){//새로운 달에 저장
                    for (i in 0.. 30) {
                        if (date - 1 == i)
                            tmpJsonRead.put(color)
                        else
                            tmpJsonRead.put(0)
                    }
                    tmpJsonObjectRead.put(getYear().toString()+"_"+getMonth().toString(), tmpJsonRead)
                    App.prefs.monthCafJson = tmpJsonObjectRead.toString()
                    Log.e("ReportFragment", "새로운 달 저장")
                }else {//기존 달 데이터에 저장
                    tmpJsonRead = JSONArray(tmpJsonArray2)//참고용
                    for( i in 0 .. tmpJsonRead.length() -1){
                        //caffeineColors[i] = tmpJsonArray1.optString(i).toInt()////////
                        if(date - 1 == i){//오늘날짜에 해당하는 값 저장
                            tmpJsonArraySave.put(color)
                        }else
                            tmpJsonArraySave.put(tmpJsonRead.optString(i).toInt())

                    }
                    tmpJsonObjectRead.put(getYear().toString()+"_"+getMonth().toString(), tmpJsonArraySave)
                    App.prefs.monthCafJson = tmpJsonObjectRead.toString()
                    Log.e("report", "기존 달")
                }
                Log.e("ReportFragment", tmpJsonObjectRead.toString())
            }
        }

        fun getMonthCafJson(date: Int, month: Int, year:Int): String{//특정 날짜의 색상 정보 가져오기
            //Log.e("report", "month: $month, date: $date, year: $year")
            val prefsArray = App.prefs.monthCafJson
            if (prefsArray == null) {
                Log.e("ReportFragment", "아직 저장되지 않음")
                return "error"
            }else{
                val tmpJsonObject = JSONObject(prefsArray)
                var tmpJsonArray1 = tmpJsonObject.optString(year.toString()+"_"+month.toString())
                if (tmpJsonArray1 == ""){//아직 데이터가 저장되지 않은 달이면(optString()은 값이 없을 시 ""를 반환한다)
                    Log.e("ReportFragment", "저장되지 않은 달의 데이터")
                    return "error"//0이면 아무것도 못하게
                }else{
                    return JSONArray(tmpJsonArray1).optString(date -1 )
                }
            }
        }

        fun calMonthCaffeineColor(todayCaf: Float): Int{
            val dayCaffeine = App.prefs.dayCaffeine?.toFloat()
            if (todayCaf > dayCaffeine!!){
                return 3 //빨강(권장량 이상)
            }else if(todayCaf > dayCaffeine!!/2){
                return 2//노랑(권장량의 반 이상)
            }else{
                return 1//초록(권장량의 반 이하)
            }
        }
    }
    //object로 바꾸기


}



