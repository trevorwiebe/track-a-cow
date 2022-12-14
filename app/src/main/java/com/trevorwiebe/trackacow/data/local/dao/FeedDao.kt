package com.trevorwiebe.trackacow.data.local.dao

import androidx.room.*
import com.trevorwiebe.trackacow.data.entities.FeedEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FeedDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFeedEntity(feedEntity: FeedEntity)

    @Query("SELECT * FROM feed WHERE lotId = :lotId")
    fun getFeedsByLotId(lotId: String): Flow<List<FeedEntity>>

    @Update
    suspend fun updateFeedEntity(feedEntity: FeedEntity)

    @Delete
    suspend fun deleteFeedEntity(feedEntity: FeedEntity)


    /* Deprecated functions */

    @Deprecated("use function with return type: flow")
    @Query("SELECT * FROM feed")
    fun allFeedEntities(): List<FeedEntity>

    @Deprecated("use suspend function")
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFeedEntityList(feedEntities: List<FeedEntity>)

    @Deprecated("use function with return type: flow")
    @Query("SELECT * FROM feed WHERE lotId = :lotId")
    fun getFeedEntitiesByLotId(lotId: String): List<FeedEntity>

    @Deprecated("use function with return type: flow")
    @Query("SELECT * FROM feed WHERE lotId = :lotId AND date = :date")
    fun getFeedEntitiesByLotAndDate(lotId: String, date: Long): List<FeedEntity>

    @Deprecated("use suspend function")
    @Query("DELETE FROM feed WHERE date = :date AND lotId = :lotId")
    fun deleteFeedEntitiesByDateAndLotId(date: Long, lotId: String): Int

    @Deprecated("use suspend function")
    @Query("DELETE FROM feed WHERE id = :id")
    fun deleteFeedEntityById(id: String): Int

    @Deprecated("use suspend function")
    @Query("DELETE FROM feed")
    fun deleteFeedTable()
}