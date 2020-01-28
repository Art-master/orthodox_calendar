package com.artmaster.android.orthodoxcalendar.domain

data class Fasting(
        var type:Type = Type.NONE,
        var description: String = "",
        var permissions: List<Permission> = emptyList()
){
    enum class Type{
        NONE, FASTING, FASTING_DAY, SOLID_WEEK
    }

    enum class Permission{
        FISH, OIL, VINE, STRICT, NO_EAT, CAVIAR, HOT_NO_OIL, NO_MEAT
    }
}