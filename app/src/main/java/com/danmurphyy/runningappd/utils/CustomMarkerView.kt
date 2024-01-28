package com.danmurphyy.runningappd.utils

import android.annotation.SuppressLint
import android.content.Context
import android.widget.TextView
import com.danmurphyy.runningappd.R
import com.danmurphyy.runningappd.db.Run
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@SuppressLint("ViewConstructor")
class CustomMarkerView(
    private val runs: List<Run>,
    c: Context,
) : MarkerView(c, R.layout.marker_view) {

    private val tvDate: TextView = findViewById(R.id.markerTvDate)
    private val tvAvgSpeed: TextView = findViewById(R.id.markerTvAvgSpeed)
    private val tvDistance: TextView = findViewById(R.id.markerTvDistance)
    private val tvDuration: TextView = findViewById(R.id.markerTvDuration)
    private val tvCaloriesBurned: TextView = findViewById(R.id.markerTvCaloriesBurned)

    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        super.refreshContent(e, highlight)
        if (e == null) return
        val curRunId = e.x.toInt()
        val currentRun = runs[curRunId]

        val calendar = Calendar.getInstance().apply {
            timeInMillis = currentRun.timestamp
        }
        val dateFormat = SimpleDateFormat("dd.MM.yy", Locale.getDefault())
        tvDate.text = dateFormat.format(calendar.time)

        val avgSpeed = "${currentRun.avgSpeedInKMH}km/h"
        tvAvgSpeed.text = avgSpeed

        val distanceInKm = "${currentRun.distanceInMeters / 1000f}km"
        tvDistance.text = distanceInKm

        tvDuration.text = TrackingUtils.getFormattedStopWatchTime(currentRun.timeInMillis)

        val caloriesBurned = "${currentRun.caloriesBurned}kcal"
        tvCaloriesBurned.text = caloriesBurned
    }
}
