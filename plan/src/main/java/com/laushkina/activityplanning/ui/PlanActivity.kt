package com.laushkina.activityplanning.ui

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.laushkina.activityplanning.component.plan.R
import com.laushkina.activityplanning.di.ContextModule
import com.laushkina.activityplanning.di.DaggerPlanPresenterComponent
import com.laushkina.activityplanning.di.PlanViewModule
import com.laushkina.activityplanning.model.plan.Plan
import kotlinx.android.synthetic.main.plan_activity.*

class PlanActivity : BaseActivity(), PlanView, PlansAdapter.PlansChangeListener, NewPlanDialog.NoticeDialogListener {
    private lateinit var presenter: PlanPresenter
    private lateinit var plansAdapter: PlansAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.plan_activity)
        initToolbar("Planning", true)

        presenter = DaggerPlanPresenterComponent.builder()
            .contextModule(ContextModule(applicationContext))
            .planViewModule(PlanViewModule(this))
            .build()
            .getPlanPresenter()

        presenter.onCreate()
        add_button.setOnClickListener { presenter.onAddRequested() }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }

    override fun onActivityRemoveRequested(ind: Int) {
        presenter.onRemoveRequested(ind)
    }

    override fun initPlans(plans: List<Plan>, hoursPerDay: Int) {
        plansAdapter = PlansAdapter(plans, this, hoursPerDay)
        plans_recycler.adapter = plansAdapter
        plans_recycler.layoutManager = GridLayoutManager(this, 1)
    }

    override fun updatePlans(plans: List<Plan>, hoursPerDay: Int) {
        plansAdapter.updatePlans(plans, hoursPerDay)
    }

    override fun showError(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun showHourPerDaySpinner(variants: Array<Int>, selectedItem: Int) {
        val adapter = ArrayAdapter(
            this,
            R.layout.hours_per_day_element,
            variants
        )
        hours_per_day.adapter = adapter

        val itemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
                val variant = adapter.getItem(pos)
                presenter.onHoursPerDayChanged(variant)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }
        hours_per_day.onItemSelectedListener = itemSelectedListener
        hours_per_day.setSelection(selectedItem)
    }

    override fun showAddPlanDialog(remainingPercent: Int) {
        val dialog = NewPlanDialog()

        val extras = Bundle()
        extras.putInt(NewPlanDialog.REMAINING_PERCENT_EXTRA, remainingPercent)
        dialog.arguments = extras

        dialog.show(supportFragmentManager, NewPlanDialog::javaClass.name)
    }

    override fun showInitWithSampleValuesButton() {
        sample_values.visibility = View.VISIBLE
        sample_values.setOnClickListener{ presenter.onFillWithSampleRequested() }
    }

    override fun hideInitWithSampleValuesButton() {
        sample_values.visibility = View.GONE
    }

    override fun onPlanConfirmed(name: CharSequence?, pecent: CharSequence?) {
        if (name != null && pecent != null) {
            presenter.onPlanConfirmed(name.toString(), Integer.parseInt(pecent.toString()))
        }
    }
}