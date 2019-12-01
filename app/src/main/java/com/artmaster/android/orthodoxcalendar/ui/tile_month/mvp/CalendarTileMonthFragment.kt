package com.artmaster.android.orthodoxcalendar.ui.tile_month.mvp

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
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
import com.artmaster.android.orthodoxcalendar.common.Constants
import com.artmaster.android.orthodoxcalendar.data.font.CustomFont
import com.artmaster.android.orthodoxcalendar.data.font.TextViewWithCustomFont
import com.artmaster.android.orthodoxcalendar.domain.HolidayEntity
import com.artmaster.android.orthodoxcalendar.domain.Time
import com.artmaster.android.orthodoxcalendar.ui.tile_month.impl.ContractTileMonthView
import kotlinx.android.synthetic.main.fragment_month_tile_calendar.*
import kotlinx.android.synthetic.main.fragment_month_tile_calendar.view.*
import kotlinx.android.synthetic.main.tile_day_layout.view.*
import org.jetbrains.anko.textColor

internal class CalendarTileMonthFragment: MvpAppCompatFragment(), ContractTileMonthView {

    @InjectPresenter(tag = "TileMonthPresenter", type = PresenterType.LOCAL)
    lateinit var presenter: TileMonthPresenter

    private lateinit var tileView: View
    private lateinit var tileDayView: View
    private lateinit var layoutManager: LinearLayoutManager

    private var tileBackground: Drawable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        retainInstance = true

        layoutManager = LinearLayoutManager(context)

        if(!presenter.isInRestoreState(this)){
            presenter.attachView(this)
            presenter.viewIsReady(getYear(), getMonth())
        }
    }

    override fun onCreateView(inflater: LayoutInflater, groupContainer: ViewGroup?, savedInstanceState: Bundle?): View? {
        tileView = inflater.inflate(R.layout.fragment_month_tile_calendar, groupContainer, false)
        tileDayView = inflater.inflate(R.layout.tile_day_layout, null, false)
        return tileView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.viewIsCreated()
    }

    override fun onResume() {
        super.onResume()
        //setFocus()
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
    }

    private fun getDayLayout() = layoutInflater.inflate(R.layout.tile_day_layout, null)
    private fun getYear() = getArgs().getInt(Constants.Keys.YEAR.value, Time().year)
    private fun getMonth() = arguments!!.getInt(Constants.Keys.MONTH.value, Time().month -1)
    private fun getParentMonth() = getArgs().getInt(Constants.Keys.MONTH.value, Time().month -1)
    private fun getDay() = getArgs().getInt(Constants.Keys.DAY.value, Time().dayOfMonth)
    private fun setDayArgs(value: Int) = getArgs().putInt(Constants.Keys.DAY.value, value)
    private fun getArgs() = parentFragment!!.arguments!!

    override fun setFocus(monthNum: Int){
        val r = monthNum
        val g = getParentMonth()
        if(monthNum != getParentMonth() && isVisible) return
        val id = getDay()
        if(id == 0 || tableMonthTile == null) return
        val daysOfWeek = 0 until tableMonthTile.childCount
        for(i in daysOfWeek){
            val row = tableMonthTile.getChildAt(i) as TableRow
            val view = row.findViewById<ConstraintLayout>(id) ?: continue
            view.requestFocus()
            return
        }
    }

    override fun clearView(){
        view!!.tableMonthTile.removeAllViews()
    }

    override fun createDay(dayOfWeek: Int, level: Int, holidays: List<HolidayEntity>, i: Int){
        if(presenter.isInRestoreState(this)) return
        val row = view!!.tableMonthTile.getChildAt(dayOfWeek -1) as TableRow

        val v = getDayLayout()
        v.setOnFocusChangeListener { view, hasFocus -> changedFocus(view, hasFocus, holidays) }
        v.id = i

        styleDayView(v, holidays, dayOfWeek, i)
        if(row.childCount == level -1) row.addView(TextView(context), level-1)
        row.addView(v, level)
    }

    private fun changedFocus(view: View, hasFocus: Boolean, holidays: List<HolidayEntity>){
        if(hasFocus){
            initRecyclerView(holidays)
            tileBackground = view.container.background
            view.container.background =
                    ContextCompat.getDrawable(view.context!!, R.drawable.tile_selected_item)
            setDayArgs(view.id)
        } else {
            view.container.background = tileBackground
        }
    }

    private fun initRecyclerView(holidays: List<HolidayEntity>){
        if(recyclerViewDayHolidays.layoutManager == null) recyclerViewDayHolidays.layoutManager = layoutManager
        recyclerViewDayHolidays.adapter = HolidayDayAdapter(holidays, context!!)
    }

    private fun styleDayView(view: View, holidays: List<HolidayEntity>, dayOfWeek: Int, day: Int){
        val text = view.findViewById<TextViewWithCustomFont>(R.id.numDay)
        text.text = day.toString()

        if(dayOfWeek == Time.Day.SUNDAY.num) text.textColor = Color.RED

        if(holidays.isEmpty()) return
        styleHoliday(holidays, view)
    }

    private fun styleHoliday(holiday: List<HolidayEntity>, v: View){
        val text = v.findViewById<TextViewWithCustomFont>(R.id.numDay)

        when {
            isTypeHoliday(HolidayEntity.Type.TWELVE, holiday) -> {
                text.textColor = ContextCompat.getColor(v.context!!, R.color.colorTextHeadHolidays)
            v.container.background = ContextCompat.getDrawable(v.context!!, R.drawable.tile_twelve_holiday)
            }
            isTypeHoliday(HolidayEntity.Type.GREAT, holiday) -> {
                text.textColor = ContextCompat.getColor(v.context!!, R.color.colorTextHeadHolidays)
                v.container.background = ContextCompat.getDrawable(v.context!!, R.drawable.tile_twelve_holiday)
            }

            isTypeHoliday(HolidayEntity.Type.MAIN, holiday) -> {
                text.textColor = ContextCompat.getColor(v.context!!, R.color.colorTextHeadHolidays)
                v.container.background = ContextCompat.getDrawable(v.context!!, R.drawable.tile_easter)
            }
        }
    }

    private fun isTypeHoliday(type: HolidayEntity.Type, holidays: List<HolidayEntity>): Boolean {
        for(holiday in holidays){
            if(holiday.type.contains(type.value, true)) return true
        }
        return false
    }

    override fun createDaysOfWeekRows(dayOfWeek: IntRange) {
        if(presenter.isInRestoreState(this)) return
        for(i in dayOfWeek){
            val row = createDayOfWeekRow()
            view!!.tableMonthTile.addView(row)
            row.addView(createDayOtWeek(i))
        }
    }

    override fun createDayOfWeekName(dayOfWeek: Int){
        view!!.tableMonthTile.getChildAt(dayOfWeek) as TableRow
    }

    private fun getDayNames() = resources.getStringArray(R.array.daysNamesAbb)

    private fun createDayOtWeek(month : Int): TextView {
        val text = TextViewWithCustomFont(context!!)
        val dayNames = getDayNames()
        text.text = dayNames[month -1]
        text.textColor = Color.RED
        text.textSize = resources.getDimension(R.dimen.size_tile_day_of_week)
        text.typeface = CustomFont.getFont(context!!, getString(R.string.font_basic))
        text.textAlignment = Layout.Alignment.ALIGN_CENTER.ordinal
        text.setPadding(0,0,20,0)
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
