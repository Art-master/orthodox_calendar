package com.artmaster.android.orthodoxcalendar.domain

data class Day (
        var month: Int = 0,
        var dayInMonth: Int = 0,
        var dayInWeek: Int = 0,
        var holidays: ArrayList<HolidayEntity> = ArrayList(),
        var fasting: Fasting = Fasting(),
        var memorialType: MemorialType = MemorialType.NONE
){
    enum class MemorialType{
        NONE
    }
}