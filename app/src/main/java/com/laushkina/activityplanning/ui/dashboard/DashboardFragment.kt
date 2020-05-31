package com.laushkina.activityplanning.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.laushkina.activityplanning.R
import com.laushkina.activityplanning.di.ContextModule
import com.laushkina.activityplanning.di.dashboard.DaggerDashboardPresenterComponent
import com.laushkina.activityplanning.di.dashboard.DashboardViewModule
import org.eazegraph.lib.charts.PieChart
import org.eazegraph.lib.models.PieModel

class DashboardFragment : Fragment(), DashboardView {
    private lateinit var chart: PieChart
    private lateinit var presenter: DashboardPresenter
    private lateinit var dateView: TextView

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

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.onDestroy()
    }

    override fun showChart(pieSlices: List<PieModel>) {
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
}
