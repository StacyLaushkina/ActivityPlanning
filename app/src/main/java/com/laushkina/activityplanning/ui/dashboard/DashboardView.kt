package com.laushkina.activityplanning.ui.dashboard

import org.eazegraph.lib.models.PieModel

interface DashboardView {
    fun showChart(pieSlices: List<PieModel>)
    fun setDate(date: String)
    fun showError(message: String?)
}