package com.gimble.seekpot.feature.enquiry.presentation

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.gimble.seekpot.R

class ProgShowOne : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the Builder class for convenient dialog construction
        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(R.layout.progress_shower_d1)
        return builder.create()
    }

}