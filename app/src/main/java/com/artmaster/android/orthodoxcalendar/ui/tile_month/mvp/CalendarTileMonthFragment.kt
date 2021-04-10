package com.artmaster.android.orthodoxcalendar.ui.tile_month.mvp

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.content.ClipDescription
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Layout
import android.view.*
import android.widget.ImageView
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.artmaster.android.orthodoxcalendar.R
import com.artmaster.android.orthodoxcalendar.common.Constants
import com.artmaster.android.orthodoxcalendar.data.font.CustomFont
import com.artmaster.android.orthodoxcalendar.data.font.TextViewWithCustomFont
import com.artmaster.android.orthodoxcalendar.databinding.FragmentMonthTileCalendarBinding
import com.artmaster.android.orthodoxcalendar.databinding.TileDayLayoutBinding
import com.artmaster.android.orthodoxcalendar.domain.Day
import com.artmaster.android.orthodoxcalendar.domain.Fasting
import com.artmaster.android.orthodoxcalendar.domain.Holiday
import com.artmaster.android.orthodoxcalendar.domain.Time
import com.artmaster.android.orthodoxcalendar.ui.tile_month.impl.ContractTileMonthView
import kotlinx.coroutines.launch
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import java.util.*
import kotlin.collections.ArrayList

internal class CalendarTileMonthFragment : MvpAppCompatFragment(), ContractTileMonthView {

    @InjectPresenter(tag = "TileMonthPresenter")
    lateinit var presenter: TileMonthPresenter

    private lateinit var layoutManager: LinearLayoutManager

    private var tableRows = ArrayList<TableRow>()

    private var _monthBinding: FragmentMonthTileCalendarBinding? = null
    private val monthBinding get() = _monthBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        layoutManager = LinearLayoutManager(context)

