package com.artmaster.android.orthodoxcalendar.common

/**
 * Constants for settings app
 */
class Settings {
    companion object {
        const val EMPTY: String = "NONE"
        const val TRUE: String = "true"
        const val FALSE: String = "false"
    }

    enum class Name(val value: String, val defValue: String) {
        FIRST_LOAD_APP("first_load_app", FALSE),
        IS_ENABLE_NOTIFICATION_TIME("is_enable_notification_before", TRUE),
        IS_ENABLE_NOTIFICATION_IN_TIME("is_enable_notification_before", TRUE),
        IS_ENABLE_NOTIFICATION_TODAY("is_enable_notification_today", TRUE),
        TIME_OF_NOTIFICATION("time_of_notification", "1"),
        HOURS_OF_NOTIFICATION("time_of_notification", "13"),
        SOUND_OF_NOTIFICATION("sound_of_notification", TRUE),
        VIBRATION_OF_NOTIFICATION("vibration_of_notification", TRUE),
        AVERAGE_HOLIDAYS_NOTIFY_ALLOW("average_holidays_notification", TRUE)
    }
}


