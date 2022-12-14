package com.trevorwiebe.trackacow.data.local.cacheDao

import androidx.room.*
import com.trevorwiebe.trackacow.data.cacheEntities.CacheCallEntity
import com.trevorwiebe.trackacow.data.entities.CallEntity

@Dao
interface CacheCallDao {
    @Insert
    fun insertHoldingCall(cacheCallEntity: CacheCallEntity)

    @Insert
    fun insertHoldingCallList(holdingCallEntities: List<CacheCallEntity>)

    @get:Query("SELECT * FROM holdingCall")
    val holdingCallEntities: List<CacheCallEntity>

    @Query("DELETE FROM holdingCall")
    fun deleteCallTable()

    @Update
    fun updateCallEntity(callEntity: CallEntity)

    @Delete
    fun deleteCallEntity(callEntity: CallEntity)
}