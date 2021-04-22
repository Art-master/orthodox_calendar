package com.artmaster.android.orthodoxcalendar.domain

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import com.artmaster.android.orthodoxcalendar.domain.DbSchema.Holiday as Schema

@Parcelize
@Entity(tableName = Schema.TABLE_NAME)
data class Holiday(

        @PrimaryKey(autoGenerate = true)
        @SerializedName(Schema.ID)
        @ColumnInfo(name = Schema.ID)
        var id: Long = 0,

        @SerializedName(Schema.TITLE)
        @ColumnInfo(name = Schema.TITLE)
        var title: String = "",

        @SerializedName(Schema.DAY)
        @ColumnInfo(name = Schema.DAY)
        var day: Int = 0,

        @SerializedName(Schema.MONTH)
        @ColumnInfo(name = Schema.MONTH)
        var month: Int = 0,

        @SerializedName(Schema.TYPE_ID)
        @ColumnInfo(name = Schema.TYPE_ID)
        var typeId: Int = 0,

        @SerializedName(Schema.DYNAMIC_TYPE)
        @ColumnInfo(name = Schema.DYNAMIC_TYPE)
        var dynamicType: Int = 0,

        @SerializedName(Schema.IMAGE_ID)
        @ColumnInfo(name = Schema.IMAGE_ID)
        var imageId: String = "",

        @SerializedName(Schema.CREATED_BY_USER)
        @ColumnInfo(name = Schema.CREATED_BY_USER)
        var isCreatedByUser: Boolean = false,

        @SerializedName(Schema.YEAR)
        @ColumnInfo(name = Schema.YEAR)
        var year: Int = 0,

        @Ignore
        var description: String = "",

        @Ignore
        var monthWith0: Int = month - 1,

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

        fun Holiday.fillLocaleData(locale: Holiday) {
            title = locale.title
            description = locale.description
        }

        fun Holiday.mergeFullData(data: FullHolidayData): Holiday {
            description = data.description
            monthWith0 = month - 1
            return this
        }
    }
}
