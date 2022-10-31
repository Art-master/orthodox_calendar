package com.artmaster.android.orthodoxcalendar.ui.init.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
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
    val year = remember { mutableStateOf(2022) }
    DropDownYearMenu(currentYear = year)
}

@Preview(showBackground = true)
@Composable
fun ItemContentPreview() {
    ItemContent(text = "2022")
}

@Composable
fun DropDownYearMenu(currentYear: MutableState<Int>, onYearSelect: (year: Int) -> Unit = {}) {

    val items by remember {
        mutableStateOf(getYears(Time().year))
    }

    var expanded by remember { mutableStateOf(false) }

    var selectedIndex by remember(currentYear) {
        mutableStateOf(getYearIndex(currentYear.value, items))
    }

    Box(modifier = Modifier.wrapContentSize(Alignment.Center)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                textAlign = TextAlign.Center,
                text = "<",
                color = DefaultTextColor,
                fontSize = 35.sp,
                fontFamily = FontFamily(Font(R.font.ort_basic, FontWeight.Normal))
            )
            ItemContent(
                text = items[selectedIndex],
                color = HeadSymbolTextColor,
                onClick = { expanded = true })

            Text(
                textAlign = TextAlign.Center,
                text = ">",
                color = DefaultTextColor,
                fontSize = 35.sp,
                fontFamily = FontFamily(Font(R.font.ort_basic, FontWeight.Normal))
            )
        }


        DropdownMenu(
            modifier = Modifier
                .width(100.dp)
                .align(Alignment.BottomCenter),
            expanded = expanded,
            onDismissRequest = { expanded = false }
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

                        ItemContent(text = text) {
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


@Composable
fun ItemContent(text: String, color: Color = DefaultTextColor, onClick: () -> Unit = {}) {
    Text(
        modifier = Modifier.clickable(onClick = onClick),
        textAlign = TextAlign.Center,
        text = text,
        color = color,
        fontSize = 30.sp,
        fontFamily = FontFamily(Font(R.font.ort_basic, FontWeight.Normal))
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