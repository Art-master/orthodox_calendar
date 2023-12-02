package com.artmaster.android.orthodoxcalendar.ui.user_holiday_page

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import com.artmaster.android.orthodoxcalendar.R
import com.artmaster.android.orthodoxcalendar.domain.Filter
import com.artmaster.android.orthodoxcalendar.domain.Holiday
import com.artmaster.android.orthodoxcalendar.domain.Holiday.Type
import com.artmaster.android.orthodoxcalendar.domain.Holiday.Type.COMMON_MEMORY_DAY
import com.artmaster.android.orthodoxcalendar.domain.Time
import com.artmaster.android.orthodoxcalendar.ui.common.CheckBox
import com.artmaster.android.orthodoxcalendar.ui.common.EditText
import com.artmaster.android.orthodoxcalendar.ui.common.Header
import com.artmaster.android.orthodoxcalendar.ui.common.Select
import com.artmaster.android.orthodoxcalendar.ui.common.StyledButton
import com.artmaster.android.orthodoxcalendar.ui.tile_calendar_page.components.Spinner
import com.artmaster.android.orthodoxcalendar.ui.viewmodel.CalendarViewModel
import kotlinx.coroutines.launch

@Preview(showBackground = true, device = Devices.PIXEL_3)
@Composable
fun UserHolidayLayoutPreview() {
    UserHolidayLayout(0)
}

val padding = 8.dp
val spaseBetween = 15.dp
const val MAX_TITLE_SIZE = 50
const val MAX_YEAR_LENGTH = 4
const val MAX_DAYS_IN_MONTH_COUNT = 31
const val MAX_DESCRIPTION_SIZE = 1000
val time = Time()

