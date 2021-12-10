package com.example.dailymoneyrecord.recorde_Book.domain.use_case.cases

import com.example.dailymoneyrecord.recorde_Book.domain.model.DebtorPayment
import com.example.dailymoneyrecord.recorde_Book.domain.repository.DebtorRepository

class DeletePayment(private val repository: DebtorRepository) {

    suspend operator fun invoke(payment: DebtorPayment) {
        repository.deletePayment(payment)
    }
}