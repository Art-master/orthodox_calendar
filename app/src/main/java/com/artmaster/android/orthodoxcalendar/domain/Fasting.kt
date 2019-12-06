package com.artmaster.android.orthodoxcalendar.domain

data class Fasting(
        var type:Type = Type.NONE,
        var description: String = "",
        var permission: Permission = Permission.ALL
){
    enum class Type{
        NONE, FASTING, FASTING_DAY
    }

    enum class Permission{
        FISH, MEAT, OIL, VINE, ALL
    }
}