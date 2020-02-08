package com.artmaster.android.orthodoxcalendar.domain

data class Day (
        var year: Int = 0,
        var month: Int = 0,
        var dayOfMonth: Int = 0,
        var dayInWeek: Int = 0,
        var holidays: ArrayList<HolidayEntity> = ArrayList(),
        var fasting: Fasting = Fasting(),
        var memorialType: MemorialType = MemorialType.NONE
){
    enum class MemorialType() {
        NONE,
        MEATLESS_SATURDAY,
        SATURDAY_OF_TRINITY,
        SATURDAY_OF_PARENT_2,
        SATURDAY_OF_PARENT_3,
        SATURDAY_OF_PARENT_4,
        SATURDAY_OF_PARENT_TRINITY,
        SATURDAY_OF_PARENT_CHRISTMAS,
        SATURDAY_OF_PARENT_ASSUMPTION,
        SATURDAY_OF_DMITRY,
        RADUNYTSYA,
    }
}