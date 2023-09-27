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

}