package com.laushkina.activityplanning.ui.track

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.laushkina.activityplanning.R
import com.laushkina.activityplanning.model.track.Track

class TrackAdapter(tracks: List<Track>, private val listener: TrackChangeListener)
    : RecyclerView.Adapter<TrackAdapter.ViewHolder>(), TrackItemView  {

    private val presenter = TrackItemPresenter(this, tracks)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.track_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return presenter.getItemCount()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        presenter.onBindViewHolder(holder, position)
    }

    override fun enableStartButton(holder: ViewHolder, position: Int, track: Track) {
        holder.startButton.visibility = View.VISIBLE
        holder.startButton.setOnClickListener { listener.onTrackStart(position, track) }
    }

    override fun enableEndButton(holder: ViewHolder, position: Int, track: Track) {
        holder.endButton.visibility = View.VISIBLE
        holder.endButton.setOnClickListener { listener.onTrackFinish(position, track) }
    }

    override fun enableContinueButton(holder: ViewHolder, position: Int, track: Track) {
        holder.continueButton.visibility = View.VISIBLE
        holder.continueButton.setOnClickListener { listener.onTrackContinue(position, track) }
    }

    override fun setActivityName(holder: ViewHolder, name: String) {
        holder.activityName.text = name
    }

    override fun showProgress(holder: ViewHolder, time: String?) {
        holder.progress.visibility = View.VISIBLE
        holder.progress.text = time
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val activityName: TextView = itemView.findViewById(R.id.activity_name)
        internal val startButton: Button = itemView.findViewById(R.id.activity_start)
        internal val continueButton: Button = itemView.findViewById(R.id.activity_continue)
        internal val endButton: Button = itemView.findViewById(R.id.activity_end)
        internal val progress: TextView = itemView.findViewById(R.id.progress)
    }

    interface TrackChangeListener {
        fun onTrackStart(ind: Int, track: Track)
        fun onTrackContinue(ind: Int, track: Track)
        fun onTrackFinish(ind: Int, track: Track)
    }
}