@Composable
fun UserHolidayLayout(
    holidayId: Long? = null,
    viewModel: CalendarViewModel = CalendarViewModel(),
    onSave: (Holiday) -> Unit = {}
) {
    val scope = rememberCoroutineScope()
    var target by rememberSaveable { mutableStateOf(Holiday(id = -1)) }
    val title = stringResource(id = R.string.add_holiday)

    LaunchedEffect(Unit) {
        if (holidayId != null) {
            scope.launch {
                target = viewModel.getFullHolidayById(holidayId)
            }
        } else target = createNewHolidayTemplate(title)
    }

    if (target.id == -1L) {
        Spinner()
        return
    }

    val scroll = rememberScrollState(0)

    val months = stringArrayResource(id = R.array.months_names_gen)
    val types = stringArrayResource(id = R.array.user_holidays_names)

    val onTypeSelect = remember {
        { type: String ->
            val index = types.indexOf(type)
            target = target.copy(typeId = COMMON_MEMORY_DAY.id + index.inc())
        }
    }

    var dayError by rememberSaveable { mutableStateOf("") }
    val onDayChange = remember {
        { day: String ->
            if (day.isDigitsOnly() && day.length <= 2) {
                dayError =
                    if (day.isEmpty() || day.toInt() > MAX_DAYS_IN_MONTH_COUNT) "error" else ""
                target = if (day.isEmpty()) {
                    target.copy(day = 0)
                } else {
                    target.copy(day = day.toInt())
                }
            }
        }
    }

    var yearEnabled by rememberSaveable { mutableStateOf(target.year != 0) }
    val onYearFlagChange = remember {
        { flag: Boolean ->
            yearEnabled = flag
        }
    }

    val onMonthSelect = remember {
        { monthName: String ->
            val index = months.indexOf(monthName)
            if (yearEnabled) {
                val time = Time()
                time.calendar.set(time.year, index, time.dayOfMonth)
                if (target.day > time.daysInMonth) {
                    target = target.copy(day = time.daysInMonth)
                    dayError = ""
                }
            }

            target = target.copy(month = index.inc())
        }
    }

    var titleError by rememberSaveable { mutableStateOf("") }
    val onTitleChange = remember {
        { title: String ->
            titleError = if (title.length > MAX_TITLE_SIZE) "error" else ""
            target = target.copy(title = title)
        }
    }

    var yearError by rememberSaveable { mutableStateOf("") }
    val onYearChange = remember {
        { year: String ->
            if (year.isDigitsOnly() && year.length <= MAX_YEAR_LENGTH) {
                yearError = if (year.isEmpty() || year.toInt() > time.year) "error" else ""
                target = if (year.isEmpty()) {
                    target.copy(year = 0)
                } else {
                    target.copy(year = year.toInt())
                }
            }

        }
    }

    var descriptionError by rememberSaveable { mutableStateOf("") }
    val onDescriptionChange = remember {
        { description: String ->
            descriptionError = if (description.length > MAX_DESCRIPTION_SIZE) "error" else ""
            target = target.copy(description = description)
        }
    }

    val onCheckAndSave = remember {
        { holiday: Holiday ->
            if (!yearEnabled || holiday.typeId == Type.USERS_NAME_DAY.id) holiday.year = 0
            holiday.description = holiday.description.trim()
            holiday.title = holiday.title.trim()
            holiday.isCreatedByUser = true

            checkHolidayFilters(holiday.typeId) {
                viewModel.addFilter(it)
            }
            onSave(holiday)
        }
    }

    Box {
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(scroll)
        ) {
            Header(title = title)

            //TYPE
            Spacer(modifier = Modifier.height(spaseBetween))
            Select(
                modifier = Modifier.padding(start = padding, end = padding),
                label = stringResource(id = R.string.type_holiday),
                selectedOption = types[target.typeId.dec() - COMMON_MEMORY_DAY.id],
                options = types,
                leftLabel = true,
                onSelect = onTypeSelect
            )

            //TITLE
            Spacer(modifier = Modifier.height(spaseBetween))
            EditText(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = padding, end = padding),
                label = stringResource(id = R.string.name_holiday_title),
                value = target.title,
                isError = titleError.isNotEmpty(),
                onValueChange = onTitleChange
            )

            //DAY AND MONTH
            Spacer(modifier = Modifier.height(spaseBetween))
            Row {
                EditText(
                    modifier = Modifier
                        .width(150.dp)
                        .padding(start = padding, end = padding),
                    label = stringResource(id = R.string.day_title),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    value = (if (target.day > 0) target.day else "").toString(),
                    isError = dayError.isNotEmpty(),
                    onValueChange = onDayChange
                )

                Select(
                    modifier = Modifier.padding(start = padding, end = padding),
                    label = stringResource(id = R.string.month_title),
                    selectedOption = months[target.getMonthWith0()],
                    options = months,
                    onSelect = onMonthSelect
                )
            }

            //YEAR
            Spacer(modifier = Modifier.height(spaseBetween))
            if (target.typeId != Type.USERS_NAME_DAY.id) {
                Row {
                    CheckBox(
                        modifier = Modifier.fillMaxWidth(0.4f),
                        title = stringResource(id = R.string.year_title),
                        state = yearEnabled,
                        onCheck = onYearFlagChange
                    )

                    EditText(
                        modifier = Modifier
                            .fillMaxWidth()
                            .alpha(if (yearEnabled) 1f else 0f)
                            .padding(start = padding, end = padding),
                        label = "",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        value = (if (target.year > 0) target.year else "").toString(),
                        isError = yearError.isNotEmpty(),
                        onValueChange = onYearChange
                    )
                }
            }

            //DESCRIPTION
            Spacer(modifier = Modifier.height(spaseBetween))
            EditText(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .padding(start = padding, end = padding),
                label = stringResource(id = R.string.description_holiday),
                value = target.description,
                isError = descriptionError.isNotEmpty(),
                maxLines = 15,
                onValueChange = onDescriptionChange
            )

            //SUBMIT BUTTON
            Spacer(modifier = Modifier.height(spaseBetween))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                StyledButton(
                    title = stringResource(id = R.string.save_holiday),
                    enabled = titleError.isEmpty()
                            && descriptionError.isEmpty()
                            && yearError.isEmpty()
                            && dayError.isEmpty(),
                    onClick = { onCheckAndSave(target) })
            }
        }
    }
}

fun createNewHolidayTemplate(title: String): Holiday {
    val time = Time()
    return Holiday().apply {
        typeId = Type.USERS_BIRTHDAY.id
        day = time.dayOfMonth
        month = time.month
        year = time.year
        this.title = title
    }
}

fun checkHolidayFilters(typeId: Int, onFilterApply: (filter: Filter) -> Unit) {
    if (Filter.values().any { it.enabled })
        when (typeId) {
            Type.USERS_NAME_DAY.id -> onFilterApply(Filter.NAME_DAYS)
            Type.USERS_BIRTHDAY.id -> onFilterApply(Filter.BIRTHDAYS)
            Type.USERS_MEMORY_DAY.id -> onFilterApply(Filter.MEMORY_DAYS)
        }
}