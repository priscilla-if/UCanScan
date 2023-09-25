package nz.ac.uclive.dsi61.ucanscan.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import nz.ac.uclive.dsi61.ucanscan.entities.Landmark

@Dao
interface LandmarkDao {

    @Insert
    suspend fun insert(landmark: Landmark): Long

    @Update
    suspend fun update(landmark: Landmark)

    @Delete
    suspend fun delete(landmark: Landmark)

    @Query("SELECT * FROM landmark")
    fun getAll(): Flow<List<Landmark>>

    @Query("SELECT COUNT(*) FROM landmark")
    fun getCount(): Flow<Int>


}