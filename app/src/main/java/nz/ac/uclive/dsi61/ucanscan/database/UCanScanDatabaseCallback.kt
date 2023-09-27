package nz.ac.uclive.dsi61.ucanscan.database
import android.content.Context
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import nz.ac.uclive.dsi61.ucanscan.entities.Landmark


class UCanScanDatabaseCallback(private val context: Context) : RoomDatabase.Callback() {

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        CoroutineScope(Dispatchers.IO).launch {
            prefillLandmarks(context)
        }

    }

    private suspend fun prefillLandmarks(context: Context) {
        val landmarkDao = UCanScanDatabase.getDatabase(context)?.landmarkDao()
        // Perform your initial data insertion here using the provided database instance
        var landmark = Landmark("la", "la", "sdsds")
        landmarkDao?.insert(landmark)
    }

}
