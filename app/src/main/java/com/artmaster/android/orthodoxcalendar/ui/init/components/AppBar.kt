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
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.artmaster.android.orthodoxcalendar.R
import com.artmaster.android.orthodoxcalendar.ui.CalendarViewModel
import com.artmaster.android.orthodoxcalendar.ui.Route
import com.artmaster.android.orthodoxcalendar.ui.theme.TopBarColor

@Preview
@Composable
fun AppBarPreview() {
    val navController = rememberNavController()
    AppBar(navController = navController)
}

@Composable
fun AppBar(viewModel: CalendarViewModel = CalendarViewModel(), navController: NavHostController) {
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

            MenuItem(iconId = R.drawable.icon_tile, "Calendar") {
                val currentRoute = navController.currentDestination?.route ?: ""
                if (currentRoute == Route.TILE_CALENDAR.name) {
                    navController.navigate(Route.LIST_CALENDAR.name)
                } else navController.navigate(Route.TILE_CALENDAR.name)

            }
            MenuItem(iconId = R.drawable.icon_reset_date, "Reset")
            MenuItem(iconId = R.drawable.icon_settings, "Settings") {
                navController.navigate(Route.SETTINGS.name)
            }
            MenuItem(iconId = R.drawable.icon_info, "Info") {
                navController.navigate(Route.APP_INFO.name)
            }
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