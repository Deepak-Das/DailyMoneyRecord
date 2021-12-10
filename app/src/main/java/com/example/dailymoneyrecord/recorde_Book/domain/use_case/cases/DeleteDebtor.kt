package com.example.dailymoneyrecord.recorde_Book.domain.use_case.cases

import com.example.dailymoneyrecord.recorde_Book.domain.model.Debtor
import com.example.dailymoneyrecord.recorde_Book.domain.repository.DebtorRepository

data class DeleteDebtor(
    private val repository: DebtorRepository
){
    suspend operator fun invoke(debtor: Debtor){
        repository.deleteDebtor(debtor)
    }
}
