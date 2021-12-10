package com.example.dailymoneyrecord.recorde_Book.presentation.Debtor

import com.example.dailymoneyrecord.recorde_Book.domain.model.Debtor
import com.example.dailymoneyrecord.recorde_Book.domain.util.OrderBy
import com.example.dailymoneyrecord.recorde_Book.domain.util.OrderType

data class DebtorState(
    val debtors: List<Debtor> = emptyList(),
    val orderBy: OrderBy=OrderBy.Name(OrderType.Ascending),
    val textName:String="",
    var TextPhone:Long?=null,
//    var isExpandCalendar:Boolean=false
)
