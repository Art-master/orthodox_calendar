package com.artmaster.android.orthodoxcalendar.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.artmaster.android.orthodoxcalendar.R
import com.artmaster.android.orthodoxcalendar.common.Constants
import com.artmaster.android.orthodoxcalendar.domain.Time
import com.artmaster.android.orthodoxcalendar.ui.theme.DefaultTextColor
import com.artmaster.android.orthodoxcalendar.ui.theme.HeadSymbolTextColor

@Preview(showBackground = true)
@Composable
fun DropDownYearMenuPreview() {
    DropDownYearMenu(height = 49.dp, currentYear = 2022)
}

@Preview(showBackground = true)
@Composable
fun ItemContentPreview() {
    ItemContent(text = "2022")
}

@Composable
fun DropDownYearMenu(height: Dp, currentYear: Int, onYearSelect: (year: Int) -> Unit = {}) {

    val items by remember {
        mutableStateOf(getYears(Time().year))
    }

    var expanded by remember { mutableStateOf(false) }

    var selectedIndex by remember(currentYear) {
        mutableStateOf(getYearIndex(currentYear, items))
    }

    Column(
        modifier = Modifier
            .height(height)
            .wrapContentSize(Alignment.Center)
            .padding(start = 5.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                modifier = Modifier.offset(y = (-15).dp),
                textAlign = TextAlign.Center,
                text = stringResource(id = R.string.ornament_for_app_bar),
                color = DefaultTextColor,
                fontSize = 30.sp,
                fontFamily = FontFamily(Font(R.font.ornament))
            )
            ItemContent(
                modifier = Modifier
                    .padding(start = 5.dp, end = 5.dp),
                //.drawBehind { drawArrowDown(this, size) },
                text = items[selectedIndex],
                color = HeadSymbolTextColor,
                onClick = { expanded = true })

            Text(
                modifier = Modifier
                    .offset(y = (-15).dp)
                    .scale(scaleX = -1f, scaleY = 1f),
                textAlign = TextAlign.Center,
                text = stringResource(id = R.string.ornament_for_app_bar),
                color = DefaultTextColor,
                fontSize = 30.sp,
                fontFamily = FontFamily(Font(R.font.ornament))
            )
        }

        DropdownMenu(
            modifier = Modifier
                .width(100.dp)
                .height(500.dp)
                .align(Alignment.CenterHorizontally),
            expanded = expanded,
            onDismissRequest = { expanded = false },
            offset = DpOffset(y = 0.dp, x = 20.dp)
        ) {
            items.forEachIndexed { index, text ->
                key(index) {
                    DropdownMenuItem(
                        onClick = {
                            selectedIndex = index
                            val year = items[index]
                            onYearSelect(year.toInt())
                            expanded = false
                        }) {

                        val color =
                            if (selectedIndex == index) HeadSymbolTextColor else DefaultTextColor

                        ItemContent(text = text, color = color) {
                            selectedIndex = index
                            val year = items[index]
                            onYearSelect(year.toInt())
                            expanded = false
                        }
                    }
                }
            }
        }
    }
}

fun drawArrowDown(scope: DrawScope, size: Size) {
    scope.apply {
        val trianglePath = Path().apply {
            moveTo(x = size.width / 2, y = size.height)
            lineTo(x = 20f, y = size.height - 15f)
            lineTo(x = size.width - 20f, y = size.height - 15f)
            close()
        }
        drawPath(
            path = trianglePath,
            color = DefaultTextColor,
            style = Fill
        )
    }
}


@Composable
fun ItemContent(
    modifier: Modifier = Modifier,
    text: String,
    color: Color = DefaultTextColor,
    onClick: () -> Unit = {}
) {
    Text(
        modifier = modifier.clickable(onClick = onClick),
        textAlign = TextAlign.Center,
        text = text,
        color = color,
        fontSize = 30.sp,
        fontFamily = FontFamily(Font(R.font.ort_basic))
    )
}

fun getYearIndex(currentYear: Int, years: List<String>): Int {
    return years.indexOf(currentYear.toString())
}


fun getYears(currentYear: Int): List<String> {
    val size = Constants.HolidayList.PAGE_SIZE.value
    val firstYear = currentYear - size / 2
    val years = ArrayList<String>(size)
    for (element in firstYear until firstYear + size) {
        years.add(element.toString())
    }
    return years
}