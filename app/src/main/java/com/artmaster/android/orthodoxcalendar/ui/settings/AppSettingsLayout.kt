package com.artmaster.android.orthodoxcalendar.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.artmaster.android.orthodoxcalendar.R
import com.artmaster.android.orthodoxcalendar.common.Settings
import com.artmaster.android.orthodoxcalendar.ui.common.CheckBox
import com.artmaster.android.orthodoxcalendar.ui.common.DividerWithText
import com.artmaster.android.orthodoxcalendar.ui.common.Header
import com.artmaster.android.orthodoxcalendar.ui.theme.*
import com.artmaster.android.orthodoxcalendar.ui.tile_calendar.components.Spinner

@Preview(showBackground = true)
@Composable
fun PreviewSettingsLayout() {
    val onSettingChange = remember { { _: Settings.Name, _: String -> } }
    val getSettingValue = remember {
        { name: Settings.Name ->
            if (name.type == Int::class.java) "10" else "false"
        }
    }

    SettingsLayout(
        isInit = true,
        onSettingChange = onSettingChange,
        getSettingValue = getSettingValue
    )

}

@Composable
fun SettingsLayoutWrapper(viewModel: SettingsViewModel = SettingsViewModel()) {

    val onSettingChange = remember {
        { setting: Settings.Name, value: String -> viewModel.setSetting(setting, value) }
    }

    val getSettingValue = remember {
        { setting: Settings.Name -> viewModel.getSetting(setting)!!.value }
    }

    SettingsLayout(
        isInit = viewModel.isInit.value,
        onSettingChange = onSettingChange,
        getSettingValue = getSettingValue
    )
}

@Composable
fun SettingsLayout(
    isInit: Boolean,
    getSettingValue: (setting: Settings.Name) -> String,
    onSettingChange: (setting: Settings.Name, value: String) -> Unit
) {
    val scroll = rememberScrollState(0)

    if (isInit.not()) {
        Spinner()
        return
    }

    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(scroll)
    ) {
        Header(title = stringResource(id = R.string.settings_name))

        DividerWithText(text = stringResource(id = R.string.settings_notifications_header))
        CheckBoxSettings(
            setting = Settings.Name.IS_ENABLE_NOTIFICATION_TODAY,
            onSettingChange = onSettingChange,
            getSettingValue = getSettingValue,
            title = stringResource(id = R.string.settings_notifications)
        )
        CheckBoxSettings(
            setting = Settings.Name.AVERAGE_HOLIDAYS_NOTIFY_ALLOW,
            onSettingChange = onSettingChange,
            getSettingValue = getSettingValue,
            title = stringResource(id = R.string.settings_average_notifications)
        )
        CheckBoxSettings(
            setting = Settings.Name.MEMORY_DAYS_NOTIFY_ALLOW,
            onSettingChange = onSettingChange,
            getSettingValue = getSettingValue,
            title = stringResource(id = R.string.settings_memory_days_notifications)
        )
        CheckBoxSettings(
            setting = Settings.Name.BIRTHDAYS_NOTIFY_ALLOW,
            onSettingChange = onSettingChange,
            getSettingValue = getSettingValue,
            title = stringResource(id = R.string.settings_birthdays_notifications)
        )
        CheckBoxSettings(
            setting = Settings.Name.MEMORY_DAYS_NOTIFY_ALLOW,
            onSettingChange = onSettingChange,
            getSettingValue = getSettingValue,
            title = stringResource(id = R.string.settings_memory_days_notifications)
        )
        CheckBoxSettings(
            setting = Settings.Name.SOUND_OF_NOTIFICATION,
            onSettingChange = onSettingChange,
            getSettingValue = getSettingValue,
            title = stringResource(id = R.string.settings_notifications_sound)
        )
        CheckBoxSettings(
            setting = Settings.Name.STANDARD_SOUND,
            onSettingChange = onSettingChange,
            getSettingValue = getSettingValue,
            title = stringResource(id = R.string.settings_notifications_sound_default)
        )
        CheckBoxSettings(
            setting = Settings.Name.VIBRATION_OF_NOTIFICATION,
            onSettingChange = onSettingChange,
            getSettingValue = getSettingValue,
            title = stringResource(id = R.string.settings_notifications_vibration)
        )
        CheckBoxSettingsWithEditBox(
            setting = Settings.Name.IS_ENABLE_NOTIFICATION_TIME,
            linkedSetting = Settings.Name.TIME_OF_NOTIFICATION,
            onSettingChange = onSettingChange,
            getSettingValue = getSettingValue,
            title = stringResource(id = R.string.settings_notifications_time),
            postfix = stringResource(id = R.string.settings_days)
        )
        CheckBoxSettingsWithEditBox(
            setting = Settings.Name.IS_ENABLE_NOTIFICATION_IN_TIME,
            linkedSetting = Settings.Name.HOURS_OF_NOTIFICATION,
            onSettingChange = onSettingChange,
            getSettingValue = getSettingValue,
            title = stringResource(id = R.string.settings_notifications_in_time),
            postfix = stringResource(id = R.string.settings_hours)
        )

        DividerWithText(text = stringResource(id = R.string.settings_interface_header))

        CheckBoxSettings(
            setting = Settings.Name.FIRST_LOADING_TILE_CALENDAR,
            onSettingChange = onSettingChange,
            getSettingValue = getSettingValue,
            title = stringResource(id = R.string.settings_first_load_view)
        )
        CheckBoxSettings(
            setting = Settings.Name.SPEED_UP_START_ANIMATION,
            onSettingChange = onSettingChange,
            getSettingValue = getSettingValue,
            title = stringResource(id = R.string.settings_speed_up_start_anim)
        )
        CheckBoxSettings(
            setting = Settings.Name.OFF_START_ANIMATION,
            onSettingChange = onSettingChange,
            getSettingValue = getSettingValue,
            title = stringResource(id = R.string.settings_off_start_anim)
        )
    }
}

