package nz.ac.uclive.dsi61.ucanscan.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date

@Entity(tableName = "times")
class Times (
    @ColumnInfo var dateAchieved: String = SimpleDateFormat("dd-mm-YYYY").format(Date()),
    @ColumnInfo var endTime: Long) {

    @PrimaryKey(autoGenerate = true) var id: Long = 0

}
