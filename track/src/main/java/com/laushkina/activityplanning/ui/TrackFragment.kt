package com.laushkina.activityplanning.ui

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.laushkina.activityplanning.component.track.R
import com.laushkina.activityplanning.di.ContextModule
import com.laushkina.activityplanning.di.DaggerTrackPresenterComponent
import com.laushkina.activityplanning.di.TrackViewModule
import com.laushkina.activityplanning.model.track.Track
import java.util.*
import kotlinx.android.synthetic.main.fragment_track.*

class TrackFragment : Fragment(), TrackView, TrackAdapter.TrackChangeListener {
    private lateinit var presenter: TrackPresenter
    private lateinit var plansAdapter: TrackAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        presenter = DaggerTrackPresenterComponent.builder()
            .contextModule(ContextModule(requireContext().applicationContext))
            .trackViewModule(TrackViewModule(this))
            .build()
            .getTrackPresenter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_track, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.onCreate()
        track_date.setOnClickListener { presenter.onDateChangeRequested() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.onDestroy()
    }

    override fun showTracks(tracks: List<Track>) {
        plansAdapter = TrackAdapter(tracks, this)
        tracks_recycler.adapter = plansAdapter
        tracks_recycler.layoutManager = GridLayoutManager(context, 1)
    }

    override fun showDate(date: String) {
        track_date.text = date
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
        start_tracking.visibility = View.VISIBLE
        start_tracking.setOnClickListener { presenter.onStartTrackingRequested() }
    }

    override fun hideStartTrackingButton() {
        start_tracking.visibility = View.GONE
    }

    override fun showEndTrackingButton() {
        end_tracking.visibility = View.VISIBLE
        end_tracking.setOnClickListener { presenter.onEndTrackingRequested() }
    }

    override fun hideEndTrackingButton() {
        end_tracking.visibility = View.GONE
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
