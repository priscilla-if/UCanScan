import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import nz.ac.uclive.dsi61.ucanscan.dao.LandmarkDao
import nz.ac.uclive.dsi61.ucanscan.dao.PreferencesDao
import nz.ac.uclive.dsi61.ucanscan.dao.TimesDao
import nz.ac.uclive.dsi61.ucanscan.entities.Landmark
import nz.ac.uclive.dsi61.ucanscan.entities.Times
import nz.ac.uclive.dsi61.ucanscan.entities.Preferences

@Database(entities = [Landmark::class, Preferences::class, Times::class], version = 1)
abstract class UCanScanDatabase : RoomDatabase() {
    abstract fun landmarkDao(): LandmarkDao
    abstract fun preferencesDao(): PreferencesDao
    abstract fun timesDao(): TimesDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: UCanScanDatabase? = null

        fun getDatabase(context: Context): UCanScanDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UCanScanDatabase::class.java,
                    "u_can_scan_database"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}
