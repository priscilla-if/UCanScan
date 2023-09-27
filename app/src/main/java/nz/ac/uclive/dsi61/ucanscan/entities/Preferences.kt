package nz.ac.uclive.dsi61.ucanscan.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "preferences")
class Preferences (
    @ColumnInfo var name: String,
    @ColumnInfo var state: Boolean
    ) {

    @PrimaryKey(autoGenerate = true) var id: Long = 0

    override fun toString() = name
}
