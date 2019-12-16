package com.artmaster.android.orthodoxcalendar.common

/**
 * Constants for settings app
 */
class Settings {
    companion object {
        const val EMPTY: String = "NONE"
        const val TRUE: String = "1"
        const val FALSE: String = "0"
    }

    enum class Name(val value: String) {
        FIRST_LOAD_APP("first_load_app"),
        IS_ENABLE_NOTIFICATION_BEFORE("is_enable_notification_before"),
        IS_ENABLE_NOTIFICATION_TODAY("is_enable_notification_today"),
        TIME_OF_NOTIFICATION("time_of_notification"),
    }
}


