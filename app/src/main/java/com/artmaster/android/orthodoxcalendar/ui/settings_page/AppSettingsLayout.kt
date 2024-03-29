package com.artmaster.android.orthodoxcalendar.ui.settings_page

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.core.text.isDigitsOnly
import com.artmaster.android.orthodoxcalendar.R
import com.artmaster.android.orthodoxcalendar.common.Settings
import com.artmaster.android.orthodoxcalendar.ui.common.CheckBox
import com.artmaster.android.orthodoxcalendar.ui.common.DividerWithText
import com.artmaster.android.orthodoxcalendar.ui.common.Header
import com.artmaster.android.orthodoxcalendar.ui.theme.CursorColor
import com.artmaster.android.orthodoxcalendar.ui.theme.DefaultTextColor
import com.artmaster.android.orthodoxcalendar.ui.theme.DisabledTextColor
import com.artmaster.android.orthodoxcalendar.ui.theme.EditTextBackground
import com.artmaster.android.orthodoxcalendar.ui.theme.EditTextCursorColor
import com.artmaster.android.orthodoxcalendar.ui.theme.EditTextIndicatorColor
import com.artmaster.android.orthodoxcalendar.ui.theme.HeadSymbolTextColor
import com.artmaster.android.orthodoxcalendar.ui.tile_calendar_page.components.Spinner
import com.artmaster.android.orthodoxcalendar.ui.viewmodel.ISettingsViewModel
import com.artmaster.android.orthodoxcalendar.ui.viewmodel.SettingsViewModel
import com.artmaster.android.orthodoxcalendar.ui.viewmodel.SettingsViewModelFake

@Preview(showBackground = true)
@Composable
fun PreviewSettingsLayout() {
    SettingsLayoutWrapper(viewModel = SettingsViewModelFake())
}

@Composable
fun SettingsLayoutWrapper(viewModel: ISettingsViewModel = SettingsViewModel()) {

    val onSettingChange = remember {
        { setting: Settings.Name, value: String -> viewModel.setSetting(setting, value) }
    }

    val getSettingValue = remember {
        { setting: Settings.Name -> viewModel.getSetting(setting).value }
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
            setting = Settings.Name.NAME_DAYS_NOTIFY_ALLOW,
            onSettingChange = onSettingChange,
            getSettingValue = getSettingValue,
            title = stringResource(id = R.string.settings_name_days_notifications)
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
        CheckBoxSettingsWithEditBox(
            setting = Settings.Name.IS_ENABLE_NOTIFICATION_TIME,
            linkedSetting = Settings.Name.TIME_OF_NOTIFICATION,
            onSettingChange = onSettingChange,
            getSettingValue = getSettingValue,
            title = stringResource(id = R.string.settings_notifications_time),
            postfix = stringResource(id = R.string.settings_days)
        )
        CheckBoxSettingsWithEditBox(
            setting = Settings.Name.FASTING_NOTIFY_ALLOW,
            linkedSetting = Settings.Name.TIME_OF_FASTING_NOTIFICATION_IN_DAYS,
            onSettingChange = onSettingChange,
            getSettingValue = getSettingValue,
            title = stringResource(id = R.string.settings_fasting_notify_allow),
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
        CheckBoxSettings(
            setting = Settings.Name.HIDE_TOOL_PANEL,
            onSettingChange = onSettingChange,
            getSettingValue = getSettingValue,
            title = stringResource(id = R.string.settings_hide_tool_panel)
        )
        CheckBoxSettings(
            setting = Settings.Name.TOOL_PANEL_MOVE_TO_LEFT,
            onSettingChange = onSettingChange,
            getSettingValue = getSettingValue,
            title = stringResource(id = R.string.settings_tool_panel_move_to_left)
        )
        CheckBoxSettings(
            setting = Settings.Name.HIDE_HORIZONTAL_YEARS_TAB,
            onSettingChange = onSettingChange,
            getSettingValue = getSettingValue,
            title = stringResource(id = R.string.settings_hide_horizontal_years_tab)
        )
        CheckBoxSettings(
            setting = Settings.Name.HIDE_HORIZONTAL_MONTHS_TAB,
            onSettingChange = onSettingChange,
            getSettingValue = getSettingValue,
            title = stringResource(id = R.string.settings_hide_horizontal_months_tab)
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

const val MAX_VALUE_LENGTH = 2

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
                val str = it.trim()
                isError =
                    if (str.isEmpty() || !str.isDigitsOnly() || str.toInt() !in 1 until maxNumber) {
                        onSettingChange(linkedSetting, linkedSetting.defValue)
                        true
                    } else {
                        onSettingChange(linkedSetting, str)
                        false
                    }
                if (str.length <= MAX_VALUE_LENGTH) text = str
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
                leadingIconColor = CursorColor,
                trailingIconColor = CursorColor,
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