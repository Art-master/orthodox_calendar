package com.artmaster.android.orthodoxcalendar.ui.tile_pager.mvp

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import com.artmaster.android.orthodoxcalendar.R
import org.jetbrains.anko.sdk27.coroutines.onClick

class CalendarInfoFragment : DialogFragment(){
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = activity!!.layoutInflater
        val view = inflater.inflate(R.layout.tile_monh_help, null)
        view.onClick { dismiss() }
        val adb = AlertDialog.Builder(context!!).setView(view)
        return adb.create()
    }
}