@Composable
fun CheckBoxSettings(
    setting: Settings.Name,
    title: String,
    getSettingValue: (setting: Settings.Name) -> String,
    onSettingChange: (setting: Settings.Name, value: String) -> Unit
) {
    val onCheck = remember {
        { flag: Boolean -> onSettingChange(setting, flag.toString()) }
    }
    val state = getSettingValue(setting)

    CheckBox(title = title, state = state.toBoolean(), onCheck = onCheck)
}

@Composable
fun CheckBoxSettingsWithEditBox(
    setting: Settings.Name,
    linkedSetting: Settings.Name,
    title: String,
    postfix: String,
    onSettingChange: (setting: Settings.Name, value: String) -> Unit,
    getSettingValue: (setting: Settings.Name) -> String
) {

    var flag by remember { mutableStateOf(getSettingValue(setting).toBoolean()) }
    var text by remember { mutableStateOf(getSettingValue(linkedSetting)) }

    val onCheck = remember {
        { value: Boolean ->
            flag = value
            onSettingChange(setting, value.toString())
        }
    }

    var isError by remember { mutableStateOf(false) }
    val maxNumber = 24

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 0.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CheckBox(
            modifier = Modifier.fillMaxWidth(0.7f),
            title = title,
            state = flag,
            onCheck = onCheck
        )
        TextField(
            modifier = Modifier.width(60.dp),
            value = text,
            enabled = flag,
            isError = isError,
            onValueChange = {
                isError = if (it.isEmpty() || it.toInt() !in 1 until maxNumber) {
                    onSettingChange(linkedSetting, linkedSetting.defValue)
                    true
                } else {
                    onSettingChange(linkedSetting, it)
                    false
                }
                text = it
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            textStyle = TextStyle(
                color = if (flag) DefaultTextColor else DisabledTextColor,
                fontFamily = FontFamily(Font(R.font.cyrillic_old)),
                fontWeight = FontWeight.Normal,
                fontSize = 18.sp
            ),
            colors = TextFieldDefaults.textFieldColors(
                textColor = DefaultTextColor,
                backgroundColor = EditTextBackground,
                focusedIndicatorColor = EditTextIndicatorColor,
                cursorColor = EditTextCursorColor,
                disabledTextColor = DisabledTextColor
            )
        )
        Text(
            modifier = Modifier.padding(start = 5.dp),
            text = postfix,
            fontSize = 20.sp,
            color = DefaultTextColor,
            fontFamily = FontFamily(Font(R.font.cyrillic_old)),
            textAlign = TextAlign.Left
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