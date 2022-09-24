package com.trevorwiebe.trackacow.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.trevorwiebe.trackacow.data.local.entities.RationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRation(rationEntity: RationEntity)

    @Query("SELECT * FROM ration")
    fun getRations(): Flow<List<RationEntity>>

    @Query("SELECT * FROM ration WHERE primaryKey = :id")
    fun getRationsById(id: Int): Flow<RationEntity>

    @Update
    suspend fun updateRation(rationEntity: RationEntity)

    @Query("DELETE FROM ration WHERE primaryKey = :rationId")
    suspend fun deleteRationById(rationId: Int)

    @Query("DELETE FROM ration")
    suspend fun deleteRationTable()
}