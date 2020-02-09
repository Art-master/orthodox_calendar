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
        const val LOADING_ANIMATION = 3000 //ms
        const val PLACEHOLDER_FOR_IMAGE = "image_holiday"
        const val RESOURCE_FOR_IMAGE = "drawable"
        const val VIEW_PAGER_SPEED = 800 //ms
        const val MONTH_SIZE = 12
        const val PROJECT_DIR = "com.artmaster.android.orthodoxcalendar"
    }

    enum class Keys constructor(val value: String) {
        HOLIDAY_ID("$PROJECT_DIR.common.holiday_id"),
        HOLIDAY("$PROJECT_DIR.common.holiday"),
        YEAR("$PROJECT_DIR.common.year"),
        MONTH("$PROJECT_DIR.common.month"),
        DAY("$PROJECT_DIR.common.day"),
        CURRENT_LIST_POSITION("$PROJECT_DIR.common.list.position"),
        INIT_LIST_POSITION("$PROJECT_DIR.common.list.position")
    }
}

