package nz.ac.uclive.dsi61.ucanscan.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import nz.ac.uclive.dsi61.ucanscan.entity.Preferences

@Dao
interface PreferencesDao {

    @Insert
    suspend fun insert(preferences: Preferences): Long

    @Update
    suspend fun update(preferences: Preferences)

    @Delete
    suspend fun delete(preferences: Preferences)

    @Query("SELECT * FROM preferences")
    fun getAll(): Flow<List<Preferences>>



    @Query("SELECT state FROM preferences WHERE name = :preferenceName")
    fun getPreferenceState(preferenceName: String): Flow<Boolean>

    @Query("UPDATE preferences SET state = :newState WHERE name = :preferenceName")
    suspend fun updatePreference(preferenceName: String, newState: Boolean)

}