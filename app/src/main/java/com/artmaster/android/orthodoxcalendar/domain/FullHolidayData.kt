package com.artmaster.android.orthodoxcalendar.domain

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = DbSchema.FullHolidayData.TABLE_NAME)
data class FullHolidayData(

        @PrimaryKey(autoGenerate = true)
        @SerializedName(DbSchema.Holiday.ID)
        @ColumnInfo(name = DbSchema.Holiday.ID)
        var id: Long = 0,

        @Ignore
        var description: String = "",

        ) : Parcelable {

    companion object {
        fun FullHolidayData.fill(holiday: Holiday): FullHolidayData {
            description = holiday.description
            return this
        }
    }
}
