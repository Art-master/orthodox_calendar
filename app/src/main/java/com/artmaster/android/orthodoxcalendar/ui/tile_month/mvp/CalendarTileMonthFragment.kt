package com.artmaster.android.orthodoxcalendar.ui.tile_month.mvp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import com.artmaster.android.orthodoxcalendar.common.Constants
import com.artmaster.android.orthodoxcalendar.domain.Filter
import com.artmaster.android.orthodoxcalendar.domain.SharedTime
import com.artmaster.android.orthodoxcalendar.ui.calendar_list.fragments.shared.CalendarViewModel
import moxy.MvpAppCompatFragment

internal class CalendarTileMonthFragment : MvpAppCompatFragment() {

    private val viewModel: CalendarViewModel by viewModels({ requireParentFragment() })

    private var time: SharedTime = SharedTime()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        time = requireArguments().getParcelable(Constants.Keys.TIME.value) ?: SharedTime()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {

            }
        }
    }

    private fun getFilters(): ArrayList<Filter> {
        return requireArguments().getParcelableArrayList(Constants.Keys.FILTERS.value)!!
    }

/*

*//*    override fun onCreateView(inflater: LayoutInflater, groupContainer: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMonthTileCalendarBinding.inflate(inflater, groupContainer, false)
        layoutManager = LinearLayoutManager(context)
        return binding.root
    }*//*

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAnimation()
        binding.root.setOnDragListener(onDragListener)
        binding.root.setOnClickListener(onClick)

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
        set.setTarget(binding.ringLoading)
        set.start()
    }

    private fun getDayLayout() = TileDayLayoutBinding.inflate(layoutInflater)

    override fun setFocus() {
        if (_binding == null) return
        val id = time.day
        if (id == 0) return
        val daysOfWeek = 0 until binding.tableMonthTile.childCount
        for (i in daysOfWeek) {
            val row = binding.tableMonthTile.getChildAt(i) as TableRow
            val view = row.findViewById<View>(id) ?: continue
            view.requestFocus()
            return
        }
    }

    override fun clearView() {
        if (_binding == null) return //if state restored and _monthBinding == null
        setVisibility()
        binding.tableMonthTile.removeAllViews()
    }

    private fun setVisibility() {
        binding.recyclerViewDayHolidays.visibility = RecyclerView.VISIBLE
        binding.tableMonthTile.visibility = TableLayout.VISIBLE
        binding.ringLoading.visibility = ImageView.GONE
        binding.crossLoading.visibility = ImageView.GONE
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
        if (binding.recyclerViewDayHolidays.layoutManager == null) {
            binding.recyclerViewDayHolidays.layoutManager = layoutManager
        }
        binding.recyclerViewDayHolidays.adapter =
            HolidayDayAdapter(holidays, requireContext(), getFilters())
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





    override fun prepareDaysOfWeekRows(dayOfWeek: IntRange) {
        if (tableRows.isEmpty().not()) tableRows.clear()
        for (i in dayOfWeek) {
            val row = createDayOfWeekRow()
            tableRows.add(row)
            row.addView(createDayOtWeek(i))
        }
    }

    private fun getDayNames() = resources.getStringArray(R.array.daysNamesAbb)
    private fun getDayOfWeekLayout() = TileDayNameLayoutBinding.inflate(layoutInflater)

    private fun createDayOtWeek(dayOfWeek: Int): ConstraintLayout {
        val layout = getDayOfWeekLayout()
        val dayNames = getDayNames()
        layout.nameDay.apply {
            text = dayNames[dayOfWeek - 1]
            val color = if (dayOfWeek == 7) Color.RED else {
                ContextCompat.getColor(requireContext(), R.color.text)
            }
            setTextColor(color)
        }
        return layout.root
    }

    private fun createDayOfWeekRow(): TableRow {
        val row = TableRow(context)
        row.layoutParams = ViewGroup.LayoutParams(
            TableRow.LayoutParams.MATCH_PARENT,
            TableRow.LayoutParams.WRAP_CONTENT
        )
        row.gravity = Gravity.CENTER_VERTICAL
        return row
    }

    override fun drawView() {
        if (_binding == null) return //if state restored and _monthBinding == null
        tableRows.forEach { e ->
            if (e.parent == null) binding.tableMonthTile.addView(e)
        }
    }

    private val onClick = View.OnClickListener {
        val r = 0
        val rw = 0
    }*/
}
