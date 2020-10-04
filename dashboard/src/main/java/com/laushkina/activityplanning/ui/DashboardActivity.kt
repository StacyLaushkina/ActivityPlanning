package com.laushkina.activityplanning.ui

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Toast
import com.laushkina.activityplanning.component.dashboard.R
import com.laushkina.activityplanning.di.ContextModule
import com.laushkina.activityplanning.di.DaggerDashboardPresenterComponent
import com.laushkina.activityplanning.di.DashboardViewModule
import kotlinx.android.synthetic.main.dashboard_activity.*
import org.eazegraph.lib.models.PieModel
import java.util.*

class DashboardActivity: BaseActivity(), DashboardView {
    private lateinit var presenter: DashboardPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.dashboard_activity)
        initToolbar("Dashboard", true)

        presenter = DaggerDashboardPresenterComponent
            .builder()
            .contextModule(ContextModule(applicationContext))
            .dashboardViewModule(DashboardViewModule(this))
            .build()
            .getDashboardPresenter()

        presenter.onCreate()
        date_view.setOnClickListener { presenter.onDateChangeRequested() }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }

    override fun showChart(pieSlices: List<PieModel>) {
        piechart.clearChart()
        for (item in pieSlices) {
            piechart.addPieSlice(item)
        }
        piechart.startAnimation()
    }

    override fun setDate(date: String) {
        date_view.text = date
    }

    override fun showError(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun openDateSelection(maxDate: Long) {
        val onDateChange = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            presenter.onDateChange(year, monthOfYear, dayOfMonth)
        }

        val calendar = Calendar.getInstance()
        val dateDialog = DatePickerDialog(
            this,
            R.style.DialogTheme,
            onDateChange,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        dateDialog.datePicker.maxDate = maxDate
        dateDialog.setTitle("") // Title duplicates selected date
        dateDialog.show()
    }
}