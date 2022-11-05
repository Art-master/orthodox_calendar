package com.artmaster.android.orthodoxcalendar.ui.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.artmaster.android.orthodoxcalendar.R
import com.artmaster.android.orthodoxcalendar.ui.theme.DefaultTextColor


@Composable
fun Select(
    modifier: Modifier = Modifier,
    label: String,
    selectedOption: String,
    leftLabel: Boolean = false,
    options: Array<String>,
    onSelect: (String) -> Unit
) {

    if (leftLabel) {
        Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
            if (label.isNotEmpty()) {
                SelectLabel(modifier = Modifier.padding(end = 5.dp), text = label)
            }
            SelectContent(
                modifier = modifier,
                selectedOption = selectedOption,
                options = options,
                onSelect = onSelect
            )
        }
    } else {
        Column(modifier = modifier, horizontalAlignment = Alignment.Start) {
            if (label.isNotEmpty()) {
                SelectLabel(text = label)
            }
            SelectContent(
                modifier = modifier,
                selectedOption = selectedOption,
                options = options,
                onSelect = onSelect
            )
        }
    }
}

@Composable
fun SelectLabel(modifier: Modifier = Modifier, text: String) {
    Text(
        modifier = modifier.padding(start = 5.dp),
        textAlign = TextAlign.Center,
        text = text,
        color = DefaultTextColor,
        fontSize = 25.sp,
        fontFamily = FontFamily(Font(R.font.cyrillic_old))
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SelectContent(
    modifier: Modifier = Modifier,
    selectedOption: String,
    options: Array<String>,
    onSelect: (String) -> Unit
) {

    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = {
        expanded = !expanded
    }) {
        TextField(
            modifier = modifier,
            readOnly = true,
            value = selectedOption,
            onValueChange = { },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            },
            textStyle = TextStyle(
                fontSize = 25.sp,
                color = DefaultTextColor,
                fontFamily = FontFamily(Font(R.font.cyrillic_old))
            ),
            colors = ExposedDropdownMenuDefaults.textFieldColors()
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = {
            expanded = false
        }) {
            options.forEach { selectionOption ->
                DropdownMenuItem(onClick = {
                    onSelect(selectionOption)
                    expanded = false
                }) {
                    Text(text = selectionOption)
                }
            }
        }
    }
}