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
    enum class MemorialType(name: String) {
        NONE("нет"),
        MEATLESS_SATURDAY("Мясопустная вселенская родительская суббота"),
        SATURDAY_OF_TRINITY("Троицкая вселенская родительская суббота"),
        SATURDAY_OF_PARENT_2("Родительская 2-я суббота Великого Поста"),
        SATURDAY_OF_PARENT_3("Родительская 3-я суббота Великого Поста"),
        SATURDAY_OF_PARENT_4("Родительская 4-я суббота Великого Поста")
    }
}