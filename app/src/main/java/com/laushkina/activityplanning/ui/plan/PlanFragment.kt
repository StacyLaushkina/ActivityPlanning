package com.laushkina.activityplanning.ui.plan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import android.widget.Button
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.laushkina.activityplanning.R
import com.laushkina.activityplanning.di.ContextModule
import com.laushkina.activityplanning.di.plan.DaggerPlanPresenterComponent
import com.laushkina.activityplanning.di.plan.PlanViewModule
import com.laushkina.activityplanning.model.plan.Plan

class PlanFragment :
    Fragment(), PlanView, PlansAdapter.PlansChangeListener, NewPlanDialog.NoticeDialogListener {
    private lateinit var presenter: PlanPresenter
    private lateinit var plansRecycler: RecyclerView
    private lateinit var plansAdapter: PlansAdapter
    private lateinit var hoursPerDaySpinner: Spinner
    private lateinit var fillWithSampleButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_plan, container, false)

        plansRecycler = root.findViewById(R.id.plans)
        hoursPerDaySpinner = root.findViewById(R.id.hours_per_day)

        val addButton: View = root.findViewById(R.id.add_button)
        addButton.setOnClickListener { presenter.onAddRequested() }

        fillWithSampleButton = root.findViewById(R.id.sample_values)

        presenter = DaggerPlanPresenterComponent.builder()
            .contextModule(ContextModule(requireContext().applicationContext))
            .planViewModule(PlanViewModule(this))
            .build()
            .getPlanPresenter()

        presenter.onCreate()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.onDestroy()
    }

    override fun onActivityRemoveRequested(ind: Int) {
        presenter.onActivityRemoveRequested(ind)
    }

    override fun showPlans(plans: List<Plan>, hoursPerDay: Int) {
        plansAdapter = PlansAdapter(plans, this, hoursPerDay)
        plansRecycler.adapter = plansAdapter
        plansRecycler.layoutManager = GridLayoutManager(context, 1)
    }

    override fun updatePlans(plans: MutableList<Plan>, hoursPerDay: Int) {
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
        hoursPerDaySpinner.adapter = adapter

        val itemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
                val variant = adapter.getItem(pos)
                presenter.onHoursPerDayChanged(variant)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }
        hoursPerDaySpinner.onItemSelectedListener = itemSelectedListener
        hoursPerDaySpinner.setSelection(selectedItem)
    }

    override fun showAddActivityDialog(remainingPercent: Int) {
        val dialog: DialogFragment = NewPlanDialog()
        dialog.setTargetFragment(this, 12345)

        val extras = Bundle()
        extras.putInt(NewPlanDialog.REMAINING_PERCENT_EXTRA, remainingPercent)
        dialog.arguments = extras

        dialog.show(requireFragmentManager(), NewPlanDialog::javaClass.name)
    }

    override fun showInitWithSampleValuesButton() {
        fillWithSampleButton.visibility = View.VISIBLE
        fillWithSampleButton.setOnClickListener{ presenter.onFillWithSampleRequested() }
    }

    override fun hideInitWithSampleValuesButton() {
        fillWithSampleButton.visibility = View.GONE
    }

    override fun onPlanConfirmed(name: CharSequence?, pecent: CharSequence?) {
        if (name != null && pecent != null) {
            presenter.onPlanConfirmed(name.toString(), Integer.parseInt(pecent.toString()))
        }
    }
}
