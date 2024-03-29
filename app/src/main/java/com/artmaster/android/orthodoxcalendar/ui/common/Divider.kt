package com.artmaster.android.orthodoxcalendar.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.artmaster.android.orthodoxcalendar.R
import com.artmaster.android.orthodoxcalendar.ui.theme.Background
import com.artmaster.android.orthodoxcalendar.ui.theme.DefaultTextColor
import com.artmaster.android.orthodoxcalendar.ui.theme.HeaderTextColor


@Composable
@Preview(showBackground = true)
fun DividerWithTextPreview() {
    Row(modifier = Modifier.fillMaxSize()) {
        DividerWithText(text = "Уведомления")
    }
}

@Composable
@Preview(showBackground = true)
fun DividerPreview() {
    Row(modifier = Modifier.fillMaxSize()) {
        Divider()
    }
}

@Composable
fun DividerWithText(color: Color = HeaderTextColor, text: String) {
    DividerWithText {
        Text(
            modifier = Modifier
                .background(Background)
                .padding(start = 3.dp, end = 3.dp)
                .offset(y = 6.dp),
            color = color,
            text = text,
            fontSize = 20.sp,
            fontFamily = FontFamily(Font(R.font.cyrillic_old)),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun DividerWithText(color: Color = HeaderTextColor, content: @Composable () -> Unit) {
    Box(contentAlignment = Alignment.Center) {
        Row(
            modifier = Modifier.padding(top = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                color = color,
                text = stringResource(id = R.string.ornament_for_name_date_left),
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.ornament)),
                textAlign = TextAlign.Center
            )
            Spacer(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(1.dp)
                    .offset(y = 3.dp)
                    .background(color)
            )
            Text(
                color = color,
                text = stringResource(id = R.string.ornament_for_name_date_right),
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.ornament)),
                textAlign = TextAlign.Center
            )
        }
        content()
    }
}

@Composable
fun Divider() {
    Row(
        modifier = Modifier.padding(top = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            color = DefaultTextColor,
            text = stringResource(id = R.string.ornament_for_name_date_left),
            fontSize = 16.sp,
            fontFamily = FontFamily(Font(R.font.ornament)),
            textAlign = TextAlign.Center
        )
        Spacer(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(1.dp)
                .offset(y = 3.dp)
                .background(DefaultTextColor)
        )
        Text(
            color = DefaultTextColor,
            text = stringResource(id = R.string.ornament_for_name_date_right),
            fontSize = 16.sp,
            fontFamily = FontFamily(Font(R.font.ornament)),
            textAlign = TextAlign.Center
        )
    }
}