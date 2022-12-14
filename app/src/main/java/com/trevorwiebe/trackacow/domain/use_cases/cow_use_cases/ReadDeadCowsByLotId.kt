package com.trevorwiebe.trackacow.domain.use_cases.cow_use_cases

import com.trevorwiebe.trackacow.domain.models.cow.CowModel
import com.trevorwiebe.trackacow.domain.repository.local.CowRepository
import kotlinx.coroutines.flow.Flow

data class ReadDeadCowsByLotId(
    private val cowRepository: CowRepository
){
    operator fun invoke(lotId: String): Flow<List<CowModel>> {
        return cowRepository.getDeadCowsByLotId(lotId)
    }
}
