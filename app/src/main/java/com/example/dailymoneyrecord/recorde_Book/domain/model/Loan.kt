package com.example.dailymoneyrecord.recorde_Book.domain.model

import com.example.dailymoneyrecord.recorde_Book.domain.util.OrderBy

data class Loan(
    val loanId:Int,
    val debtorId:Int,
    val DebtorName:String,
    val LoneAmount:Int,
    val timeStamp: Long,
    val color:Int,
    val status:String
)
