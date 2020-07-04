package com.laushkina.activityplanning.ui.dashboard

import android.graphics.Color

class DashboardColors {
    private val colors = listOf(
        Color.parseColor("#FE6DA8"),
        Color.parseColor("#56B7F1"),
        Color.parseColor("#CDA67F"),
        Color.parseColor(("#FED70E"))
    )
    private var ind = -1

    fun getNextColor(): Int {
        ind += 1
        return colors[ind % colors.size]
    }
}