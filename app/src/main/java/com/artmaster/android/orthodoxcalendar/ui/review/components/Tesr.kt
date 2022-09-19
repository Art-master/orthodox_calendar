package com.artmaster.android.orthodoxcalendar.ui.review.components

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp

@Preview(showBackground = true, fontScale = 1.5F)
@Composable
fun SamplePreview3() {
    CustomText(200.dp)
}

@Composable
fun CustomText(y: Dp) {
    Layout(content = { Text(text = "Lorem Ipsum Lorem IpsumLorem IpsumLorem IpsumLorem IpsumLorem Ipsum Lorem Ipsum Lorem IpsumLorem IpsumLorem IpsumLorem IpsumLorem IpsumLorem Ipsum Lorem Ipsum Lorem Ipsum") }) { measurables, constraints ->
        val text = measurables[0].measure(constraints)
        layout(constraints.maxWidth, constraints.maxHeight) { //Change these per your needs
            text.placeRelative(IntOffset(y.value.toInt(), 0))
        }
    }
}