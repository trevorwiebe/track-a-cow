package com.trevorwiebe.trackacow.data.cacheEntities

import androidx.annotation.Keep
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import androidx.room.Entity

@Keep
@Entity(tableName = "HoldingDrug")
class CacheDrugEntity (
    @PrimaryKey(autoGenerate = true)
    var primaryKey: Int,
    var defaultAmount: Int,
    var drugId: String,
    var drugName: String,
    var whatHappened: Int
)