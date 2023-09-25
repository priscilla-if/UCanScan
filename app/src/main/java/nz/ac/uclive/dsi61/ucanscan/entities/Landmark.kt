package nz.ac.uclive.dsi61.ucanscan.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "landmark")
class Landmark (
    @ColumnInfo var name: String,
    @ColumnInfo var description: String,
    @Embedded
    @ColumnInfo var coordinates: Location,
    @ColumnInfo var code: String) {

    @PrimaryKey(autoGenerate = true) var id: Long = 0

    override fun toString() = name
}

// Temporary as we don't know how we are storing location from Google Maps just yet.
class Location {
    var latitude: Double = 0.0
    var longitude: Double = 0.0
}