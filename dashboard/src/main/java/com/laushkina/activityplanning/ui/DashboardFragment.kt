package com.laushkina.activityplanning.ui

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.laushkina.activityplanning.component.dashboard.R
import com.laushkina.activityplanning.di.ContextModule
import com.laushkina.activityplanning.di.DashboardViewModule
import com.laushkina.activityplanning.di.DaggerDashboardPresenterComponent
import org.eazegraph.lib.charts.PieChart
import org.eazegraph.lib.models.PieModel
import java.util.*

class DashboardFragment : Fragment(), DashboardView {
    private lateinit var chart: PieChart
    private lateinit var presenter: DashboardPresenter
    private lateinit var dateView: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)

        dateView = root.findViewById(R.id.date)

        chart = root.findViewById(R.id.piechart)
        presenter = DaggerDashboardPresenterComponent
            .builder()
            .contextModule(ContextModule(requireContext().applicationContext))
            .dashboardViewModule(DashboardViewModule(this))
            .build()
            .getDashboardPresenter()

        presenter.onCreate()

        dateView.setOnClickListener { presenter.onDateChangeRequested() }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.onDestroy()
    }

    override fun showChart(pieSlices: List<PieModel>) {
        chart.clearChart()
        for (item in pieSlices) {
            chart.addPieSlice(item)
        }
        chart.startAnimation()
    }

    override fun setDate(date: String) {
        dateView.text = date
    }

    override fun showError(message: String?) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    override fun openDateSelection(maxDate: Long) {
        val onDateChange = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            presenter.onDateChange(year, monthOfYear, dayOfMonth)
        }

        val calendar = Calendar.getInstance()
        val dateDialog = DatePickerDialog(
            requireContext(),
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
