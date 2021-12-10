package com.example.dailymoneyrecord.recorde_Book.domain.use_case.cases

import com.example.dailymoneyrecord.recorde_Book.domain.model.DebtorPayment
import com.example.dailymoneyrecord.recorde_Book.domain.repository.DebtorRepository

class InsertPayment(private val repository: DebtorRepository) {

    suspend operator fun invoke(payment:DebtorPayment){
        repository.insertPayment(payment)
    }
}