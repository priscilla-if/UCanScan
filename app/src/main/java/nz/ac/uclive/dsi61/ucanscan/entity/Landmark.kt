package nz.ac.uclive.dsi61.ucanscan.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "landmark")
class Landmark (
    @ColumnInfo var name: String,
    @ColumnInfo var description: String,
    @ColumnInfo var latitude: Double,
    @ColumnInfo var longitude: Double,
    @ColumnInfo var isFound: Boolean) {

    @PrimaryKey(autoGenerate = true) var id: Long = 0

    override fun toString() = name
}