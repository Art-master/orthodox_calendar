package com.artmaster.android.orthodoxcalendar.domain

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import com.artmaster.android.orthodoxcalendar.domain.DbSchema.FullHolidayData as Schema

@Parcelize
@Entity(tableName = Schema.TABLE_NAME,
        foreignKeys = [ForeignKey(
                entity = Holiday::class,
                parentColumns = [Schema.ID],
                childColumns = [Schema.HOLIDAY_ID],
                onDelete = CASCADE)])

data class FullHolidayData(

        @PrimaryKey(autoGenerate = true)
        @SerializedName(Schema.ID)
        @ColumnInfo(name = Schema.ID)
        var id: Long = 0,

        @SerializedName(Schema.HOLIDAY_ID)
        @ColumnInfo(name = Schema.HOLIDAY_ID)
        var holidayId: Long = 0,

        @SerializedName(Schema.DESCRIPTION)
        @ColumnInfo(name = Schema.DESCRIPTION)
        var description: String = "",

        ) : Parcelable {

    companion object {
        fun FullHolidayData.fill(holiday: Holiday): FullHolidayData {
            description = holiday.description
            holidayId = holiday.id
            return this
        }
    }
}
