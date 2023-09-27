package nz.ac.uclive.dsi61.ucanscan.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "landmark")
class Landmark (
    @ColumnInfo var name: String,
    @ColumnInfo var description: String,
    @ColumnInfo var code: String) {

    @PrimaryKey(autoGenerate = true) var id: Long = 0

    override fun toString() = name
}