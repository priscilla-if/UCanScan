package nz.ac.uclive.dsi61.ucanscan.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "times")
class Times (
    @ColumnInfo var name: String,
    @ColumnInfo var startTime: String,
    @ColumnInfo var endTime: String) {

    @PrimaryKey(autoGenerate = true) var id: Long = 0
    override fun toString() = name

}
