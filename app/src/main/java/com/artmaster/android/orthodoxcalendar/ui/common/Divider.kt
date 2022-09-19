package com.artmaster.android.orthodoxcalendar.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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


@Composable
@Preview(showBackground = true)
fun DividerPreview() {
    Row(modifier = Modifier.fillMaxSize()) {
        DividerWithText(text = "Уведомления")
    }
}

@Composable
fun DividerWithText(text: String) {
    Box(contentAlignment = Alignment.Center) {
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
        Text(
            modifier = Modifier
                .background(Background)
                .padding(start = 3.dp, end = 3.dp)
                .offset(y = 6.dp),
            color = DefaultTextColor,
            text = text,
            fontSize = 16.sp,
            fontFamily = FontFamily(Font(R.font.cyrillic_old)),
            textAlign = TextAlign.Center
        )
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