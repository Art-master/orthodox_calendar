package com.artmaster.android.orthodoxcalendar.ui

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.artmaster.android.orthodoxcalendar.R
import com.artmaster.android.orthodoxcalendar.common.Message
import com.artmaster.android.orthodoxcalendar.common.Message.Companion.EMPTY
import com.artmaster.android.orthodoxcalendar.common.OrtUtils

/**
 * Building system message for user
 */
class MessageBuilderFragment : DialogFragment(), DialogInterface.OnClickListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val arg = arguments
        val typeMsg = arg!!.getString(Message.TYPE)
        val msgText = getTextMessage(typeMsg)

        val builder = buildMessage(msgText)
        return builder.create()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        OrtUtils.exitProgram(this.requireContext())
    }

    override fun onClick(dialog: DialogInterface, which: Int) {
        onCancel(dialog)
    }

    private fun getTextMessage(typeMsg: String?): Pair<String, String> {
        when (typeMsg) {
            Message.ERROR.INIT_DATABASE.toString() -> {
                return getString(R.string.fatalErrorHeader) to getString(R.string.fatalErrorText)
            }
        }
        return Pair(EMPTY, EMPTY)
    }

    private fun buildMessage(msgText: Pair<String, String>): AlertDialog.Builder {
        val builder = AlertDialog.Builder(this.requireActivity())
        builder.setTitle(msgText.first)
                .setMessage(msgText.second)
                .setIcon(R.mipmap.alert_error)
                .setCancelable(false)
                .setNegativeButton(R.string.buttonOk, this)
        return builder
    }
}