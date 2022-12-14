package com.trevorwiebe.trackacow.data.local.dao

import androidx.room.*
import com.trevorwiebe.trackacow.data.entities.LotEntity
import com.trevorwiebe.trackacow.domain.models.lot.LotModel
import kotlinx.coroutines.flow.Flow

@Dao
interface LotDao {

    @Query("SELECT * FROM lot")
    fun getLotEntities(): Flow<List<LotEntity>>

    @Query("SELECT * FROM lot WHERE lotPrimaryKey = :lotPrimaryKey")
    fun getLotByLotId(lotPrimaryKey: Int): Flow<LotEntity?>

    @Query("SELECT * FROM lot WHERE lotPenCloudDatabaseId = :penId")
    fun getLotEntitiesByPenId(penId: String): Flow<List<LotEntity>>

    @Query("UPDATE lot SET lotPenCloudDatabaseId = :penId WHERE lotCloudDatabaseId = :lotId")
    suspend fun updateLotWithNewPenId(lotId: String, penId: String)

    @Query("UPDATE lot SET " +
            "lotName = :lotName, customerName = :customerName, notes = :notes, date = :date " +
            "WHERE lotPrimaryKey = :lotPrimaryKey")
    suspend fun updateLot(lotPrimaryKey: Int, lotName: String, customerName: String?, notes: String?, date: Long)

    // Deprecated
    @Deprecated("use suspend function")
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLot(lotEntity: LotEntity)

    @Deprecated("use suspend function")
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLotEntityList(lotEntities: List<LotEntity>)

    @Deprecated("use suspend function")
    @Query("DELETE FROM lot")
    fun deleteLotEntityTable()

    @Deprecated("use suspend function")
    @Query("DELETE FROM lot WHERE lotCloudDatabaseId = :lotId")
    fun deleteLotEntity(lotId: String)

    @Deprecated("use suspend function")
    @Update
    fun updateLotEntity(lotEntity: LotEntity)

    @Deprecated("use suspend function")
    @Query("UPDATE lot SET lotName = :lotName, customerName = :customerName, date = :date, notes = :notes WHERE lotCloudDatabaseId = :lotId")
    fun updateLotByFields(
        lotName: String?,
        customerName: String?,
        notes: String?,
        date: Long,
        lotId: String
    )

    @Deprecated("Use suspend function instead")
    @Query("UPDATE lot SET lotPenCloudDatabaseId = :penId WHERE lotCloudDatabaseId = :lotId")
    fun updateLotWithNewPenId2(lotId: String, penId: String)

    @Deprecated("Use flows return types")
    @Query("SELECT * FROM lot")
    fun getLotEntityList(): List<LotEntity>

    @Deprecated("Use getLotEntitiesByPenId with return type of Flow instead")
    @Query("SELECT * FROM lot WHERE lotPenCloudDatabaseId = :penId")
    fun getLotEntitiesByPenId2(penId: String): List<LotEntity>

    @Deprecated("Use flow return types")
    @Query("SELECT * FROM lot WHERE lotCloudDatabaseId = :lotId")
    fun getLotEntityById(lotId: String): LotEntity?
}