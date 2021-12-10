package com.example.dailymoneyrecord.recorde_Book.domain.use_case.cases

import com.example.dailymoneyrecord.recorde_Book.domain.model.DebtorLoan
import com.example.dailymoneyrecord.recorde_Book.domain.repository.DebtorRepository

class DeleteLoan (private val repository: DebtorRepository){

    suspend operator fun invoke(debtorLoan: DebtorLoan){
        repository.deleteLona(debtorLoan)
    }
}