package com.laushkina.activityplanning.ui.track

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.laushkina.activityplanning.R
import com.laushkina.activityplanning.di.ContextModule
import com.laushkina.activityplanning.di.track.DaggerTrackPresenterComponent
import com.laushkina.activityplanning.di.track.TrackViewModule
import com.laushkina.activityplanning.model.track.Track
import com.laushkina.activityplanning.ui.plan.NewPlanDialog

class TrackFragment : Fragment(), TrackView, TrackAdapter.TrackChangeListener {
    private lateinit var presenter: TrackPresenter
    private lateinit var plansRecycler: RecyclerView
    private lateinit var plansAdapter: TrackAdapter
    private lateinit var startButton: Button
    private lateinit var endButton: Button
    private lateinit var dateView: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_track, container, false)

        startButton = root.findViewById(R.id.start_tracking)
        endButton = root.findViewById(R.id.end_tracking)
        dateView = root.findViewById(R.id.date)

        presenter = DaggerTrackPresenterComponent.builder()
            .contextModule(ContextModule(requireContext().applicationContext))
            .trackViewModule(TrackViewModule(this))
            .build()
            .getTrackPresenter()

        presenter.onCreate()
        plansRecycler = root.findViewById(R.id.tracks)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.onDestroy()
    }

    override fun showTracks(tracks: List<Track>, date: String) {
        plansAdapter = TrackAdapter(tracks, this)
        plansRecycler.adapter = plansAdapter
        plansRecycler.layoutManager = GridLayoutManager(context, 1)

        dateView.text = date
    }

    override fun showError(message: String?) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    override fun showMessage(message: String) {
        val dialog: DialogFragment = TrackResultsDialog()

        val extras = Bundle()
        extras.putString(TrackResultsDialog.MESSAGE_EXTRA, message)
        dialog.arguments = extras

        dialog.show(requireFragmentManager(), NewPlanDialog::javaClass.name)
    }

    override fun showStartTrackingButton() {
        startButton.visibility = View.VISIBLE
        startButton.setOnClickListener{ presenter.onStartTrackingRequested() }
    }

    override fun hideStartTrackingButton() {
        startButton.visibility = View.GONE
    }

    override fun showEndTrackingButton() {
        endButton.visibility = View.VISIBLE
        endButton.setOnClickListener{ presenter.onEndTrackingRequested() }
    }

    override fun hideEndTrackingButton() {
        endButton.visibility = View.GONE
    }

    override fun onTrackStart(ind: Int, track: Track) {
        presenter.onTrackStart(ind, track)
    }

    override fun onTrackContinue(ind: Int, track: Track) {
        presenter.onTrackContinue(ind, track)
    }

    override fun onTrackFinish(ind: Int, track: Track) {
        presenter.onTrackFinish(ind, track)
    }
}
