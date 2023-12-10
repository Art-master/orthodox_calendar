package com.artmaster.android.orthodoxcalendar.ui.holiday_page.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
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
        UserHolidayControlMenu(holiday = Holiday(), onEditClick = {}, onDeleteClick = {})
    }
}


@Composable
fun UserHolidayControlMenu(
    modifier: Modifier = Modifier,
    holiday: Holiday,
    onEditClick: (holiday: Holiday) -> Unit,
    onDeleteClick: (holiday: Holiday) -> Unit
) {

    val onEditClickRemembered by rememberUpdatedState { onEditClick(holiday) }
    val onDeleteClickRemembered by rememberUpdatedState { onDeleteClick(holiday) }

    Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
        FloatingActionButton(
            modifier = Modifier
                .size(45.dp)
                .align(Top)
                .padding(top = 8.dp, end = 8.dp),
            backgroundColor = FloatingButtonColor,
            contentColor = FiltersContentColor,
            onClick = onEditClickRemembered
        ) {
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
            onClick = onDeleteClickRemembered
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_baseline_delete_24),
                contentDescription = "",
                modifier = Modifier.rotate(0f)
            )
        }
    }
}