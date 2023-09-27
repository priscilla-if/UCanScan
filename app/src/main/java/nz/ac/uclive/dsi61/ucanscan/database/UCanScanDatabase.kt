package nz.ac.uclive.dsi61.ucanscan.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import nz.ac.uclive.dsi61.ucanscan.dao.LandmarkDao
import nz.ac.uclive.dsi61.ucanscan.dao.PreferencesDao
import nz.ac.uclive.dsi61.ucanscan.dao.TimesDao
import nz.ac.uclive.dsi61.ucanscan.entities.Landmark
import nz.ac.uclive.dsi61.ucanscan.entities.Times
import nz.ac.uclive.dsi61.ucanscan.entities.Preferences

@Database(entities = [Landmark::class, Preferences::class, Times::class], version = 2)
abstract class UCanScanDatabase : RoomDatabase() {
    abstract fun landmarkDao(): LandmarkDao
    abstract fun preferencesDao(): PreferencesDao
    abstract fun timesDao(): TimesDao

    companion object {
    // Singleton prevents multiple instances of database opening at the same time.
    @Volatile
    private var INSTANCE: UCanScanDatabase? = null

    fun getDatabase(context: Context): UCanScanDatabase {
        // if the INSTANCE is not null, then return it,
        // if it is, then create the database
        println(context)
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                UCanScanDatabase::class.java,
                "u_can_scan_database"
            ).addCallback(object : RoomDatabase.Callback() {

                override fun onCreate(db: SupportSQLiteDatabase) {

                    super.onCreate(db)

                    // Populate the database with initial data

                    // For example:

                    db.execSQL("INSERT INTO LANDMARK (name, description, code) VALUES ('yayaya', 'yayay', 'yayaya')")

                }

            }).build()
            INSTANCE = instance
            // return instance
            instance
        }
    }
}

}
