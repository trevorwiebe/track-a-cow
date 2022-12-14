package com.trevorwiebe.trackacow.data.remote.repository

import com.google.firebase.database.FirebaseDatabase
import com.trevorwiebe.trackacow.domain.models.lot.LotModel
import com.trevorwiebe.trackacow.domain.repository.remote.LotRepositoryRemote
import com.trevorwiebe.trackacow.domain.use_cases.GetCloudDatabaseId

class LotRepositoryRemoteImpl(
    private val firebaseDatabase: FirebaseDatabase,
    private val databasePath: String
): LotRepositoryRemote {

    override fun updateLotWithNewPenIdRemote(lotId: String, penId: String) {
        firebaseDatabase.getReference("$databasePath/$lotId/lotPenId").setValue(penId)
    }

    override fun insertLotRemote(lotModel: LotModel) {
        if(lotModel.lotCloudDatabaseId.isNotEmpty()) {
            firebaseDatabase.getReference(
                "$databasePath/${lotModel.lotCloudDatabaseId}"
            ).setValue(lotModel)
        }
    }

}