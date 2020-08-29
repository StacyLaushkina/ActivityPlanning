package com.laushkina.activityplanning.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.laushkina.activityplanning.component.plan.R
import com.laushkina.activityplanning.di.ContextModule
import com.laushkina.activityplanning.di.DaggerPlanPresenterComponent
import com.laushkina.activityplanning.di.PlanViewModule
import com.laushkina.activityplanning.model.plan.Plan
import kotlinx.android.synthetic.main.fragment_plan.*

class PlanFragment :
    Fragment(), PlanView, PlansAdapter.PlansChangeListener, NewPlanDialog.NoticeDialogListener {
    private lateinit var presenter: PlanPresenter

    private lateinit var plansAdapter: PlansAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        presenter = DaggerPlanPresenterComponent.builder()
            .contextModule(ContextModule(requireContext().applicationContext))
            .planViewModule(PlanViewModule(this))
            .build()
            .getPlanPresenter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_plan, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.onCreate()
        add_button.setOnClickListener { presenter.onAddRequested() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.onDestroy()
    }

    override fun onActivityRemoveRequested(ind: Int) {
        presenter.onRemoveRequested(ind)
    }

    override fun initPlans(plans: List<Plan>, hoursPerDay: Int) {
        plansAdapter = PlansAdapter(plans, this, hoursPerDay)
        plans_recycler.adapter = plansAdapter
        plans_recycler.layoutManager = GridLayoutManager(context, 1)
    }

    override fun updatePlans(plans: List<Plan>, hoursPerDay: Int) {
        plansAdapter.updatePlans(plans, hoursPerDay)
    }

    override fun showError(message: String?) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    override fun showHourPerDaySpinner(variants: Array<Int>, selectedItem: Int) {
        val adapter = ArrayAdapter(
            requireContext(),
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
        dialog.setTargetFragment(this, 12345)

        val extras = Bundle()
        extras.putInt(NewPlanDialog.REMAINING_PERCENT_EXTRA, remainingPercent)
        dialog.arguments = extras

        dialog.show(requireFragmentManager(), NewPlanDialog::javaClass.name)
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
