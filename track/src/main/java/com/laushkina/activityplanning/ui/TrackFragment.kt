package com.laushkina.activityplanning.ui

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
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
import com.laushkina.activityplanning.component.track.R
import com.laushkina.activityplanning.di.ContextModule
import com.laushkina.activityplanning.di.DaggerTrackPresenterComponent
import com.laushkina.activityplanning.di.TrackViewModule
import com.laushkina.activityplanning.model.track.Track
import java.util.*

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

        dateView.setOnClickListener { presenter.onDateChangeRequested() }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.onDestroy()
    }

    override fun showTracks(tracks: List<Track>) {
        plansAdapter = TrackAdapter(tracks, this)
        plansRecycler.adapter = plansAdapter
        plansRecycler.layoutManager = GridLayoutManager(context, 1)
    }

    override fun showDate(date: String) {
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

        dialog.show(requireFragmentManager(), TrackResultsDialog::javaClass.name)
    }

    override fun showStartTrackingButton() {
        startButton.visibility = View.VISIBLE
        startButton.setOnClickListener { presenter.onStartTrackingRequested() }
    }

    override fun hideStartTrackingButton() {
        startButton.visibility = View.GONE
    }

    override fun showEndTrackingButton() {
        endButton.visibility = View.VISIBLE
        endButton.setOnClickListener { presenter.onEndTrackingRequested() }
    }

    override fun hideEndTrackingButton() {
        endButton.visibility = View.GONE
    }

    override fun updateTimes() {
        plansAdapter.updateTime()
    }

    override fun openDateSelection(maxDate: Long) {
        val onDateChange = OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
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

    override fun onTrackStart(track: Track) {
        presenter.onTrackStart(track)
    }

    override fun onTrackStop(track: Track) {
        presenter.onTrackStop(track)
    }
}
