package nz.ac.uclive.dsi61.ucanscan.repository
import nz.ac.uclive.dsi61.ucanscan.database.UCanScanDatabase
import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import nz.ac.uclive.dsi61.ucanscan.entity.Landmark

class UCanScanRepository(private val database: UCanScanDatabase) {
    private val landmarkDao = database.landmarkDao()
    private val preferencesDao = database.preferencesDao()
    private val timesDao = database.timesDao()

    val landmarks: Flow<List<Landmark>> = landmarkDao.getAll()
    val numLandmarks: Flow<Int> = landmarkDao.getCount()


    // Here we define methods to interact w our entities - so feel free to add more if we need :)
    // I recommend looking at the Ben tutorial + the official android developer documentation for this
    @WorkerThread
    suspend fun insert(landmark: Landmark) {
        landmarkDao.insert(landmark)
    }


    fun getPreferenceState(preferenceName: String): Flow<Boolean> =
        preferencesDao.getPreferenceState(preferenceName)

    @WorkerThread
    suspend fun updatePreference(preferenceName: String, newState: Boolean) {
        preferencesDao.updatePreference(preferenceName, newState)
    }

    fun getUserName(preferenceName: String): Flow<String> =
        preferencesDao.getPreference(preferenceName).map { preferences ->
            preferences.userName
        }


    @WorkerThread
    suspend fun updateUserName(preferenceName: String, newUserName: String) {
        preferencesDao.updateUserName(preferenceName, newUserName)
    }

}
