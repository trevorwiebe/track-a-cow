package com.trevorwiebe.trackacow.data.cacheEntities

import androidx.annotation.Keep
import androidx.room.PrimaryKey
import androidx.room.Entity

@Keep
@Entity(tableName = "holdingLot")
class CacheLotEntity (
    @PrimaryKey(autoGenerate = true)
    var lotPrimaryKey: Int = 0,
    var lotName: String? = "",
    var lotCloudDatabaseId: String = "",
    var customerName: String? = "",
    var notes: String? = "",
    var date: Long = 0,
    var lotPenCloudDatabaseId: String? = "",
    var whatHappened: Int = 0
)