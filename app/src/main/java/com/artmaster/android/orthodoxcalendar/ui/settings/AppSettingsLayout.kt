package com.artmaster.android.orthodoxcalendar.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import com.artmaster.android.orthodoxcalendar.ui.common.DividerWithText
import com.artmaster.android.orthodoxcalendar.ui.theme.HeadSymbolTextColor
import com.artmaster.android.orthodoxcalendar.ui.theme.HeaderTextColor
import com.artmaster.android.orthodoxcalendar.ui.tile_calendar.components.Spinner

@Preview(showBackground = true)
@Composable
fun PreviewSettingsLayout() {
    SettingsLayout()
}

@Composable
fun SettingsLayout(viewModel: SettingsViewModel = SettingsViewModel()) {
    val scroll = rememberScrollState(0)

    if (viewModel.isInit.value.not()) {
        Spinner()
        return
    }

    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(scroll)
    ) {
        Header()

        DividerWithText(text = stringResource(id = R.string.settings_notifications_header))
        CheckBoxSettings(
            setting = Settings.Name.IS_ENABLE_NOTIFICATION_TODAY,
            viewModel = viewModel,
            title = stringResource(id = R.string.settings_notifications)
        )
        CheckBoxSettings(
            setting = Settings.Name.AVERAGE_HOLIDAYS_NOTIFY_ALLOW,
            viewModel = viewModel,
            title = stringResource(id = R.string.settings_average_notifications)
        )
        CheckBoxSettings(
            setting = Settings.Name.MEMORY_DAYS_NOTIFY_ALLOW,
            viewModel = viewModel,
            title = stringResource(id = R.string.settings_memory_days_notifications)
        )
        CheckBoxSettings(
            setting = Settings.Name.BIRTHDAYS_NOTIFY_ALLOW,
            viewModel = viewModel,
            title = stringResource(id = R.string.settings_birthdays_notifications)
        )
        CheckBoxSettings(
            setting = Settings.Name.MEMORY_DAYS_NOTIFY_ALLOW,
            viewModel = viewModel,
            title = stringResource(id = R.string.settings_memory_days_notifications)
        )
        CheckBoxSettings(
            setting = Settings.Name.SOUND_OF_NOTIFICATION,
            viewModel = viewModel,
            title = stringResource(id = R.string.settings_notifications_sound)
        )
        CheckBoxSettings(
            setting = Settings.Name.STANDARD_SOUND,
            viewModel = viewModel,
            title = stringResource(id = R.string.settings_notifications_sound_default)
        )
        CheckBoxSettings(
            setting = Settings.Name.VIBRATION_OF_NOTIFICATION,
            viewModel = viewModel,
            title = stringResource(id = R.string.settings_notifications_vibration)
        )
        CheckBoxSettings(
            setting = Settings.Name.TIME_OF_NOTIFICATION,
            viewModel = viewModel,
            title = stringResource(id = R.string.settings_notifications_time)
        )
        CheckBoxSettings(
            setting = Settings.Name.HOURS_OF_NOTIFICATION,
            viewModel = viewModel,
            title = stringResource(id = R.string.settings_notifications_in_time)
        )

        DividerWithText(text = stringResource(id = R.string.settings_interface_header))

        CheckBoxSettings(
            setting = Settings.Name.FIRST_LOADING_TILE_CALENDAR,
            viewModel = viewModel,
            title = stringResource(id = R.string.settings_first_load_view)
        )
        CheckBoxSettings(
            setting = Settings.Name.SPEED_UP_START_ANIMATION,
            viewModel = viewModel,
            title = stringResource(id = R.string.settings_speed_up_start_anim)
        )
        CheckBoxSettings(
            setting = Settings.Name.OFF_START_ANIMATION,
            viewModel = viewModel,
            title = stringResource(id = R.string.settings_off_start_anim)
        )
    }
}

@Composable
fun CheckBoxSettings(
    setting: Settings.Name,
    title: String,
    viewModel: SettingsViewModel
) {
    val onCheck = remember {
        { flag: Boolean -> viewModel.setSetting(setting, flag.toString()) }
    }
    val state = viewModel.getSetting(setting)
    CheckBox(title = title, state = state?.value.toBoolean(), onCheck = onCheck)
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
            color = HeaderTextColor,
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