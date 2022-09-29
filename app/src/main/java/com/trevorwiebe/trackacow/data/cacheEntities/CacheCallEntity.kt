package com.trevorwiebe.trackacow.data.cacheEntities

import androidx.annotation.Keep
import androidx.room.PrimaryKey
import androidx.room.Entity

@Keep
@Entity(tableName = "holdingCall")
data class CacheCallEntity (
    @PrimaryKey(autoGenerate = true)
    var primaryKey: Int = 0,
    var callAmount: Int,
    var date: Long,
    var lotId: String,
    var id: String,
    var whatHappened: Int
)