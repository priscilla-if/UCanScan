package nz.ac.uclive.dsi61.ucanscan.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "preferences")
class Preferences (
    @ColumnInfo var name: String,
    @ColumnInfo var state: Boolean,
    @ColumnInfo var userName: String
    ) {

    @PrimaryKey(autoGenerate = true) var id: Long = 0

    override fun toString() = name
}
