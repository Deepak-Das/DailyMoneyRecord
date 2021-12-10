package com.example.dailymoneyrecord.recorde_Book.presentation.Daily

import com.example.dailymoneyrecord.recorde_Book.domain.model.DebtorLoan
import com.example.dailymoneyrecord.recorde_Book.domain.model.DebtorPayment
import com.example.dailymoneyrecord.recorde_Book.domain.util.OrderBy
import com.example.dailymoneyrecord.recorde_Book.domain.util.Status

sealed class Event() {
    class OrderChange(val orderBy: OrderBy) : Event()
    object onInsertPayment : Event()
    class onDelete(val payment: DebtorPayment) : Event()
    class DateChange(val date: Long) : Event()
    class Loan(val debtorLoan: DebtorLoan) : Event()
    class StatusFilter(val Status: Status) : Event()
    class LoneByName(val ID:Int?):Event()
    class AddLoan(val DebtorLoan:DebtorLoan):Event()
    class LoanDelete(val DebtorLoan:DebtorLoan):Event()
    class Limit(val id:Int, val start:Int, val end:Int):Event()
}
