package com.laushkina.activityplanning.ui.plan

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.laushkina.activityplanning.R
import com.laushkina.activityplanning.model.plan.Plan

class PlansAdapter(private var plans: List<Plan>, private val listener: PlansChangeListener)
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

        holder.activityName.setText(plan.activityName)
        holder.activityPercent.setText(plan.percent?.toString())

        val activityNameWatcher: TextWatcher = object:TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if (p0 != null) {
                    listener.onActivityNameChange(position, p0.toString())
                }
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }
        }
        holder.activityName.addTextChangedListener(activityNameWatcher)

        val activityPercentWatcher: TextWatcher = object:TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if (p0 != null && p0.isNotEmpty()) {
                    listener.onPercentChange(position, Integer.parseInt(p0.toString()))
                }
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }
        }
        holder.activityPercent.addTextChangedListener(activityPercentWatcher)

        if (position == itemCount - 1) {
            holder.activityName.requestFocus()
        }
    }

    fun updatePlans(plans: List<Plan>) {
        this.plans = plans
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val activityName: EditText = itemView.findViewById(R.id.activity_name)
        internal val activityPercent: EditText = itemView.findViewById(R.id.activity_percent)
    }

    interface PlansChangeListener {
        fun onActivityNameChange(ind: Int, newName: String)
        fun onPercentChange(ind: Int, newPercent: Int)
    }
}