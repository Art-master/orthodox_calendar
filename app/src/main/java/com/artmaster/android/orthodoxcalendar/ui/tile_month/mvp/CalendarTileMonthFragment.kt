package com.artmaster.android.orthodoxcalendar.ui.tile_month.mvp

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.content.ClipDescription
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Layout
import android.view.*
import android.widget.ImageView
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.artmaster.android.orthodoxcalendar.R
import com.artmaster.android.orthodoxcalendar.common.Constants
import com.artmaster.android.orthodoxcalendar.data.components.CustomFont
import com.artmaster.android.orthodoxcalendar.data.components.TextViewWithCustomFont
import com.artmaster.android.orthodoxcalendar.databinding.FragmentMonthTileCalendarBinding
import com.artmaster.android.orthodoxcalendar.databinding.TileDayLayoutBinding
import com.artmaster.android.orthodoxcalendar.domain.*
import com.artmaster.android.orthodoxcalendar.ui.calendar_list.fragments.shared.CalendarViewModel
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

    private val viewModel: CalendarViewModel by viewModels({ requireParentFragment() })

    private var time: SharedTime = SharedTime()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!presenter.isInRestoreState(this)) {
            presenter.attachView(this)

            val filters = getFilters()
            time = requireArguments().getParcelable(Constants.Keys.TIME.value) ?: SharedTime()

            lifecycleScope.launch {
                presenter.viewIsReady(time, filters ?: ArrayList())
            }
        }
    }

    private fun getFilters(): ArrayList<Filter> {
        return requireArguments().getParcelableArrayList(Constants.Keys.FILTERS.value)!!
    }

    override fun onCreateView(inflater: LayoutInflater, groupContainer: ViewGroup?, savedInstanceState: Bundle?): View {
        _monthBinding = FragmentMonthTileCalendarBinding.inflate(inflater, groupContainer, false)
        layoutManager = LinearLayoutManager(context)
        return monthBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAnimation()
        monthBinding.root.setOnDragListener(onDragListener)
        monthBinding.root.setOnClickListener(onClick)

        viewModel.time.observe(viewLifecycleOwner, { item ->
            time = item
        })
    }

    override fun onResume() {
        super.onResume()
        presenter.viewIsCreated()
        setFocus()
    }

    private fun initAnimation() {
        val set = AnimatorInflater.loadAnimator(context, R.animator.loading_animator) as AnimatorSet
        set.setTarget(monthBinding.ringLoading)
        set.start()
    }

    private fun getDayLayout() = TileDayLayoutBinding.inflate(layoutInflater)

    override fun setFocus() {
        if (_monthBinding == null) return
        val id = time.day
        if (id == 0) return
        val daysOfWeek = 0 until monthBinding.tableMonthTile.childCount
        for (i in daysOfWeek) {
            val row = monthBinding.tableMonthTile.getChildAt(i) as TableRow
            val view = row.findViewById<View>(id) ?: continue
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
        v.root.setOnFocusChangeListener { _, hasFocus -> onChangeFocus(v, hasFocus, day) }
        v.root.id = day.dayOfMonth

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

    private fun onChangeFocus(view: TileDayLayoutBinding, hasFocus: Boolean, day: Day) {
        val bg = view.container.background
        if (hasFocus) {
            initRecyclerView(day.holidays)
            setDayLayoutColorFilter(bg, view.root)
            viewModel.setDayOfMonth(view.root.id)
        } else bg.clearColorFilter()
    }

    private fun setDayLayoutColorFilter(drawable: Drawable, view: View) {
        val color = ContextCompat.getColor(view.context!!, R.color.colorSelectTile)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            drawable.colorFilter = BlendModeColorFilter(color, BlendMode.MULTIPLY)
        } else {
            drawable.setColorFilter(color, PorterDuff.Mode.MULTIPLY)
        }
    }

    private fun initRecyclerView(holidays: List<Holiday>) {
        if (monthBinding.recyclerViewDayHolidays.layoutManager == null) {
            monthBinding.recyclerViewDayHolidays.layoutManager = layoutManager
        }
        monthBinding.recyclerViewDayHolidays.adapter = HolidayDayAdapter(holidays, requireContext(), getFilters())
    }

    private fun styleDayView(view: TileDayLayoutBinding, day: Day, dayOfWeek: Int) {
        val text = view.numDay
        text.text = day.dayOfMonth.toString()

        if (dayOfWeek == Time.Day.SUNDAY.num) text.setTextColor(Color.RED)

        styleFastingHoliday(day, view)
        styleMemoryTypeHoliday(day, view)
        styleTypeHoliday(day, view)
        setHolidaysAttributeVisibility(view, day)
    }

    private fun setHolidaysAttributeVisibility(view: TileDayLayoutBinding, day: Day) {
        view.dot.visibility = if (day.holidays.isEmpty()) View.GONE else View.VISIBLE
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
                    setImg(v, ContextCompat.getDrawable(v.root.context, R.drawable.sun))

                Fasting.Permission.FISH ->
                    setImg(v, ContextCompat.getDrawable(v.root.context, R.drawable.fish))

                Fasting.Permission.VINE ->
                    setImg(v, ContextCompat.getDrawable(v.root.context, R.drawable.vine))

                Fasting.Permission.STRICT ->
                    setImg(v, ContextCompat.getDrawable(v.root.context, R.drawable.triangle))

                Fasting.Permission.NO_EAT -> {
                }
                Fasting.Permission.CAVIAR -> {
                }
                Fasting.Permission.HOT_NO_OIL -> {
                }
                Fasting.Permission.NO_MEAT -> {
                    setImg(v, ContextCompat.getDrawable(v.root.context, R.drawable.eggs))
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

        text.setTextColor(ContextCompat.getColor(view.root.context, color))
        view.container.background = ContextCompat.getDrawable(view.root.context, style)
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

    //TODO resize tile calendar
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
