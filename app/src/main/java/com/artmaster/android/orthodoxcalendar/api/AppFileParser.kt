package com.artmaster.android.orthodoxcalendar.api

import com.artmaster.android.orthodoxcalendar.domain.Holiday

interface AppFileParser {
    fun getData(): List<Holiday>
}