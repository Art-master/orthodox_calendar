package com.artmaster.android.orthodoxcalendar.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.artmaster.android.orthodoxcalendar.R
import com.artmaster.android.orthodoxcalendar.common.Settings
import com.artmaster.android.orthodoxcalendar.ui.common.CheckBox
import com.artmaster.android.orthodoxcalendar.ui.theme.DefaultTextColor
import com.artmaster.android.orthodoxcalendar.ui.theme.HeadSymbolTextColor

@Preview(showBackground = true)
@Composable
fun PreviewSettingsLayout() {
    SettingsLayout()
}

@Composable
fun SettingsLayout() {
    Column(Modifier.fillMaxSize()) {
        Header()

        CheckBoxSettings(
            setting = Settings.Name.OFF_START_ANIMATION,
            title = stringResource(id = R.string.settings_average_notifications)
        )
    }
}

@Composable
fun CheckBoxSettings(setting: Settings.Name, title: String) {
    CheckBox(title = title, initialState = setting.defValue.toBoolean())
}

@Composable
fun Header() {
    val title = stringResource(id = R.string.settings_name)

    Column(
        Modifier
            .fillMaxWidth()
            .padding(top = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        val annotatedString = buildAnnotatedString {
            Ornament(builder = this, text = stringResource(id = R.string.ornament_for_headers_left))
            append(title)
            Ornament(
                builder = this,
                text = stringResource(id = R.string.ornament_for_headers_right)
            )
        }

        Text(
            color = DefaultTextColor,
            text = annotatedString,
            fontSize = 30.sp,
            fontFamily = FontFamily(Font(R.font.cyrillic_old, FontWeight.Normal)),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun Ornament(builder: AnnotatedString.Builder, text: String) {
    builder.apply {
        withStyle(
            style = SpanStyle(
                color = HeadSymbolTextColor,
                fontFamily = FontFamily(Font(R.font.ornament)),
                fontSize = 15.sp
            )
        ) {
            append(text)
        }
    }
}