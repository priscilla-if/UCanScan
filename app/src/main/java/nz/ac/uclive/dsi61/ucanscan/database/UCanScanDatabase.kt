package nz.ac.uclive.dsi61.ucanscan.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import nz.ac.uclive.dsi61.ucanscan.dao.LandmarkDao
import nz.ac.uclive.dsi61.ucanscan.dao.PreferencesDao
import nz.ac.uclive.dsi61.ucanscan.dao.TimesDao
import nz.ac.uclive.dsi61.ucanscan.entity.Landmark
import nz.ac.uclive.dsi61.ucanscan.entity.Preferences
import nz.ac.uclive.dsi61.ucanscan.entity.Times

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
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                UCanScanDatabase::class.java,
                "u_can_scan_database"
            ).addCallback(object : RoomDatabase.Callback() {

                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)

                    // Populate the database with initial data

                    // Landmarks
                    // * lat & long only need to be precise to 5dp because this is accurate within 1.1 metres
                    // * keep the description short so it doesn't get cut off :')
                    db.execSQL("INSERT INTO LANDMARK (name, description, latitude, longitude, isFound) VALUES ('Jack Erskine', 'The CSSE and Maths department building', -43.52256, 172.58119, true)")
                    db.execSQL("INSERT INTO LANDMARK (name, description, latitude, longitude, isFound) VALUES ('Elsie Locke', 'The Arts department building', -43.52456, 172.58351, true)")
                    db.execSQL("INSERT INTO LANDMARK (name, description, latitude, longitude, isFound) VALUES ('UC Rec Centre', 'The campus gym and recreation centre', -43.52698, 172.58451, true)")
                    db.execSQL("INSERT INTO LANDMARK (name, description, latitude, longitude, isFound) VALUES ('Ernest Rutherford', 'Home of the Science faculty', -43.52257, 172.58268, true)")
                    db.execSQL("INSERT INTO LANDMARK (name, description, latitude, longitude, isFound) VALUES ('Haere-roa', 'The hub of UC students and the UCSA', -43.52435, 172.58079, true)")
                    db.execSQL("INSERT INTO LANDMARK (name, description, latitude, longitude, isFound) VALUES ('Rehua', 'Centre for Education, Health and more', -43.52323, 172.58450, false)")

                    // Preferences
                    db.execSQL("INSERT INTO PREFERENCES (name, state, userName) VALUES ('notificationOption1', true, '')")
                    db.execSQL("INSERT INTO PREFERENCES (name, state, userName) VALUES ('notificationOption2', true, '')")
                    db.execSQL("INSERT INTO PREFERENCES (name, state, userName) VALUES ('notificationOption3', true, '')")
                    db.execSQL("INSERT INTO PREFERENCES (name, state, userName) VALUES ('themeOption1', true, '')")
                    db.execSQL("INSERT INTO PREFERENCES (name, state, userName) VALUES ('animationOption1', true, '')")
                    db.execSQL("INSERT INTO PREFERENCES (name, state, userName) VALUES ('animationOption2', true, '')")

                    db.execSQL("INSERT INTO PREFERENCES (name, state, userName) VALUES ('userName', true, '')")

                }

            }).build()
            INSTANCE = instance
            // return the instance
            instance
        }
    }
}

}
