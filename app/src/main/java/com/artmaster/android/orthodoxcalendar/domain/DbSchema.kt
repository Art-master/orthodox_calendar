package com.artmaster.android.orthodoxcalendar.domain

interface DbSchema {
    object Holiday {
        const val TABLE_NAME = "holidays"
        const val ID = "id"
        const val TITLE = "title"
        const val DAY = "day"
        const val MONTH = "month"
        const val TYPE_ID = "type_id"
        const val DYNAMIC_TYPE = "dynamic_type"
        const val DESCRIPTION = "description"
        const val IMAGE_ID = "image_id"
    }
}