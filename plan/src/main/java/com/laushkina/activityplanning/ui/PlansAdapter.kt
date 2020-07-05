package com.laushkina.activityplanning.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.laushkina.activityplanning.component.plan.R
import com.laushkina.activityplanning.model.plan.Plan
import java.util.*

class PlansAdapter(private var plans: List<Plan>,
                   private val listener: PlansChangeListener,
                   private var hoursPerDay: Int)
    : RecyclerView.Adapter<PlansAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.plan_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return plans.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val plan = plans[position]

        holder.activityName.text = plan.activityName
        holder.activityPercent.text = plan.percent.toString()
        holder.activityHours.text = getHours(hoursPerDay, plan.percent)

        holder.activityRemove.setOnClickListener{ listener.onActivityRemoveRequested(position) }
    }

    private fun getHours(hoursPerDay: Int, percent: Int?): String {
        if (percent == null) {
            return ""
        }

        val hours = hoursPerDay * percent * 0.01
        return String.format(Locale.US, "%.2f", hours)
    }

    fun updatePlans(plans: List<Plan>, hoursPerDay: Int) {
        this.plans = plans
        this.hoursPerDay = hoursPerDay
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val activityName: TextView = itemView.findViewById(R.id.activity_name)
        internal val activityPercent: TextView = itemView.findViewById(R.id.activity_percent)
        internal val activityHours: TextView = itemView.findViewById(R.id.activity_hours)
        internal val activityRemove: ImageButton = itemView.findViewById(R.id.activity_remove)
    }

    interface PlansChangeListener {
        fun onActivityRemoveRequested(ind: Int)
    }
}