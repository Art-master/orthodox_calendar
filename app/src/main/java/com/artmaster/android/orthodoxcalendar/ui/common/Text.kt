package com.artmaster.android.orthodoxcalendar.ui.common

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.artmaster.android.orthodoxcalendar.R
import com.artmaster.android.orthodoxcalendar.ui.theme.DefaultTextColor

@Composable
fun CustomText(modifier: Modifier = Modifier, title: String) {
    Text(
        modifier = Modifier
            .padding(start = 5.dp)
            .then(modifier),
        text = title,
        fontSize = 20.sp,
        color = DefaultTextColor,
        fontFamily = FontFamily(Font(R.font.cyrillic_old)),
        textAlign = TextAlign.Left
    )
}