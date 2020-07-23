package com.laushkina.activityplanning.ui

import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.laushkina.activityplanning.component.track.R
import com.laushkina.activityplanning.model.track.Track

class TrackAdapter(
    tracks: List<Track>,
    showControlButtons: Boolean,
    private val listener: TrackChangeListener
) : RecyclerView.Adapter<TrackAdapter.ViewHolder>(), TrackItemView {

    private val presenter = TrackItemPresenter(this, tracks, showControlButtons)

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

    override fun showStartButton(holder: ViewHolder, position: Int, track: Track) {
        holder.statusChangeButton.setImageResource(R.drawable.ic_start)
        holder.statusChangeButton.setOnClickListener { listener.onTrackStart(track) }
    }

    override fun showStopButton(holder: ViewHolder, position: Int, track: Track) {
        holder.statusChangeButton.setImageResource(R.drawable.ic_stop)
        holder.statusChangeButton.setOnClickListener { listener.onTrackStop(track) }
    }

    override fun hideControlButtons(holder: ViewHolder) {
        holder.statusChangeButton.visibility = View.GONE
    }

    override fun setActivityName(holder: ViewHolder, name: String) {
        holder.activityName.text = name
    }

    override fun showProgress(holder: ViewHolder, progress: Int, text: String, color: Int) {
        holder.progress.progressDrawable.setColorFilter(color, PorterDuff.Mode.SRC_IN)
        holder.progress.progress = progress
        holder.progressText.text = text
    }

    fun updateTime() {
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val activityName: TextView = itemView.findViewById(R.id.activity_name)
        internal val statusChangeButton: ImageButton =
            itemView.findViewById(R.id.track_status_change)
        internal val progress: ProgressBar = itemView.findViewById(R.id.progress)
        internal val progressText: TextView = itemView.findViewById(R.id.progress_text)
    }

    interface TrackChangeListener {
        fun onTrackStart(track: Track)
        fun onTrackStop(track: Track)
    }
}