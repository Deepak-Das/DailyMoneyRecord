package com.example.dailymoneyrecord.recorde_Book.domain.use_case.cases

import com.example.dailymoneyrecord.recorde_Book.domain.model.DailyPayment
import com.example.dailymoneyrecord.recorde_Book.domain.repository.DebtorRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetAllPayments(
    private val repository: DebtorRepository
) {

    operator fun invoke():Flow<List<DailyPayment>> {
        return repository.getAllPayments()
    }

}