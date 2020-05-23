package com.laushkina.activityplanning.ui.dashboard

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.laushkina.activityplanning.R
import com.laushkina.activityplanning.model.track.TrackService
import com.laushkina.activityplanning.repository.db.track.TrackDBRepository
import org.eazegraph.lib.charts.PieChart
import org.eazegraph.lib.models.PieModel

class DashboardFragment : Fragment(), DashboardView {
    private lateinit var chart: PieChart
    private lateinit var presenter: DashboardPresenter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)

        chart = root.findViewById(R.id.piechart)
        presenter = DashboardPresenter(this, TrackService(TrackDBRepository(context?.applicationContext!!)))
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

    override fun showError(message: String?) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
}
