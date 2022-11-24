package com.artmaster.android.orthodoxcalendar.ui.theme

import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.ui.graphics.Color

val Error = Color(0xFFD32F2F)

val Background = Color(0xFFFBF5CF)
val TopBarColor = Color(0xFFFAE1B6)
val TabsBackground = Color(0xFFFAEBB6)

val CheckBoxCheckmarkColor = Color(0xFFD32F2F)
val CheckBoxCheckedColor = Color(0xFFC09831)

val Easter = Color(0XFFE53935)
val HeadHoliday = Color(0XFFFF6D00)
val TwelveHoliday = Color(0XFFDD6A66)
val FastingDay = Color(0XFFBDBDBD)
val UsualDay = Color(0XFFFFFFFF)
val DayOfSolidWeek = Color(0XFFF4EB9B)
val Birthday = Color(0XFFFF6D00)
val NameDay = Color(0XFFFF6D00)

val DefaultTextColor = Color(0XFF757575)
val HeadSymbolTextColor = Color(0XFFE53935)
val OldDateTextColor = Color(0xFF2196F3)
val TileBorderColor = DefaultTextColor

val HeaderTextColor = Color(0XFF757575)


val EditTextBackground = Background
val EditTextErrorBackground = Error.copy(alpha = 0.3f)
val EditTextIndicatorColor = Color(0xFFCCA94E)
val EditTextCursorColor = Color(0xFFC09831)
val DisabledTextColor = DefaultTextColor.copy(alpha = 0.5f)

val FloatingButtonColor = Color(0xFFFF6D00)
val FloatingButtonColorLight = Color(0xFFE9A877)
val FiltersContentColor = Color(0xFF4E453F)
val FiltersLabelColor = Color(0xFFD3CFCC)


val LinksColor = Color.Blue
val ShadowColor = Color(0XFF757575)

val CursorColor = Color(0xFFFF6D00)

val ButtonBackground = Color(0xFFCCA94E)
val DisabledButtonBackground = Color(0xFF9C9C9C)
val ButtonContent = Color(0xFFFFFFFF)
val DisabledButtonContent = Color(0xFFD1D1D1)

val SelectedItemColor = Color(0x7A757575)
val SnackBarBackground = Color(0xFF363636)

val customTextSelectionColors = TextSelectionColors(
    handleColor = CursorColor,
    backgroundColor = Background
)