        if (!presenter.isInRestoreState(this)) {
            presenter.attachView(this)

            lifecycleScope.launch {
                presenter.viewIsReady(getYear(), getMonth())
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, groupContainer: ViewGroup?, savedInstanceState: Bundle?): View {
        _monthBinding = FragmentMonthTileCalendarBinding.inflate(inflater, groupContainer, false)
        return monthBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAnimation()
        monthBinding.root.setOnDragListener(onDragListener)
        monthBinding.root.setOnClickListener(onClick)
    }

    override fun onResume() {
        super.onResume()
        presenter.viewIsCreated()
    }

    private fun initAnimation() {
        val set = AnimatorInflater.loadAnimator(context, R.animator.loading_animator) as AnimatorSet
        set.setTarget(monthBinding.ringLoading)
        set.start()
    }

    private fun getDayLayout() = TileDayLayoutBinding.inflate(layoutInflater)
    private fun getYear() = getArgs().getInt(Constants.Keys.YEAR.value, Time().year)
    private fun getMonth() = requireArguments().getInt(Constants.Keys.MONTH.value, Time().month - 1)
    private fun getParentMonth() = getArgs().getInt(Constants.Keys.MONTH.value, Time().month - 1)
    private fun getDay() = getArgs().getInt(Constants.Keys.DAY.value, Time().dayOfMonth)
    private fun setDayArgs(value: Int) = getArgs().putInt(Constants.Keys.DAY.value, value)
    private fun getArgs() = requireParentFragment().requireArguments()

    override fun setFocus(monthNum: Int) {
        if (monthNum != getParentMonth() && isVisible) return
        val id = getDay()
        if (id == 0) return
        val daysOfWeek = 0 until monthBinding.tableMonthTile.childCount
        for (i in daysOfWeek) {
            val row = monthBinding.tableMonthTile.getChildAt(i) as TableRow
            val view = row.findViewById<ConstraintLayout>(id) ?: continue
            view.requestFocus()
            return
        }
    }

    override fun clearView() {
        if (_monthBinding == null) return //if state restored and _monthBinding == null
        setVisibility()
        monthBinding.tableMonthTile.removeAllViews()
    }

    private fun setVisibility() {
        monthBinding.recyclerViewDayHolidays.visibility = RecyclerView.VISIBLE
        monthBinding.tableMonthTile.visibility = TableLayout.VISIBLE
        monthBinding.ringLoading.visibility = ImageView.GONE
        monthBinding.crossLoading.visibility = ImageView.GONE
    }

    private fun prepareDayOfMonth(dayOfWeek: Int, level: Int, day: Day) {
        val row = tableRows[dayOfWeek - 1]

        val v = getDayLayout()
        v.root.setOnFocusChangeListener { view, hasFocus -> changedFocus(view, hasFocus, day) }
        v.numDay.id = day.dayOfMonth

        styleDayView(v, day, dayOfWeek)
        if (row.childCount == level - 1) row.addView(TextView(context), level - 1)
        row.addView(v.root, level)
    }

    override fun prepareMonthsDays(days: List<Day>, time: Time) {
        val currentTime = Time(time.calendar)
        val numDays = currentTime.daysInMonth
        for (index in 1..numDays) {
            currentTime.calendar.set(Calendar.DAY_OF_MONTH, index)
            val dayOfWeek = currentTime.dayOfWeek
            val week = currentTime.calendar.get(Calendar.WEEK_OF_MONTH)
            prepareDayOfMonth(dayOfWeek, week, days[index - 1])
        }
    }

    private fun changedFocus(view: View, hasFocus: Boolean, day: Day) {
        val bg = view.background
        if (hasFocus) {
            initRecyclerView(day.holidays)
            val c = ContextCompat.getColor(view.context!!, R.color.colorSelectTile)
            //val filter = BlendModeColorFilter(c, BlendMode.COLOR) TODO filters
            setDayArgs(view.id)
        } //else bg.clearColorFilter()
    }

    private fun initRecyclerView(holidays: List<Holiday>) {
        if (monthBinding.recyclerViewDayHolidays.layoutManager == null) {
            monthBinding.recyclerViewDayHolidays.layoutManager = layoutManager
        }
        monthBinding.recyclerViewDayHolidays.adapter = HolidayDayAdapter(holidays, requireContext())
    }

    private fun styleDayView(view: TileDayLayoutBinding, day: Day, dayOfWeek: Int) {
        val text = view.numDay
        text.text = day.dayOfMonth.toString()

        if (dayOfWeek == Time.Day.SUNDAY.num) text.setTextColor(Color.RED)

        styleFastingHoliday(day, view)
        styleMemoryTypeHoliday(day, view)
        styleTypeHoliday(day, view)
    }

    private fun styleTypeHoliday(day: Day, v: TileDayLayoutBinding) {
        val holidays = day.holidays
        val text = v.numDay
        when {
            isTypeHoliday(Holiday.Type.TWELVE_MOVABLE, holidays) ||
                    isTypeHoliday(Holiday.Type.TWELVE_NOT_MOVABLE, holidays) -> {
                setStyle(v, text, R.drawable.tile_twelve_holiday)
            }
            isTypeHoliday(Holiday.Type.GREAT_NOT_TWELVE, holidays) -> {
                setStyle(v, text, R.drawable.tile_great_holiday)
            }
            isTypeHoliday(Holiday.Type.MAIN, holidays) -> {
                setStyle(v, text, R.drawable.tile_easter)
            }
            day.fasting.type == Fasting.Type.FASTING -> {
                setStyle(v, text, R.drawable.tile_fasting_day)
            }
            day.fasting.type == Fasting.Type.FASTING_DAY -> {
                setStyle(v, text, R.drawable.tile_fasting_day)
            }
            day.fasting.type == Fasting.Type.SOLID_WEEK -> {
                setStyle(v, text, R.drawable.tile_no_fasting)
            }
        }
    }

    private fun styleFastingHoliday(day: Day, v: TileDayLayoutBinding) {
        if (day.fasting.type == Fasting.Type.NONE) return
        for (permission in day.fasting.permissions) {
            when (permission) {
                Fasting.Permission.OIL ->
                    setImg(v, ContextCompat.getDrawable(requireContext(), R.drawable.sun))

                Fasting.Permission.FISH ->
                    setImg(v, ContextCompat.getDrawable(requireContext(), R.drawable.fish))

                Fasting.Permission.VINE ->
                    setImg(v, ContextCompat.getDrawable(requireContext(), R.drawable.vine))

                Fasting.Permission.STRICT ->
                    setImg(v, ContextCompat.getDrawable(requireContext(), R.drawable.triangle))

                Fasting.Permission.NO_EAT -> {
                }
                Fasting.Permission.CAVIAR -> {
                }
                Fasting.Permission.HOT_NO_OIL -> {
                }
                Fasting.Permission.NO_MEAT -> {
                    setImg(v, ContextCompat.getDrawable(requireContext(), R.drawable.eggs))
                }
            }
        }
    }

    private fun setImg(v: TileDayLayoutBinding, drawable: Drawable?) {
        val imgContainer =
                when {
                    v.im3.drawable == null -> v.im3
                    v.im2.drawable == null -> v.im2
                    v.im1.drawable == null -> v.im1
                    else -> return
                }
        imgContainer.setImageDrawable(drawable)
    }

    private fun styleMemoryTypeHoliday(day: Day, v: TileDayLayoutBinding) {
        if (day.memorialType == Day.MemorialType.NONE) return
        val img = ContextCompat.getDrawable(requireContext(), R.drawable.cross)
        if (v.im3.drawable == null) v.im3.setImageDrawable(img)
        else v.im4.setImageDrawable(img)
    }

    private fun setStyle(view: TileDayLayoutBinding, text: TextViewWithCustomFont, style: Int,
                         color: Int = R.color.colorTextHeadHolidays) {

        text.setTextColor(ContextCompat.getColor(requireContext(), color))
        view.container.background = ContextCompat.getDrawable(requireContext(), style)
    }

    private fun isTypeHoliday(type: Holiday.Type, holidays: List<Holiday>): Boolean {
        for (holiday in holidays) {
            if (holiday.typeId == type.id) return true
        }
        return false
    }

    override fun prepareDaysOfWeekRows(dayOfWeek: IntRange) {
        if (tableRows.isEmpty().not()) tableRows.clear()
        for (i in dayOfWeek) {
            val row = createDayOfWeekRow()
            tableRows.add(row)
            row.addView(createDayOtWeek(i))
        }
    }

    private fun getDayNames() = resources.getStringArray(R.array.daysNamesAbb)

    private fun createDayOtWeek(month: Int): TextView {
        return TextViewWithCustomFont(requireContext()).apply {
            val dayNames = getDayNames()
            text = dayNames[month - 1]
            setTextColor(Color.RED)
            textSize = resources.getDimension(R.dimen.size_tile_day_of_week)
            typeface = CustomFont.getFont(requireContext(), getString(R.string.font_basic))
            textAlignment = Layout.Alignment.ALIGN_CENTER.ordinal
            setPadding(0, 0, 20, 0)
        }
    }

    private fun createDayOfWeekRow(): TableRow {
        val row = TableRow(context)
        row.layoutParams = ViewGroup.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT)
        row.gravity = Gravity.CENTER_VERTICAL
        return row
    }

    override fun drawView() {
        if (_monthBinding == null) return //if state restored and _monthBinding == null
        tableRows.forEach { e ->
            if (e.parent == null) monthBinding.tableMonthTile.addView(e)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }

    private val onDragListener = View.OnDragListener { v, event ->
        when (event.action) {
            DragEvent.ACTION_DRAG_STARTED -> {
                // Determines if this View can accept the dragged data
                if (event.clipDescription.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                    // As an example of what your application might do,
                    // applies a blue color tint to the View to indicate that it can accept
                    // data.
                    (v as? ImageView)?.setColorFilter(Color.BLUE)

                    // Invalidate the view to force a redraw in the new tint
                    v?.invalidate()

                    // returns true to indicate that the View can accept the dragged data.
                    true
                } else {
                    // Returns false. During the current drag and drop operation, this View will
                    // not receive events again until ACTION_DRAG_ENDED is sent.
                    false
                }
            }
        }
        true
    }

    private val onClick = View.OnClickListener {
        val r = 0
        val rw = 0
    }
}
