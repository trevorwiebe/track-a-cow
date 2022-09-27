package com.trevorwiebe.trackacow.domain.use_cases.ration_use_cases

import android.app.Application
import com.trevorwiebe.trackacow.data.mapper.toHoldingRationModel
import com.trevorwiebe.trackacow.domain.models.ration.RationModel
import com.trevorwiebe.trackacow.domain.repository.local.RationsRepository
import com.trevorwiebe.trackacow.domain.repository.remote.RationRepositoryRemote
import com.trevorwiebe.trackacow.domain.utils.Constants
import com.trevorwiebe.trackacow.domain.utils.Utility

class UpdateRationUC(
    private val rationsRepository: RationsRepository,
    private val rationsRepositoryRemote: RationRepositoryRemote,
    private val context: Application
) {
    suspend operator fun invoke(rationModel: RationModel){
        val isConnectionActive = Utility.haveNetworkConnection(context)

        if(isConnectionActive){
            rationsRepositoryRemote.insertRationRemote(rationModel)
        }else{
            rationsRepository.insertHoldingRation(rationModel.toHoldingRationModel(Constants.INSERT_UPDATE))
            Utility.setNewDataToUpload(context, true)
        }
        rationsRepository.updateRations(rationModel)
    }
}