package com.laushkina.activityplanning.ui

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.laushkina.activityplanning.component.plan.R
import java.lang.NumberFormatException

class NewPlanDialog: DialogFragment() {
    companion object {
        const val REMAINING_PERCENT_EXTRA = "remaining_percent"
    }
    private var listener: NoticeDialogListener? = null
    private var remainingPercent = 100

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        val inflater = requireActivity().layoutInflater

        val view: View = inflater.inflate(R.layout.new_plan_fragment, null)
        val nameTextView : TextView= view.findViewById(R.id.activity_name)
        val percentTextView : TextView= view.findViewById(R.id.activity_percent)

        val remainingPercentParam = arguments?.getInt(REMAINING_PERCENT_EXTRA)
        if (remainingPercentParam != null) {
            remainingPercent = remainingPercentParam
        }

        builder.setView(view)
            .setPositiveButton("Add"
            ) { _, _ ->
                val name = nameTextView.text
                val percent = percentTextView.text
                if (percent != null && validate(percent)) {
                    listener?.onPlanConfirmed(name, percent)
                }
            }
            .setNegativeButton(android.R.string.cancel
            ) { _, _ ->
                dialog?.cancel()
            }
        return builder.create()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = try {
            context as NoticeDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement NoticeDialogListener")
        }
    }

    private fun validate(percent: CharSequence): Boolean {
        val percentInt: Int
        try {
            percentInt = Integer.parseInt(percent.toString())
        } catch (e: NumberFormatException) {
            showError("Error: not a number")
            return false
        }
        val isCorrect = percentInt <= remainingPercent
        if (!isCorrect) {
            showError("Error: percent must be less than $remainingPercent")
        }
        return isCorrect
    }

    private fun showError(error: String) {
        Toast.makeText(context, error, Toast.LENGTH_LONG).show()
    }

    interface NoticeDialogListener {
        fun onPlanConfirmed(name: CharSequence?, pecent: CharSequence?)
    }
}