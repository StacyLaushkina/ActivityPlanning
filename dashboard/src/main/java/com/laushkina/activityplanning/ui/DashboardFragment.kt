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
import kotlinx.android.synthetic.main.fragment_dashboard.*
import org.eazegraph.lib.charts.PieChart
import org.eazegraph.lib.models.PieModel
import java.util.*

class DashboardFragment : Fragment(), DashboardView {
    private lateinit var presenter: DashboardPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = DaggerDashboardPresenterComponent
            .builder()
            .contextModule(ContextModule(requireContext().applicationContext))
            .dashboardViewModule(DashboardViewModule(this))
            .build()
            .getDashboardPresenter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.onCreate()
        date_view.setOnClickListener { presenter.onDateChangeRequested() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
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
