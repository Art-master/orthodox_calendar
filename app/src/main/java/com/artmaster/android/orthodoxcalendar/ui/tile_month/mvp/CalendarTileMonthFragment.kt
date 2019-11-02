package com.artmaster.android.orthodoxcalendar.ui.tile_month.mvp

import android.graphics.Color
import android.os.Bundle
import android.text.Layout
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableRow
import android.widget.TextView
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.PresenterType
import com.artmaster.android.orthodoxcalendar.R
import com.artmaster.android.orthodoxcalendar.data.font.CustomFont
import com.artmaster.android.orthodoxcalendar.data.font.TextViewWithCustomFont
import com.artmaster.android.orthodoxcalendar.domain.HolidayEntity
import com.artmaster.android.orthodoxcalendar.domain.Time2
import com.artmaster.android.orthodoxcalendar.ui.tile_month.impl.ContractTileMonthView
import kotlinx.android.synthetic.main.fragment_month_tile_calendar.view.*
import org.jetbrains.anko.textColor

internal class CalendarTileMonthFragment: MvpAppCompatFragment(), ContractTileMonthView {

    @InjectPresenter(tag = "TileMonthPresenter", type = PresenterType.GLOBAL)
    lateinit var presenter: TileMonthPresenter

    lateinit var tileView: View
    lateinit var tileDayView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(!presenter.isInRestoreState(this)){
            presenter.attachView(this)
            presenter.viewIsReady()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, groupContainer: ViewGroup?, savedInstanceState: Bundle?): View? {
        tileView = inflater.inflate(R.layout.fragment_month_tile_calendar, groupContainer, false)
        tileDayView = inflater.inflate(R.layout.tile_day_layout, null, false)
        return tileView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
    }

    private fun getDayLayout() = layoutInflater.inflate(R.layout.tile_day_layout, null)

    override fun clearView(){
        view!!.tableMonthTile.removeAllViews()
    }

    override fun createDay(dayOfWeek: Int, level: Int, holiday: List<HolidayEntity>, i: Int){
        val row = view!!.tableMonthTile.getChildAt(dayOfWeek -1) as TableRow
        val v = getDayLayout()
        styleDayView(v, holiday, dayOfWeek, i)
        if(row.childCount == level -1) row.addView(TextView(context), level-1)
        row.addView(v, level)
    }

    private fun styleDayView(view: View, holidays: List<HolidayEntity>, dayOfWeek: Int, day: Int){
        //if(holidays.isEmpty()) return
        val text = view.findViewById<TextViewWithCustomFont>(R.id.numDay)

        holidays.forEach {
            if(it.type.contains(HolidayEntity.Type.GREAT.name, true))
                text.textColor = Color.RED}

        text.text = day.toString()
        if(dayOfWeek == Time2.Day.SUNDAY.num) text.textColor = Color.RED
    }

    override fun createDaysOfWeekRows(dayOfWeek: IntRange) {
        for(i in dayOfWeek){
            val row = createDayOfWeekRow()
            view!!.tableMonthTile.addView(row)
            row.addView(createDayOtWeek(i))
        }
    }

    override fun createDayOfWeekName(dayOfWeek: Int){
        val row = view!!.tableMonthTile.getChildAt(dayOfWeek) as TableRow

    }

    private fun createDayOtWeek(month : Int): TextView {
        val text = TextViewWithCustomFont(context!!)
        val index = when(month){
            1 -> R.string.monday
            2 -> R.string.tuesday
            3 -> R.string.wednesday
            4 -> R.string.thursday
            5 -> R.string.friday
            6 -> R.string.saturday
            7 -> R.string.sunday
            else -> R.string.monday
        }
        text.text = resources.getText(index)
        text.textColor = Color.RED
        text.typeface = CustomFont.getFont(context!!, getString(R.string.font_basic))
        text.textAlignment = Layout.Alignment.ALIGN_CENTER.ordinal
        return text
    }

    private fun createDayOfWeekRow(): TableRow {
        val row = TableRow(context)
        row.layoutParams = ViewGroup.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT)
        row.gravity = Gravity.CENTER_VERTICAL
        return row
    }
}
