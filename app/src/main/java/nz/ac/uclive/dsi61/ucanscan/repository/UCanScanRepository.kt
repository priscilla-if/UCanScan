package nz.ac.uclive.dsi61.ucanscan.repository
import nz.ac.uclive.dsi61.ucanscan.database.UCanScanDatabase
import androidx.annotation.WorkerThread
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import nz.ac.uclive.dsi61.ucanscan.entity.Landmark
import nz.ac.uclive.dsi61.ucanscan.entity.Preferences

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


   /* fun getPreferenceByName(name: String): Boolean {

        var prefState : Boolean = true

        CoroutineScope(Dispatchers.IO).launch {

            prefState =  preferencesDao.getPreferenceByName(name)
        }

        return prefState

    }*/

    fun getPreferenceByName(name: String): Flow<Boolean> {
        return preferencesDao.getPreferenceByName(name)
    }

   /* @WorkerThread
    suspend fun updatePreference(name: String, newState: Boolean) {
        preferencesDao.update(Preferences(name, newState))
    }*/


    @WorkerThread
    suspend fun updatePreference(name: String, newState: Boolean) {
        withContext(Dispatchers.IO) {
            preferencesDao.update(Preferences(name, newState))
        }
    }

}
