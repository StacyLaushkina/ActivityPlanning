package com.laushkina.activityplanning.ui

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import com.laushkina.activityplanning.component.track.R
import com.laushkina.activityplanning.di.ContextModule
import com.laushkina.activityplanning.di.DaggerTrackPresenterComponent
import com.laushkina.activityplanning.di.TrackViewModule
import com.laushkina.activityplanning.model.track.Track
import com.laushkina.activityplanning.ui.navigation.NavigationState
import kotlinx.android.synthetic.main.track_activity.*
import java.util.*

class TrackActivity : BaseActivity(), TrackView, TrackAdapter.TrackChangeListener {
    private lateinit var presenter: TrackPresenter
    private lateinit var plansAdapter: TrackAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.track_activity)

        initToolbar("Tracking", false)

        presenter = DaggerTrackPresenterComponent.builder()
            .contextModule(ContextModule(applicationContext))
            .trackViewModule(TrackViewModule(this))
            .build()
            .getTrackPresenter()

        presenter.onCreate()
        track_date.setOnClickListener { presenter.onDateChangeRequested() }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }

    override fun showTracks(tracks: List<Track>, showControlButtons: Boolean) {
        plansAdapter = TrackAdapter(tracks, showControlButtons, this)
        tracks_recycler.adapter = plansAdapter
        tracks_recycler.layoutManager = GridLayoutManager(this, 1)
    }

    override fun showDate(date: String) {
        track_date.text = date
    }

    override fun showError(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun showMessage(message: String) {
        val dialog: DialogFragment = TrackResultsDialog()

        val extras = Bundle()
        extras.putString(TrackResultsDialog.MESSAGE_EXTRA, message)
        dialog.arguments = extras

        dialog.show(supportFragmentManager, TrackResultsDialog::javaClass.name)
    }

    override fun showStartTrackingButton() {
        start_tracking.visibility = View.VISIBLE
        start_tracking.setOnClickListener { presenter.onStartTracksForToday() }
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

    override fun onTrackStart(track: Track) {
        presenter.onTrackStart(track)
    }

    override fun onTrackStop(track: Track) {
        presenter.onTrackStop(track)
    }
}