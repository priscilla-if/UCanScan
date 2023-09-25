package nz.ac.uclive.dsi61.ucanscan.repository
import UCanScanDatabase
import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow
import nz.ac.uclive.dsi61.ucanscan.entities.Landmark

class UCanScanRepository(private val database: UCanScanDatabase) {
    private val landmarkDao = database.landmarkDao()
    private val preferencesDao = database.preferencesDao()
    private val timesDao = database.timesDao()

    // Here we define methods to interact w our entities - so feel free to add more if we need :)

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(landmark: Landmark) {
        landmarkDao.insert(landmark)
    }

    @WorkerThread
    suspend fun getAllLandmarks(landmark: Landmark): Flow<List<Landmark>> {
        return landmarkDao.getAll()
    }


}
