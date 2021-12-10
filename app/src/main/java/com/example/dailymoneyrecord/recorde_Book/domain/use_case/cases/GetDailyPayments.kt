package com.example.dailymoneyrecord.recorde_Book.domain.use_case.cases

import com.example.dailymoneyrecord.recorde_Book.domain.model.DailyPayment
import com.example.dailymoneyrecord.recorde_Book.domain.repository.DebtorRepository
import com.example.dailymoneyrecord.recorde_Book.domain.util.OrderBy
import com.example.dailymoneyrecord.recorde_Book.domain.util.OrderType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetDailyPayments(
     private val repository: DebtorRepository
) {

     operator fun invoke(timeStamp: Long, orderBy: OrderBy): Flow<List<DailyPayment>> {
        return repository.getDailyPayments(timeStamp).map {
            when (orderBy.orderType) {
                is OrderType.Ascending->{
                    when(orderBy){
                        is OrderBy.Id-> it.sortedBy { it.paymentId }
                        is OrderBy.Name->it.sortedBy { it.debtorName.lowercase() }
                        is OrderBy.TimeStamp->it.sortedBy { it.timeStamp }
                    }
                }
                is OrderType.Descending->{
                    when(orderBy){
                        is OrderBy.Id-> it.sortedByDescending { it.paymentId }
                        is OrderBy.Name->it.sortedByDescending { it.debtorName.lowercase() }
                        is OrderBy.TimeStamp->it.sortedByDescending { it.timeStamp }
                    }
                }
            }

        }
    }
}