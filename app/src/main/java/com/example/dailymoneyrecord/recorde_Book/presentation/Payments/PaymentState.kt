package com.example.dailymoneyrecord.recorde_Book.presentation.Payments

import com.example.dailymoneyrecord.recorde_Book.domain.model.DailyPayment
import com.example.dailymoneyrecord.recorde_Book.domain.model.Debtor
import com.example.dailymoneyrecord.recorde_Book.domain.util.OrderBy
import com.example.dailymoneyrecord.recorde_Book.domain.util.OrderType
import java.sql.Date
import java.text.SimpleDateFormat

data class PaymentState(
    val orderBy:OrderBy=  OrderBy.TimeStamp(OrderType.Descending),
    val debtorId:Int = -1,
    val loanDate:Long = SimpleDateFormat("yyyy-MM-dd").parse(Date(System.currentTimeMillis()).toString()).time,
    val paymentList:List<DailyPayment> = emptyList(),
    val totalAmount :Int=-1,
    val entries :Int?=null,
    val LIMIT_END:Int=60,
    val LIMIT: Int=0,
    val debtorList: List<Debtor> = emptyList(),
)