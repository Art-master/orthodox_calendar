package com.artmaster.android.orthodoxcalendar.common

/**
 * Description a system messages for users
 */
interface Message {
    enum class ERROR {
        INIT_DATABASE
    }

    enum class WARNING

    enum class INFO

    companion object {
        const val TYPE = "calendar type"
        const val EMPTY = ""
    }
}