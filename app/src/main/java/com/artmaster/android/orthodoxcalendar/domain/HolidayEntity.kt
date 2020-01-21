package com.artmaster.android.orthodoxcalendar.domain

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
@Entity(tableName = "holidays")
data class HolidayEntity(

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

        @SerializedName("type")
        var type: String = "",

        @SerializedName("description")
        var description: String = "",

        @SerializedName("image")
        @ColumnInfo(name = "image_link")
        var imageLink: String = "",

        @Ignore
        var firstInGroup: Boolean = false

) : Comparable<HolidayEntity>, Parcelable {

    override fun compareTo(other: HolidayEntity): Int {
        val monthComparison = month.compareTo(other.month)
        if (monthComparison == 0) {
            return day.compareTo(other.day)
        }
        return monthComparison
    }

    enum class Type(val value: String){
        GREAT("великий"),
        TWELVE("двунадесятый"),
        MAIN("главный"),
        HEAD("главный переходящий"),
        TWELVE_MOVABLE("великий двунадесятый переходящий"),
        GREAT_TWELVE("великий двунадесятый"),
        TWELVE_NOT_MOVABLE("великий двунадесятый неподвижный"),
        NOT_TWELVE_NOT_MOVABLE("великий недвунадесятый неподвижный"),
        GREAT_NOT_TWELVE("великий недвунадесятый"),
        AVERAGE("средний"),
        AVERAGE_POLYLEIC("средний полиелейный"),
        AVERAGE_PEPPY("средний бденный");
    }
    enum class DayOfWeek(val num: Int){
        MONDAY(1),
        TUESDAY(2),
        WEDNESDAY(3),
        THURSDAY(4),
        FRIDAY(5),
        SATURDAY(6),
        SUNDAY(7);
    }

    enum class Month(val num: Int){
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
}
