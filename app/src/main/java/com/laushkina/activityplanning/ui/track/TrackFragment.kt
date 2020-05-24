package com.laushkina.activityplanning.ui.track

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.laushkina.activityplanning.R
import com.laushkina.activityplanning.model.track.Track
import com.laushkina.activityplanning.model.track.TrackService
import com.laushkina.activityplanning.repository.db.track.TrackDBRepository

class TrackFragment : Fragment(), TracksView, TracksAdapter.TrackChangeListener {
    private lateinit var presenter: TracksPresenter
    private lateinit var plansRecycler: RecyclerView
    private lateinit var plansAdapter: TracksAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_track, container, false)

        presenter = TracksPresenter(this, TrackService(TrackDBRepository(context?.applicationContext!!)))
        presenter.onCreate()
        plansRecycler = root.findViewById(R.id.tracks)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.onDestroy()
    }

    override fun showTracks(tracks: List<Track>) {
        plansAdapter = TracksAdapter(tracks, this)
        plansRecycler.adapter = plansAdapter
        plansRecycler.layoutManager = GridLayoutManager(context, 1)
    }

    override fun showError(message: String?) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
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
