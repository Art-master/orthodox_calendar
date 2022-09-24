package com.artmaster.android.orthodoxcalendar.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.artmaster.android.orthodoxcalendar.R
import com.artmaster.android.orthodoxcalendar.ui.theme.AppTheme
import com.artmaster.android.orthodoxcalendar.ui.theme.CheckBoxCheckedColor
import com.artmaster.android.orthodoxcalendar.ui.theme.CheckBoxCheckmarkColor
import com.artmaster.android.orthodoxcalendar.ui.theme.DefaultTextColor

@Preview
@Composable
fun CustomCheckBoxPreview() {
    AppTheme {
        Column(Modifier.fillMaxSize()) {
            CheckBox(title = "Какое-то название", state = false)
        }
    }
}


@Composable
fun CheckBox(
    modifier: Modifier = Modifier,
    title: String,
    state: Boolean,
    onCheck: (value: Boolean) -> Unit = {}
) {

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = state,
            onCheckedChange = onCheck,
            colors = CheckboxDefaults.colors(
                checkedColor = CheckBoxCheckedColor,
                checkmarkColor = CheckBoxCheckmarkColor
            )
        )

        Text(
            modifier = modifier
                .fillMaxWidth()
                .clickable {
                    onCheck(state.not())
                }
                .then(modifier),
            text = title,
            fontSize = 20.sp,
            color = DefaultTextColor,
            fontFamily = FontFamily(Font(R.font.cyrillic_old)),
            textAlign = TextAlign.Left
        )
    }
}
