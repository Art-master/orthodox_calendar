package com.artmaster.android.orthodoxcalendar.common

class Constants {

    enum class HolidayList constructor(val value: Int) {
        PAGE_SIZE(15)
    }

    companion object {
        const val INIT_ASSETS_FILE_NAME = "holidays.json5"
        const val INIT_ASSETS_FILE_DIRECTORY = "files/"
        const val DATABASE_FILE_NAME = "holidays.db"
        const val SETTINGS_FILE_NAME = "calendar_settings"
        const val LOADING_ANIMATION_DURATION = 3000 //ms
        const val LOADING_ANIMATION_SPEED_UP = LOADING_ANIMATION_DURATION / 2
        const val PLACEHOLDER_FOR_IMAGE = "image_holiday"
        const val RESOURCE_FOR_IMAGE = "drawable"
        const val VIEW_PAGER_SPEED = 800 //ms
        const val MONTH_COUNT = 12
        const val PROJECT_DIR = "com.artmaster.android.orthodoxcalendar"
        const val DATA_VERSION = 4
    }

    enum class ExtraData constructor(val value: String) {
        HOLIDAY_ID("$PROJECT_DIR.holiday_id"),
        HOLIDAY("$PROJECT_DIR.holiday")
    }

    enum class Action constructor(val value: String) {
        NOTIFICATION("$PROJECT_DIR.holiday.NOTIFICATION"),
        OPEN_HOLIDAY_PAGE("$PROJECT_DIR.OPEN_HOLIDAY")
    }
}

