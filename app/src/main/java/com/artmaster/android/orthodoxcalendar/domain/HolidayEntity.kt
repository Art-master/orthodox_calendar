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
        TWELVE_NOT_MOVABLE("великий двунадесятый неподвижный"),
        NOT_TWELVE_NOT_MOVABLE("великий недвунадесятый неподвижный"),
        AVERAGE("средний"),
        AVERAGE_POLYLEIC("средний полиелейный"),
        AVERAGE_PEPPY("средний бденный");
    }
}
