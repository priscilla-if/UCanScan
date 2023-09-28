package nz.ac.uclive.dsi61.ucanscan.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import nz.ac.uclive.dsi61.ucanscan.entity.Times

@Dao
interface TimesDao {

    @Insert
    suspend fun insert(times: Times): Long

    @Update
    suspend fun update(times: Times)

    @Delete
    suspend fun delete(times: Times)

    @Query("SELECT * FROM times")
    fun getAll(): Flow<List<Times>>

    @Query("SELECT COUNT(*) FROM times")
    fun getCount(): Flow<Int>


}