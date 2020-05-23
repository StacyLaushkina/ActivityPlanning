package com.laushkina.activityplanning.ui.track

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.laushkina.activityplanning.R
import com.laushkina.activityplanning.model.track.Track

class TracksAdapter(private val tracks: List<Track>, private val listener: TrackChangeListener)
    : RecyclerView.Adapter<TracksAdapter.ViewHolder>()  {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.track_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val track = tracks[position]

        holder.activityName.text = track.plan.activityName
        if (track.startTime != null) {
            holder.endButton.isEnabled = true
            holder.endButton.setOnClickListener { listener.onTrackFinish(position, track) }

            holder.startButton.isEnabled = false
        } else {
            holder.startButton.isEnabled = true
            holder.startButton.setOnClickListener { listener.onTrackStart(position, track) }

            holder.endButton.isEnabled = false
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val activityName: TextView = itemView.findViewById(R.id.activity_name)
        internal val startButton: Button = itemView.findViewById(R.id.activity_start)
        internal val endButton: Button = itemView.findViewById(R.id.activity_end)
    }

    interface TrackChangeListener {
        fun onTrackStart(ind: Int, track: Track)
        fun onTrackFinish(ind: Int, track: Track)
    }
}