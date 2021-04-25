package com.artmaster.android.orthodoxcalendar.ui

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.artmaster.android.orthodoxcalendar.R
import com.artmaster.android.orthodoxcalendar.common.msg.Error
import com.artmaster.android.orthodoxcalendar.common.msg.Message
import com.artmaster.android.orthodoxcalendar.common.msg.Warning

/**
 * Building system message for user
 */
class MessageBuilderFragment : DialogFragment(), DialogInterface.OnClickListener {

    var onClickListener: DialogInterface.OnClickListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val typeMsg = requireArguments().getString(Message.TYPE)

        val builder = buildMessage(typeMsg!!)

        return builder.create()
    }

    override fun onClick(dialog: DialogInterface, which: Int) {
        if (onClickListener == null) {
            onCancel(dialog)
        } else {
            onClickListener!!.onClick(dialog, which)
        }
    }

    private fun getTextMessage(typeMsg: String): String {
        return when (typeMsg) {
            Error.INIT_DATABASE.name -> getString(R.string.fatalErrorText)
            Warning.DELETE_HOLIDAY.name -> getString(R.string.holidayConfirmationMsg)
            else -> ""
        }
    }

    private fun getHeaderMessage(typeMsg: String): String {
        return when (typeMsg) {
            Error.INIT_DATABASE.name -> getString(R.string.fatalErrorHeader)
            Warning.DELETE_HOLIDAY.name -> getString(R.string.holidayConfirmationHeader)
            else -> ""
        }
    }

    private fun getPositiveButtonTextId(typeMsg: String): String {
        return when (typeMsg) {
            Warning.DELETE_HOLIDAY.name -> getString(R.string.buttonConfirm)
            else -> ""
        }
    }

    private fun getNegativeButtonTextId(typeMsg: String): String {
        return when (typeMsg) {
            Warning.DELETE_HOLIDAY.name -> getString(R.string.buttonReject)
            else -> ""
        }
    }

    private fun buildMessage(typeMsg: String): AlertDialog.Builder {
        return AlertDialog.Builder(requireActivity()).apply {
            setTitle(getHeaderMessage(typeMsg))
            setMessage(getTextMessage(typeMsg))
            setIcon(R.drawable.ic_baseline_warning_24)
            setCancelable(true)
            setPositiveButton(getPositiveButtonTextId(typeMsg), this@MessageBuilderFragment)
            setNegativeButton(getNegativeButtonTextId(typeMsg), this@MessageBuilderFragment)
            setOnCancelListener { }
        }
    }
}