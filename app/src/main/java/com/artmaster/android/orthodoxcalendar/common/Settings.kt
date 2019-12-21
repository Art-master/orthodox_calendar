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

    enum class Name(val value: String) {
        FIRST_LOAD_APP("first_load_app"),
        IS_ENABLE_NOTIFICATION_TIME("is_enable_notification_before"),
        IS_ENABLE_NOTIFICATION_TODAY("is_enable_notification_today"),
        TIME_OF_NOTIFICATION("time_of_notification"),
        SOUND_OF_NOTIFICATION("sound_of_notification"),
        VIBRATION_OF_NOTIFICATION("vibration_of_notification"),
        AVERAGE_HOLIDAYS_NOTIFY_ALLOW("average_holidays_notification")
    }
}


