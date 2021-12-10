package com.example.dailymoneyrecord.recorde_Book.domain.model

data class DailyPayment(
    val debtorName:String,
    val amount:Int,
    val paymentId:Int,
    val debtorId:Int,
    val timeStamp:Long,
    val color:Int
)