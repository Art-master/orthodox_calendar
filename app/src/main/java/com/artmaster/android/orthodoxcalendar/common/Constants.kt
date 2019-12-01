package com.artmaster.android.orthodoxcalendar.common

class Constants {

    enum class HolidayList constructor(val value: Int) {
        PAGE_SIZE(15)
    }

    companion object {
        const val INIT_ASSETS_FILE_NAME = "holidays.json5"
        const val INIT_ASSETS_FILE_DIRECTORY = "files/"
        const val DATABASE_FILE_NAME = "holidays.db"
        const val FONT_ASSETS_DIRECTORY = "fonts/"
        const val SETTINGS_FILE_NAME = "calendar_settings"
        const val LOADING_ANIMATION = 1000 //ms
        const val PLACEHOLDER_FOR_IMAGE = "image_holiday"
        const val RESOURCE_FOR_IMAGE = "drawable"
    }

    enum class Keys constructor(val value: String) {
        HOLIDAY_ID("com.artmaster.android.orthodoxcalendar.common.holiday_id"),
        HOLIDAY("com.artmaster.android.orthodoxcalendar.common.holiday"),
        YEAR("com.artmaster.android.orthodoxcalendar.common.year"),
        MONTH("com.artmaster.android.orthodoxcalendar.common.month"),
        DAY("com.artmaster.android.orthodoxcalendar.common.day"),
    }
}

