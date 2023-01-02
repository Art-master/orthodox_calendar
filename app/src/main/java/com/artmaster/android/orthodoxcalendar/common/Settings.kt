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

    enum class Name(
        val value: String,
        val defValue: String,
        val type: Class<*> = Boolean::class.java
    ) {
        FIRST_LOAD_APP("$PROJECT_DIR.app_first_initialization_v1", FALSE),
        IS_ENABLE_NOTIFICATION_TIME("$PROJECT_DIR.is_enable_notification_before", TRUE),
        IS_ENABLE_NOTIFICATION_IN_TIME("$PROJECT_DIR.is_enable_notification_in_time", FALSE),
        IS_ENABLE_NOTIFICATION_TODAY("$PROJECT_DIR.is_enable_notification_today", TRUE),
        TIME_OF_NOTIFICATION("$PROJECT_DIR.time_of_notification", "1", Int::class.java),
        HOURS_OF_NOTIFICATION("$PROJECT_DIR.hours_of_notification", "13", Int::class.java),
        SOUND_OF_NOTIFICATION("$PROJECT_DIR.sound_of_notification", TRUE),
        VIBRATION_OF_NOTIFICATION("$PROJECT_DIR.vibration_of_notification", FALSE),
        AVERAGE_HOLIDAYS_NOTIFY_ALLOW("$PROJECT_DIR.average_holidays_notification", TRUE),
        NAME_DAYS_NOTIFY_ALLOW("$PROJECT_DIR.name_days_notification", TRUE),
        BIRTHDAYS_NOTIFY_ALLOW("$PROJECT_DIR.birthdays_notification", TRUE),
        MEMORY_DAYS_NOTIFY_ALLOW("$PROJECT_DIR.memory_days_notification", TRUE),
        FIRST_LOADING_TILE_CALENDAR("$PROJECT_DIR.first_load_tile_calendar", FALSE),
        STANDARD_SOUND("$PROJECT_DIR.standard_sound", FALSE),
        OFF_START_ANIMATION("$PROJECT_DIR.off_start_animation", FALSE),
        SPEED_UP_START_ANIMATION("$PROJECT_DIR.speed_off_start_animation", FALSE),
        USER_DATA_VERSION("$PROJECT_DIR.user_data_version", "0", Int::class.java),
        LAST_EXECUTED_NOTIFICATIONS_DAY(
            "$PROJECT_DIR.last_executed_notifications_day",
            "0",
            Int::class.java
        ),

        //FILTERS
        FILTER_EASTER_HOLIDAY("$PROJECT_DIR.filter_easter_holiday", FALSE),
        FILTER_HEAD_HOLIDAY("$PROJECT_DIR.filter_head_holidays", FALSE),
        FILTER_AVERAGE_HOLIDAYS("$PROJECT_DIR.filter_average_holidays", FALSE),
        FILTER_COMMON_MEMORY_DAYS("$PROJECT_DIR.filter_common_memory_days", FALSE),
        FILTER_MEMORY_DAYS("$PROJECT_DIR.filter_memory_days", FALSE),
        FILTER_NAME_DAYS("$PROJECT_DIR.filter_name_days", FALSE),
        BIRTHDAYS("$PROJECT_DIR.birthdays", FALSE),
    }
}


