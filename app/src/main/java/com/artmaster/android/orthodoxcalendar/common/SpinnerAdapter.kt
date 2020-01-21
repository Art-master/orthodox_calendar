package com.artmaster.android.orthodoxcalendar.common

import android.content.Context
import android.graphics.Typeface
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class SpinnerAdapter(context: Context, resource: Int, objects: Array<String>) : ArrayAdapter<String>(context, resource, objects) {
    internal var font = Typeface.createFromAsset(context.assets, "fonts/cyrillic_old.ttf")

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent) as TextView
        view.typeface = font
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getDropDownView(position, convertView, parent) as TextView
        view.typeface = font
        return view
    }
}
