package com.example.dailymoneyrecord.recorde_Book.presentation.Lone

import com.example.dailymoneyrecord.recorde_Book.domain.model.Debtor
import com.example.dailymoneyrecord.recorde_Book.domain.model.Loan
import com.example.dailymoneyrecord.recorde_Book.domain.util.OrderType
import com.example.dailymoneyrecord.recorde_Book.domain.util.Status
import java.sql.Date
import java.text.SimpleDateFormat

data class LoneState(
    val status: Status = Status.Running(OrderType.Descending),
    val loans: List<Loan> = emptyList(),
    val debtors: List<Debtor> = emptyList(),
    var flagChange: Boolean = true,
    val id: Int? = null,
    val totalLoan: Int = -1,
    var dateStamp: Long = SimpleDateFormat("yyyy-MM-dd").parse(Date(System.currentTimeMillis()).toString()).time,
)
