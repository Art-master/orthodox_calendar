package com.artmaster.android.orthodoxcalendar.ui.user_holiday.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.artmaster.android.orthodoxcalendar.R
import com.artmaster.android.orthodoxcalendar.domain.Holiday
import com.artmaster.android.orthodoxcalendar.domain.Holiday.Type.COMMON_MEMORY_DAY
import com.artmaster.android.orthodoxcalendar.domain.Time
import com.artmaster.android.orthodoxcalendar.ui.common.CheckBox
import com.artmaster.android.orthodoxcalendar.ui.common.EditText
import com.artmaster.android.orthodoxcalendar.ui.common.Header
import com.artmaster.android.orthodoxcalendar.ui.common.Select

@Preview(showBackground = true, device = Devices.PIXEL_3)
@Composable
fun UserHolidayLayoutPreview() {
    val time = Time()

    val holiday = Holiday(
        title = "Новый",
        year = time.year,
        month = time.month,
        day = time.dayOfMonth,
        typeId = Holiday.Type.USERS_BIRTHDAY.id
    )

    UserHolidayLayout(holiday)
}

val padding = 8.dp
val spaseBetween = 15.dp

@Composable
fun UserHolidayLayout(holiday: Holiday, onSave: (Holiday) -> Unit = {}) {
    val scroll = rememberScrollState(0)

    var target by rememberSaveable { mutableStateOf(holiday.copy()) }
    val months = stringArrayResource(id = R.array.months_names_gen)
    val types = stringArrayResource(id = R.array.user_holidays_names)

    val onTypeSelect = remember {
        { type: String ->

        }
    }

    val onMonthSelect = remember {
        { month: String ->

        }
    }

    val onDayChange = remember {
        { day: String ->

        }
    }

    val onTitleChange = remember {
        { title: String ->
            target = target.copy(title = title)
        }
    }

    Box {
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(scroll)
        ) {
            Header(title = stringResource(id = R.string.add_holiday))
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
                    selectedOption = months[target.monthWith0],
                    options = months,
                    onSelect = onMonthSelect
                )
            }
            Spacer(modifier = Modifier.height(spaseBetween))
            Row {
                CheckBox(
                    modifier = Modifier.fillMaxWidth(0.4f),
                    title = stringResource(id = R.string.year_title),
                    state = false
                )

                EditText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = padding, end = padding),
                    label = "",
                    value = target.year.toString(),
                    onValueChange = onTitleChange
                )
            }
            Spacer(modifier = Modifier.height(spaseBetween))
            EditText(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .padding(start = padding, end = padding),
                label = stringResource(id = R.string.description_holiday),
                value = target.year.toString(),
                onValueChange = onTitleChange
            )
        }
    }
}