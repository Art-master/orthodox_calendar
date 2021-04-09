package com.artmaster.android.orthodoxcalendar.common

import com.artmaster.android.orthodoxcalendar.common.Constants.Companion.PROJECT_DIR

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
        FIRST_LOAD_APP("$PROJECT_DIR.first_load_app", FALSE),
        IS_ENABLE_NOTIFICATION_TIME("$PROJECT_DIR.is_enable_notification_before", TRUE),
        IS_ENABLE_NOTIFICATION_IN_TIME("$PROJECT_DIR.is_enable_notification_in_time", FALSE),
        IS_ENABLE_NOTIFICATION_TODAY("$PROJECT_DIR.is_enable_notification_today", TRUE),
        TIME_OF_NOTIFICATION("$PROJECT_DIR.time_of_notification", "1"),
        HOURS_OF_NOTIFICATION("$PROJECT_DIR.hours_of_notification", "13"),
        SOUND_OF_NOTIFICATION("$PROJECT_DIR.sound_of_notification", TRUE),
        VIBRATION_OF_NOTIFICATION("$PROJECT_DIR.vibration_of_notification", FALSE),
        AVERAGE_HOLIDAYS_NOTIFY_ALLOW("$PROJECT_DIR.average_holidays_notification", TRUE),
        FIRST_LOADING_TILE_CALENDAR("$PROJECT_DIR.first_load_tile_calendar", FALSE),
        STANDARD_SOUND("$PROJECT_DIR.standard_sound", FALSE),
        OFF_START_ANIMATION("$PROJECT_DIR.off_start_animation", FALSE),
        SPEED_UP_START_ANIMATION("$PROJECT_DIR.speed_off_start_animation", FALSE),

        //FILTERS
        FILTER_AVERAGE_HOLIDAYS("$PROJECT_DIR.filter_average_holidays", FALSE),
    }
}


