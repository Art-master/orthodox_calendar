package com.artmaster.android.orthodoxcalendar.domain

data class Fasting(
    var type: Type = Type.NONE,
    var description: String = "",
    var permissions: List<Permission> = emptyList()
) {
    enum class Type(val resourceName: String) {
        NONE("no_fasting"),
        PETER_AND_PAUL_FASTING("peter_and_paul_fasting"),
        ASSUMPTION_FASTING("assumption_fasting"),
        CHRISTMAS_FASTING("christmas_fasting"),
        GREAT_FASTING("great_fasting"),
        FASTING_DAY("fasting_day"),
        SOLID_WEEK("solid_week")
    }

    enum class Permission {
        FISH, OIL, VINE, STRICT, NO_EAT, CAVIAR, HOT_NO_OIL, NO_MEAT
    }
}