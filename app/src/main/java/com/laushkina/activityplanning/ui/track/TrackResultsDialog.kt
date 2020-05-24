package com.laushkina.activityplanning.ui.track

import android.R
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class TrackResultsDialog: DialogFragment() {
    companion object {
        const val MESSAGE_EXTRA = "message_extra"
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val message = arguments?.getString(MESSAGE_EXTRA)

        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        builder.setMessage(message)
            .setPositiveButton(R.string.ok
            ) { dialog, _ ->
             //   dialog.dismiss()
            }
        return builder.create()
    }
}