package ua.kpi.comsys.ip8311

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.*
import com.google.android.material.tabs.TabLayout
import kotlin.math.cos
import android.graphics.Color

class GraphActivity : Fragment () {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.graph_activity, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val lineChart: LineChart = view.findViewById(R.id.lineChart)
        val pieChart: PieChart = view.findViewById(R.id.pieChart)
        val tabLayout : TabLayout = view.findViewById(R.id.tabs)

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                if(tab.getPosition() == 0) {
                    lineChart.visibility = View.VISIBLE
                    pieChart.visibility = View.INVISIBLE
                } else {
                    lineChart.visibility = View.INVISIBLE
                    pieChart.visibility = View.VISIBLE
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        val entries = ArrayList<Entry>()

        for(i in -100..100){
            val x = (i.toFloat() / 100f) * Math.PI
            val y = cos(x)
            entries.add(Entry(x.toFloat(), y.toFloat()))
        }

        val vl = LineDataSet(entries, "cos(x)")

        vl.setDrawValues(false)
        vl.setDrawFilled(true)
        vl.lineWidth = 3f
        vl.fillColor = R.color.black
        vl.fillAlpha = R.color.black
        lineChart.xAxis.labelRotationAngle = 0f
        lineChart.data = LineData(vl)
        lineChart.xAxis.axisMaximum = 20.1f
        lineChart.xAxis.axisMinimum = -20.1f

        val listPie = arrayListOf(PieEntry(45F, ""), PieEntry(5F, ""), PieEntry(25F, ""), PieEntry(25F, ""))
        val listColors = arrayListOf(Color.BLUE, Color.argb(100, 166, 0, 210), Color.YELLOW, Color.GRAY)
        val pieDataSet = PieDataSet(listPie, "")
        pieDataSet.colors = listColors

        val pieData = PieData(pieDataSet)
        pieData.setValueTextSize(15f)
        pieChart.data = pieData

        pieChart.setUsePercentValues(true)
        pieChart.isDrawHoleEnabled = false
        pieChart.description.isEnabled = false
        pieChart.setEntryLabelColor(0)
        pieChart.animateY(1400, Easing.EaseInOutQuad)
    }
}