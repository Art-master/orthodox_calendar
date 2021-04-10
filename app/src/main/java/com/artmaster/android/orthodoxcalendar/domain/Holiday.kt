package com.artmaster.android.orthodoxcalendar.domain

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
@Entity(tableName = "holidays")
data class Holiday(

        @PrimaryKey(autoGenerate = true)
        var id: Long = 0,

        var uuid: String = UUID.randomUUID().toString(),

        @SerializedName("title")
        var title: String = "",

        @SerializedName("day")
        var day: Int = 0,

        @SerializedName("month")
        var month: Int = 0,

        @Ignore
        var monthWith0: Int = month - 1,

        @Ignore
        var year: Int = 0,

        @SerializedName("typeId")
        var typeId: Int = 0,

        @SerializedName("type")
        var type: String = "",

        @SerializedName("dynamic_type")
        var dynamicType: Int = 0,

        @SerializedName("description")
        var description: String = "",

        @SerializedName("imageId")
        @ColumnInfo(name = "image_link")
        var imageLink: String = "",

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
        AVERAGE_PEPPY("средний бденный", 7);
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
            main.type = locale.type
            main.description = locale.description
        }
    }
}
