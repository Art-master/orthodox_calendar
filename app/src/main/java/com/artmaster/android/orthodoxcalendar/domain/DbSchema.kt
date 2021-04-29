package com.artmaster.android.orthodoxcalendar.domain

interface DbSchema {
    object Holiday {
        const val TABLE_NAME = "holidays"
        const val ID = "id"
        const val TITLE = "title"
        const val DAY = "day"
        const val MONTH = "month"
        const val YEAR = "year"
        const val TYPE_ID = "type_id"
        const val DYNAMIC_TYPE = "dynamic_type"

        const val IMAGE_ID = "image_id"
        const val CREATED_BY_USER = "created_by_user"
    }

    object FullHolidayData {
        const val TABLE_NAME = "full_holidays_data"
        const val ID = "id"
        const val HOLIDAY_ID = "holiday_id"
        const val DESCRIPTION = "description"
    }
}