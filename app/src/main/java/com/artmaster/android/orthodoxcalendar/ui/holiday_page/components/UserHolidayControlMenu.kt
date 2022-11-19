package com.artmaster.android.orthodoxcalendar.ui.holiday_page.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.Top
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.artmaster.android.orthodoxcalendar.R
import com.artmaster.android.orthodoxcalendar.domain.Holiday
import com.artmaster.android.orthodoxcalendar.ui.theme.Error
import com.artmaster.android.orthodoxcalendar.ui.theme.FiltersContentColor
import com.artmaster.android.orthodoxcalendar.ui.theme.FloatingButtonColor

@Composable
@Preview(showBackground = true, device = Devices.PIXEL_3)
fun UserHolidayControlMenuPreview() {
    Box(Modifier.fillMaxSize()) {
        UserHolidayControlMenu(Holiday())
    }
}


@Composable
fun UserHolidayControlMenu(
    holiday: Holiday,
    onEditClick: (holiday: Holiday) -> Unit = {},
    onDeleteClick: (holiday: Holiday) -> Unit = {}
) {

    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
        FloatingActionButton(
            modifier = Modifier
                .size(45.dp)
                .align(Top)
                .padding(top = 8.dp, end = 8.dp),
            backgroundColor = FloatingButtonColor,
            contentColor = FiltersContentColor,
            onClick = { onEditClick(holiday) }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_baseline_edit_24),
                contentDescription = "",
                modifier = Modifier.rotate(0f)
            )
        }
        FloatingActionButton(
            modifier = Modifier
                .size(45.dp)
                .align(Top)
                .padding(top = 8.dp, end = 8.dp),
            backgroundColor = Error,
            contentColor = FiltersContentColor,
            onClick = { onDeleteClick(holiday) }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_baseline_delete_24),
                contentDescription = "",
                modifier = Modifier.rotate(0f)
            )
        }
    }
}