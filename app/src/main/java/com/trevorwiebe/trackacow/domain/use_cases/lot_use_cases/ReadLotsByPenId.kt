package com.trevorwiebe.trackacow.domain.use_cases.lot_use_cases

import com.trevorwiebe.trackacow.domain.models.lot.LotModel
import com.trevorwiebe.trackacow.domain.repository.local.LotRepository
import kotlinx.coroutines.flow.Flow

data class ReadLotsByPenId(
    private var lotRepository: LotRepository
){
    operator fun invoke(penId: String): Flow<List<LotModel>>{
        return lotRepository.readLotsByPenId(penId)
    }
}
