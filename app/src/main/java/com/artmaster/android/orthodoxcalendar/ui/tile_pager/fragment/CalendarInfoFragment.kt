package com.artmaster.android.orthodoxcalendar.ui.tile_pager.fragment

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.artmaster.android.orthodoxcalendar.R

class CalendarInfoFragment : DialogFragment(){
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.tile_monh_help, null)
        view.setOnClickListener { dismiss() }
        val adb = AlertDialog.Builder(requireContext()).setView(view)
        return adb.create()
    }
}