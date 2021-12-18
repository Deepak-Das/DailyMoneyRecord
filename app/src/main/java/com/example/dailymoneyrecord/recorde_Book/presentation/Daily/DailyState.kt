package com.example.dailymoneyrecord.recorde_Book.presentation.Daily

import com.example.dailymoneyrecord.recorde_Book.domain.model.DailyPayment
import com.example.dailymoneyrecord.recorde_Book.domain.model.Debtor
import com.example.dailymoneyrecord.recorde_Book.domain.model.DebtorLoan
import com.example.dailymoneyrecord.recorde_Book.domain.model.Loan
import com.example.dailymoneyrecord.recorde_Book.domain.util.OrderBy
import com.example.dailymoneyrecord.recorde_Book.domain.util.OrderType
import java.sql.Date
import java.text.SimpleDateFormat

data class DailyState(
    val payments: List<DailyPayment> = emptyList(),
    val loan_list:List<Loan> = emptyList(),
    val orderBy: OrderBy = OrderBy.Id(OrderType.Descending),
    var textName: String = "",
    var textAmount: String = "",
    var paymentId: Int? = null,
    var debtorId: Int? = null,
    var dateStamp: Long = SimpleDateFormat("yyyy-MM-dd").parse(Date(System.currentTimeMillis()).toString()).time,
    val debtors: List<Debtor> = emptyList(),
    val totalAmount: Int = 0,
    val debtorExist:Boolean=false
)
