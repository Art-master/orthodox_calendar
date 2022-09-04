package com.artmaster.android.orthodoxcalendar.ui.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Checkbox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun CustomCheckBoxPreview() {
    Column(Modifier.fillMaxSize()) {
        CheckBox()
    }
}


@Composable
fun CheckBox() {

    Checkbox(checked = true, onCheckedChange = {})
}