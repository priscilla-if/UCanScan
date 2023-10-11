package nz.ac.uclive.dsi61.ucanscan.repository
import nz.ac.uclive.dsi61.ucanscan.database.UCanScanDatabase
import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import nz.ac.uclive.dsi61.ucanscan.entity.Landmark
import nz.ac.uclive.dsi61.ucanscan.entity.Times

class UCanScanRepository(private val database: UCanScanDatabase) {
    private val landmarkDao = database.landmarkDao()
    private val preferencesDao = database.preferencesDao()
    private val timesDao = database.timesDao()

    val landmarks: Flow<List<Landmark>> = landmarkDao.getAll()
    val numLandmarks: Flow<Int> = landmarkDao.getCount()


    val allTimes: Flow<List<Times>> = timesDao.getAll().map { timesList ->
        timesList.sortedBy { it.endTime }
    }


    // Here we define methods to interact w our entities - so feel free to add more if we need :)
    // I recommend looking at the Ben tutorial + the official android developer documentation for this
    @WorkerThread
    suspend fun insert(landmark: Landmark) {
        landmarkDao.insert(landmark)
    }


    @WorkerThread
    suspend fun addTime(time: Times) {
        timesDao.insert(time)
    }



}
