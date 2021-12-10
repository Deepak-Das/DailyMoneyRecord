package com.example.dailymoneyrecord.recorde_Book.domain.repository

import com.example.dailymoneyrecord.recorde_Book.domain.model.Loan
import com.example.dailymoneyrecord.recorde_Book.domain.model.*
import kotlinx.coroutines.flow.Flow

interface DebtorRepository {

    //Insert fun

    suspend fun insertDebtor(debtor: Debtor)
    suspend fun insertLone(debtorLoan: DebtorLoan)
    suspend fun insertPayment(debtorPayment: DebtorPayment)

    //Delete
    suspend fun deleteDebtor(debtor: Debtor)
    suspend fun deletePayment(debtorPayment: DebtorPayment)
    suspend fun deleteLona(debtorLoan: DebtorLoan)

    //Query
    suspend fun getDebtorWithPayments(): List<DebtorWithPayments>?
    suspend fun getDebtorWithLone(): List<DebtorWithLoan>
    fun getAllDebtor(): Flow<List<Debtor>>
    fun getAllDebtorLoans(): Flow<List<Loan>>

    fun getPayments(timestamp: Long, debtorId: Int): Flow<List<DebtorPayment>>?
    fun getPaymentsById(id: Int, limit_st: Int, limit_ed: Int): Flow<List<DailyPayment>>
    fun getDailyPayments(timestamp: Long):Flow<List<DailyPayment>>
    fun getPaymentsByIdAndTimestamp(id: Int,timestamp: Long):Flow<List<DailyPayment>>

    fun getLoansById(id:Int):Flow<List<Loan>>

    fun getAllPayments():Flow<List<DailyPayment>>
}