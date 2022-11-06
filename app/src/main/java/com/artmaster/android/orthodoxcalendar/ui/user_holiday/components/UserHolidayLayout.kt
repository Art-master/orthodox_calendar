package com.artmaster.android.orthodoxcalendar.ui.user_holiday.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.artmaster.android.orthodoxcalendar.R
import com.artmaster.android.orthodoxcalendar.domain.Holiday
import com.artmaster.android.orthodoxcalendar.domain.Holiday.Type
import com.artmaster.android.orthodoxcalendar.domain.Holiday.Type.COMMON_MEMORY_DAY
import com.artmaster.android.orthodoxcalendar.domain.Time
import com.artmaster.android.orthodoxcalendar.ui.common.*

@Preview(showBackground = true, device = Devices.PIXEL_3)
@Composable
fun UserHolidayLayoutPreview() {
    val time = Time()

    val holiday = Holiday(
        title = "Новый",
        year = time.year,
        month = time.month,
        day = time.dayOfMonth,
        typeId = Type.USERS_BIRTHDAY.id
    )

    UserHolidayLayout(holiday)
}

val padding = 8.dp
val spaseBetween = 15.dp

@Composable
fun UserHolidayLayout(
    holiday: Holiday? = null,
    onSave: (Holiday) -> Unit = {}
) {
    val scroll = rememberScrollState(0)
    val title = stringResource(id = R.string.add_holiday)

    var target by rememberSaveable {
        mutableStateOf(holiday?.copy() ?: createNewHolidayTemplate(title))
    }
    val months = stringArrayResource(id = R.array.months_names_gen)
    val types = stringArrayResource(id = R.array.user_holidays_names)

    val onTypeSelect = remember {
        { type: String ->
            val index = types.indexOf(type)
            target = target.copy(typeId = COMMON_MEMORY_DAY.id + index.inc())
        }
    }

    val onMonthSelect = remember {
        { monthName: String ->
            val index = months.indexOf(monthName)
            target = target.copy(month = index.inc())
        }
    }

    val onDayChange = remember {
        { day: String ->
            target = target.copy(day = day.toInt())
        }
    }

    val onTitleChange = remember {
        { title: String ->
            target = target.copy(title = title)
        }
    }
    var yearEnabled by rememberSaveable { mutableStateOf(target.year != 0) }
    val onYearFlagChange = remember {
        { flag: Boolean ->
            yearEnabled = flag
        }
    }

    val onYearChange = remember {
        { year: String ->
            target.year = year.toInt()
        }
    }

    val onDescriptionChange = remember {
        { description: String ->
            target.description = description
        }
    }

    Box {
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(scroll)
        ) {
            Header(title = title)
            Spacer(modifier = Modifier.height(spaseBetween))
            Select(
                modifier = Modifier.padding(start = padding, end = padding),
                label = stringResource(id = R.string.type_holiday),
                selectedOption = types[target.typeId.dec() - COMMON_MEMORY_DAY.id],
                options = types,
                leftLabel = true,
                onSelect = onTypeSelect
            )
            Spacer(modifier = Modifier.height(spaseBetween))
            EditText(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = padding, end = padding),
                label = stringResource(id = R.string.name_holiday_title),
                value = target.title,
                onValueChange = onTitleChange
            )
            Spacer(modifier = Modifier.height(spaseBetween))
            Row {
                EditText(
                    modifier = Modifier
                        .width(100.dp)
                        .padding(start = padding, end = padding),
                    label = stringResource(id = R.string.day_title),
                    value = target.day.toString(),
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
                        value = target.year.toString(),
                        onValueChange = onYearChange
                    )
                }
            }
            Spacer(modifier = Modifier.height(spaseBetween))
            EditText(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .padding(start = padding, end = padding),
                label = stringResource(id = R.string.description_holiday),
                value = target.description,
                onValueChange = onDescriptionChange
            )

            Spacer(modifier = Modifier.height(spaseBetween))
            StyledButton(title = stringResource(id = R.string.save_holiday))
        }
    }
}

fun createNewHolidayTemplate(title: String): Holiday {
    val time = Time()
    return Holiday().apply {
        typeId = Type.USERS_BIRTHDAY.id
        day = time.dayOfMonth
        month = time.month
        monthWith0 = time.monthWith0
        year = time.year
        this.title = title
    }
}