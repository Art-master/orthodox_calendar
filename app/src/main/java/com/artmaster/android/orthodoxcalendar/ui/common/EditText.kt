package com.artmaster.android.orthodoxcalendar.ui.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
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
import com.artmaster.android.orthodoxcalendar.ui.theme.CursorColor
import com.artmaster.android.orthodoxcalendar.ui.theme.DefaultTextColor
import com.artmaster.android.orthodoxcalendar.ui.theme.EditTextErrorBackground
import com.artmaster.android.orthodoxcalendar.ui.theme.customTextSelectionColors

@Composable
fun EditText(
    modifier: Modifier = Modifier,
    label: String,
    leftLabel: Boolean = false,
    value: String,
    isError: Boolean = false,
    maxLines: Int = 1,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onValueChange: (String) -> Unit,
) {
    if (leftLabel) {
        Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
            if (label.isNotEmpty()) {
                EditTextLabel(
                    modifier = Modifier.padding(end = 5.dp),
                    text = label
                )
            }
            EditTextContent(
                modifier = modifier,
                value = value,
                keyboardOptions = keyboardOptions,
                isError = isError,
                maxLines = maxLines,
                onValueChange = onValueChange
            )
        }
    } else {
        Column(modifier = modifier, horizontalAlignment = Alignment.Start) {
            if (label.isNotEmpty()) {
                EditTextLabel(text = label)
            }
            EditTextContent(
                modifier = modifier,
                value = value,
                keyboardOptions = keyboardOptions,
                isError = isError,
                maxLines = maxLines,
                onValueChange = onValueChange
            )
        }
    }

}

@Composable
fun EditTextLabel(modifier: Modifier = Modifier, text: String) {
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
fun EditTextContent(
    modifier: Modifier,
    value: String,
    isError: Boolean,
    maxLines: Int,
    keyboardOptions: KeyboardOptions,
    onValueChange: (String) -> Unit
) {
    CompositionLocalProvider(LocalTextSelectionColors provides customTextSelectionColors) {
        TextField(
            modifier = modifier,
            keyboardOptions = keyboardOptions,
            readOnly = false,
            isError = isError,
            maxLines = maxLines,
            value = value,
            onValueChange = onValueChange,
            trailingIcon = { },
            textStyle = TextStyle(
                fontSize = 25.sp,
                color = DefaultTextColor,
                fontFamily = FontFamily(Font(R.font.cyrillic_old))
            ),
            colors = ExposedDropdownMenuDefaults.textFieldColors(
                cursorColor = CursorColor,
                focusedIndicatorColor = CursorColor,
                leadingIconColor = CursorColor,
                trailingIconColor = CursorColor,
                backgroundColor = if (isError) EditTextErrorBackground else
                    MaterialTheme.colors.onSurface.copy(alpha = TextFieldDefaults.BackgroundOpacity)
            )
        )
    }
}

