package com.artmaster.android.orthodoxcalendar.domain

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.SerializedName


@Entity(tableName = "holidays")
class HolidayEntity : Comparable<HolidayEntity> {

    @PrimaryKey(autoGenerate = true)
    var id = 0L

    var uuid = ""

    @SerializedName("title")
    var title = ""

    @SerializedName("day")
    var day = 0

    @SerializedName("month")
    var month = 0

    @Ignore
    var year = 0

    @SerializedName("type")
    var type = ""

    @SerializedName("description")
    var description = ""

    @SerializedName("image")
    @ColumnInfo(name = "image_link")
    var imageLink = ""

    @Ignore
    var firstInGroup = false

    override fun compareTo(other: HolidayEntity): Int {
        val monthComparison = month.compareTo(other.month)
        if (monthComparison == 0) {
            return day.compareTo(other.day)
        }
        return monthComparison
    }
}