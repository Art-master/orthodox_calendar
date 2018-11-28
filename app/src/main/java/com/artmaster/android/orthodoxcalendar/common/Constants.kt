package com.artmaster.android.orthodoxcalendar.common

class Constants {
    enum class HolidayType constructor(private val value: String) {
        HEAD("главный переходящий"),
        TWELVE_MOVABLE("великий двунадесятый переходящий"),
        TWELVE_NOT_MOVABLE("великий двунадесятый неподвижный"),
        NOT_TWELVE_NOT_MOVABLE("великий недвунадесятый неподвижный"),
        AVERAGE("средний"),
        AVERAGE_POLYLEIC("средний полиелейный"),
        AVERAGE_PEPPY("средний бденный");
    }

    enum class HolidayList constructor(val value: Int) {
        PAGE_SIZE(25)
    }

    companion object {
        const val INIT_ASSETS_FILE_NAME = "holidays.json"
        const val INIT_ASSETS_FILE_DIRECTORY = "files/"
        const val DATABASE_FILE_NAME = "holidays.db"
        const val FONT_ASSETS_DIRECTORY = "fonts/"
        const val SETTINGS_FILE_NAME = "calendar_settings"
    }
}

