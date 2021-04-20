package com.artmaster.android.orthodoxcalendar.domain

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = DbSchema.Holiday.TABLE_NAME)
data class Holiday(

        @PrimaryKey(autoGenerate = true)
        @SerializedName(DbSchema.Holiday.ID)
        @ColumnInfo(name = DbSchema.Holiday.ID)
        var id: Long = 0,

        @SerializedName(DbSchema.Holiday.TITLE)
        @ColumnInfo(name = DbSchema.Holiday.TITLE)
        var title: String = "",

        @SerializedName(DbSchema.Holiday.DAY)
        @ColumnInfo(name = DbSchema.Holiday.DAY)
        var day: Int = 0,

        @SerializedName(DbSchema.Holiday.MONTH)
        @ColumnInfo(name = DbSchema.Holiday.MONTH)
        var month: Int = 0,

        @SerializedName(DbSchema.Holiday.TYPE_ID)
        @ColumnInfo(name = DbSchema.Holiday.TYPE_ID)
        var typeId: Int = 0,

        @SerializedName(DbSchema.Holiday.DYNAMIC_TYPE)
        @ColumnInfo(name = DbSchema.Holiday.DYNAMIC_TYPE)
        var dynamicType: Int = 0,

        @SerializedName(DbSchema.Holiday.DESCRIPTION)
        @ColumnInfo(name = DbSchema.Holiday.DESCRIPTION)
        var description: String = "",

        @SerializedName(DbSchema.Holiday.IMAGE_ID)
        @ColumnInfo(name = DbSchema.Holiday.IMAGE_ID)
        var imageId: String = "",

        @SerializedName(DbSchema.Holiday.CREATED_BY_USER)
        @ColumnInfo(name = DbSchema.Holiday.CREATED_BY_USER)
        var isCreatedByUser: Boolean = false,

        @Ignore
        var monthWith0: Int = month - 1,

        @Ignore
        var year: Int = 0,

        @Ignore
        var firstInGroup: Boolean = false

) : Comparable<Holiday>, Parcelable {

    override fun compareTo(other: Holiday): Int {
        val monthComparison = month.compareTo(other.month)
        if (monthComparison == 0) {
            return day.compareTo(other.day)
        }
        return monthComparison
    }

    enum class Type(val value: String, val id: Int) {
        MAIN("главный", 1),
        TWELVE_MOVABLE("великий двунадесятый переходящий", 2),
        TWELVE_NOT_MOVABLE("великий двунадесятый неподвижный", 3),
        NOT_TWELVE_NOT_MOVABLE("великий недвунадесятый неподвижный", 4),
        GREAT_NOT_TWELVE("великий недвунадесятый переходящий", 5),
        AVERAGE_POLYLEIC("средний полиелейный", 6),
        AVERAGE_PEPPY("средний бденный", 7),

        USERS_NAME_DAY("именины", 8),
        USERS_BIRTHDAY("день рождения", 9),
        USERS_MEMORY_DAY("средний бденный", 10);
    }

    enum class DayOfWeek(val num: Int) {
        MONDAY(1),
        TUESDAY(2),
        WEDNESDAY(3),
        THURSDAY(4),
        FRIDAY(5),
        SATURDAY(6),
        SUNDAY(7);
    }

    enum class Month(val num: Int) {
        JANUARY(0),
        FEBRUARY(1),
        MARCH(2),
        APRIL(3),
        MAY(4),
        JUNE(5),
        JULY(6),
        AUGUST(7),
        SEPTEMBER(8),
        OCTOBER(9),
        NOVEMBER(10),
        DECEMBER(11);
    }

    enum class MovableDay(val dayFromEaster: Int, val dynamicType: Int) {
        //The Holy Easter
        THE_EASTER(0, 1),

        //The first sunday before Easter
        THE_ENTRY_OF_THE_LORD_INTO_JERUSALEM(-7, 2),

        //40 day after Easter
        THE_ASCENSION_OF_THE_LORD(39, 3),

        //50 day after Easter
        THE_HOLY_TRINITY(49, 4),
    }

    companion object {
        fun getTypeIdsByFilter(filter: Filter): List<Int> {
            if (filter == Filter.EASTER) {
                return listOf(Type.MAIN.id)
            }
            if (filter == Filter.HEAD_HOLIDAYS) {
                return listOf(Type.TWELVE_MOVABLE.id, Type.TWELVE_NOT_MOVABLE.id,
                        Type.GREAT_NOT_TWELVE.id, Type.NOT_TWELVE_NOT_MOVABLE.id)
            }
            if (filter == Filter.AVERAGE_HOLIDAYS) {
                return listOf(Type.AVERAGE_PEPPY.id, Type.AVERAGE_POLYLEIC.id)
            }
            //TODO other filters
            if (filter == Filter.COMMON_MEMORY_DAYS) {
                return listOf()
            }
            if (filter == Filter.MEMORY_DAYS) {
                return listOf()
            }
            if (filter == Filter.NAME_DAYS) {
                return listOf()
            }
            return emptyList()
        }

        fun fillLocaleData(main: Holiday, locale: Holiday) {
            main.title = locale.title
            main.description = locale.description
        }
    }
}
