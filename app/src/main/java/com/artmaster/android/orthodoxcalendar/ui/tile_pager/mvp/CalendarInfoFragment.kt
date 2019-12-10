package com.artmaster.android.orthodoxcalendar.ui.tile_pager.mvp

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.view.View
import com.artmaster.android.orthodoxcalendar.R

class CalendarInfoFragment : DialogFragment(), View.OnClickListener {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = activity!!.layoutInflater
        val view = inflater.inflate(R.layout.tile_monh_help, null)

        val adb = AlertDialog.Builder(context!!).setView(view)
        return adb.create()
    }

    override fun onClick(v: View?) {
        onDestroy()
    }
}