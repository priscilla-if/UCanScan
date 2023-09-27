package nz.ac.uclive.dsi61.ucanscan.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "times")
class Times (
    @ColumnInfo var startTime: String,
    @ColumnInfo var endTime: String) {

    @PrimaryKey(autoGenerate = true) var id: Long = 0

}
