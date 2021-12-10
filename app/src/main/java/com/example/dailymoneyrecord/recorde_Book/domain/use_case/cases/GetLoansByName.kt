package com.example.dailymoneyrecord.recorde_Book.domain.use_case.cases

import com.example.dailymoneyrecord.recorde_Book.domain.model.Loan
import com.example.dailymoneyrecord.recorde_Book.domain.repository.DebtorRepository
import com.example.dailymoneyrecord.recorde_Book.domain.util.OrderType
import com.example.dailymoneyrecord.recorde_Book.domain.util.Status
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetLoansByName(private val repository: DebtorRepository) {

    operator fun invoke(id:Int,status:Status):Flow<List<Loan>> {
        return repository.getLoansById(id).map {
                when (status.orderType) {
                    is OrderType.Ascending -> {
                        when (status) {
                            is Status.Running -> it.filter { it.status == "Running"}.sortedBy { it.timeStamp }
                            is Status.Paid -> it.filter { it.status == "Paid" }.sortedBy { it.timeStamp }
                            is Status.All -> it.sortedBy { it.timeStamp }

                        }
                    }
                    is OrderType.Descending -> {
                        when (status) {
                            is Status.Running -> it.filter { it.status == "Running"}.sortedByDescending { it.timeStamp }
                            is Status.Paid -> it.filter { it.status == "Paid" }.sortedByDescending { it.timeStamp }
                            is Status.All -> it.sortedByDescending { it.timeStamp }

                        }
                    }
                }

            }
        }
    }
