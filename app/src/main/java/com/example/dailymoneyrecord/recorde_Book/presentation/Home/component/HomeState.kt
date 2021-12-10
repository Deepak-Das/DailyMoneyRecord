package com.example.dailymoneyrecord.recorde_Book.presentation.Home.component

import com.example.dailymoneyrecord.recorde_Book.domain.model.DailyPayment
import com.example.dailymoneyrecord.recorde_Book.domain.model.Loan

data class HomeState(
    val loanList: MutableList<List<String>> = mutableListOf(),
    val paysList: MutableList<List<String>> = mutableListOf(),
    val listloans: List<Loan> = emptyList(),
    val listpay:List<DailyPayment> = emptyList()
)
