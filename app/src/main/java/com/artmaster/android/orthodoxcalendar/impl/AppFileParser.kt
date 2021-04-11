package com.artmaster.android.orthodoxcalendar.impl

import com.artmaster.android.orthodoxcalendar.domain.Holiday

interface AppFileParser {
    fun getData(): List<Holiday>
}