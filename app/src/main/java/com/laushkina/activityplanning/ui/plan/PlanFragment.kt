package com.laushkina.activityplanning.ui.plan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.laushkina.activityplanning.R
import com.laushkina.activityplanning.model.plan.Plan
import com.laushkina.activityplanning.model.plan.PlanService
import com.laushkina.activityplanning.repository.db.plan.PlanDBRepository

class PlanFragment : Fragment(), PlanView, PlansAdapter.PlansChangeListener {
    private lateinit var presenter: PlanPresenter
    private lateinit var plansRecycler: RecyclerView
    private lateinit var plansAdapter: PlansAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_plan, container, false)

        presenter = PlanPresenter(this,
            PlanService(
                PlanDBRepository(
                    context?.applicationContext!!
                )
            )
        )
        presenter.onCreate()

        val addButton: View = root.findViewById(R.id.add_button)
        plansRecycler = root.findViewById(R.id.plans)

        addButton.setOnClickListener { presenter.onAddRequested() }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.onDestroy()
    }

    override fun onActivityNameChange(ind: Int, newName: String) {
        presenter.onActivityNameChange(ind, newName)
    }

    override fun onPercentChange(ind: Int, newPercent: Int) {
        presenter.onPercentChange(ind, newPercent)
    }

    override fun showPlans(plans: List<Plan>) {
        plansAdapter = PlansAdapter(plans, this)
        plansRecycler.adapter = plansAdapter
        plansRecycler.layoutManager = GridLayoutManager(context, 1)
    }

    override fun updatePlans(plans: MutableList<Plan>) {
        plansAdapter.updatePlans(plans)
    }

    override fun showError(message: String?) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
}
