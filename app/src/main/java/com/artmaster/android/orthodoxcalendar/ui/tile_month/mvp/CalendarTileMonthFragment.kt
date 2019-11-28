package com.artmaster.android.orthodoxcalendar.ui.tile_month.mvp

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
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
import com.artmaster.android.orthodoxcalendar.domain.Time2
import com.artmaster.android.orthodoxcalendar.ui.tile_month.impl.ContractTileMonthView
import kotlinx.android.synthetic.main.fragment_month_tile_calendar.*
import kotlinx.android.synthetic.main.fragment_month_tile_calendar.view.*
import kotlinx.android.synthetic.main.tile_day_layout.view.*
import org.jetbrains.anko.textColor

internal class CalendarTileMonthFragment: MvpAppCompatFragment(), ContractTileMonthView {

    @InjectPresenter(tag = "TileMonthPresenter", type = PresenterType.LOCAL)
    lateinit var presenter: TileMonthPresenter

    lateinit var tileView: View
    lateinit var tileDayView: View
    lateinit var layoutManager: LinearLayoutManager

    private var tileBackground: Drawable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

    private fun getDayLayout() = layoutInflater.inflate(R.layout.tile_day_layout, null)

    private fun getYear() = arguments!!.getInt(Constants.Keys.YEAR.value, Time2().year)
    private fun getMonth() = arguments!!.getInt(Constants.Keys.MONTH.value, Time2().month)
    private fun getDay() = arguments!!.getInt(Constants.Keys.DAY.value, Time2().dayOfMonth)

    override fun initCalendar(){
    }

    override fun clearView(){
        view!!.tableMonthTile.removeAllViews()
    }

    override fun createDay(dayOfWeek: Int, level: Int, holidays: List<HolidayEntity>, i: Int){
        if(presenter.isInRestoreState(this)) return
        val row = view!!.tableMonthTile.getChildAt(dayOfWeek -1) as TableRow

        val v = getDayLayout()
        v.setOnFocusChangeListener { view, hasFocus -> changedFocus(view, hasFocus, holidays) }

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

        if(holidays.isEmpty()) return
        val firstHoliday = holidays[0]
        styleHoliday(firstHoliday, view)

        if(dayOfWeek == Time2.Day.SUNDAY.num) text.textColor = Color.RED
    }

    private fun styleHoliday(holiday: HolidayEntity, v: View){
        val text = v.findViewById<TextViewWithCustomFont>(R.id.numDay)
        if(holiday.type.contains(HolidayEntity.Type.GREAT.name, true)){
            text.textColor = Color.RED
        }
        if(holiday.type.contains(HolidayEntity.Type.TWELVE.value)){
            v.container.background = ContextCompat.getDrawable(v.context!!, R.drawable.tile_twelve_holiday)
        }
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
        val row = view!!.tableMonthTile.getChildAt(dayOfWeek) as TableRow

    }

    private fun getDayNames() = resources.getStringArray(R.array.daysNamesAbb)

    private fun createDayOtWeek(month : Int): TextView {
        val text = TextViewWithCustomFont(context!!)
        val dayNames = getDayNames()
        text.text = dayNames[month -1]
        text.textColor = Color.RED
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
