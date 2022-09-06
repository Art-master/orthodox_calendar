package com.artmaster.android.orthodoxcalendar.ui.init.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.artmaster.android.orthodoxcalendar.R
import com.artmaster.android.orthodoxcalendar.ui.calendar_list.fragments.shared.CalendarViewModel
import com.artmaster.android.orthodoxcalendar.ui.theme.TopBarColor

@Preview
@Composable
fun AppBarPreview() {
    AppBar()
}

@Composable
fun AppBar(viewModel: CalendarViewModel = CalendarViewModel()) {
    Surface(
        modifier = Modifier
            .padding(bottom = 5.dp)
            .drawWithContent {
                clipRect(
                    left = 0f,
                    top = 0f,
                    right = size.width,
                    bottom = size.height + 5
                ) {
                    this@drawWithContent.drawContent()
                }
            },
        color = TopBarColor,
        elevation = 3.dp
    ) {
        DropDownYearMenu(currentYear = viewModel.getYear()) {
            viewModel.setYear(it)
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {

            MenuItem(iconId = R.drawable.icon_tile, "Calendar")
            MenuItem(iconId = R.drawable.icon_reset_date, "Reset")
            MenuItem(iconId = R.drawable.icon_settings, "Settings")
            MenuItem(iconId = R.drawable.icon_info, "Info")
        }
    }
}

@Composable
fun MenuItem(iconId: Int, description: String = "", onClick: () -> Unit = {}) {
    IconButton(onClick = onClick) {
        Icon(
            painter = painterResource(iconId),
            contentDescription = description
        )
    }
}