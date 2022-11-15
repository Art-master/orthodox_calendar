package com.artmaster.android.orthodoxcalendar.ui.alerts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.artmaster.android.orthodoxcalendar.R
import com.artmaster.android.orthodoxcalendar.ui.common.StyledButton
import com.artmaster.android.orthodoxcalendar.ui.common.StyledText

@Composable
@Preview(showBackground = true, device = Devices.PIXEL_3)
fun DeleteHolidayDialogPreview() {
    DeleteHolidayDialog(true)
}

@Composable
fun DeleteHolidayDialog(
    state: Boolean = false,
    onRejectClick: () -> Unit = {},
    onConfirmClick: () -> Unit = {}
) {
    var openDialog by remember(state) { mutableStateOf(state) }
    if (openDialog) {
        AlertDialog(
            onDismissRequest = {
                openDialog = false
                onRejectClick()
            },
            title = { StyledText(title = stringResource(id = R.string.holidayConfirmationHeader)) },
            text = { StyledText(title = stringResource(id = R.string.holidayConfirmationMsg)) },
            buttons = {
                Row(
                    modifier = Modifier.padding(all = 8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    StyledButton(
                        title = stringResource(id = R.string.buttonReject),
                        modifier = Modifier.weight(1f),
                        onClick = {
                            openDialog = false
                            onRejectClick()
                        }
                    )
                    StyledButton(
                        title = stringResource(id = R.string.buttonConfirm),
                        modifier = Modifier.weight(1f),
                        onClick = {
                            openDialog = false
                            onConfirmClick()
                        }
                    )
                }
            }
        )
    }
}