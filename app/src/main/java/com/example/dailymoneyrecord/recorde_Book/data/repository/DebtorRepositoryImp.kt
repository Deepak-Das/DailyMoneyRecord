package com.example.dailymoneyrecord.recorde_Book.data.repository

import com.example.dailymoneyrecord.recorde_Book.data.data_soruce.DebtorDoa
import com.example.dailymoneyrecord.recorde_Book.domain.model.Loan
import com.example.dailymoneyrecord.recorde_Book.domain.model.*
import com.example.dailymoneyrecord.recorde_Book.domain.repository.DebtorRepository
import kotlinx.coroutines.flow.Flow

class DebtorRepositoryImp(private val debtorDoa: DebtorDoa) : DebtorRepository {

    override suspend fun insertDebtor(debtor: Debtor) {
        debtorDoa.insertDebtor(debtor)
    }

    override suspend fun insertLone(debtorLoan: DebtorLoan) {
        debtorDoa.insertLone(debtorLoan)
    }

    override suspend fun insertPayment(debtorPayment: DebtorPayment) {
        debtorDoa.insertPayment(debtorPayment)
    }

    override suspend fun deleteDebtor(debtor: Debtor) {
        debtorDoa.deleteDebtor(debtor)
    }

    override suspend fun deletePayment(debtorPayment: DebtorPayment) {
        debtorDoa.deleteDebtorPayment(debtorPayment)
    }

    override suspend fun getDebtorWithPayments(): List<DebtorWithPayments>? {
        return debtorDoa.getDebtorWithPayments()
    }

    override suspend fun getDebtorWithLone(): List<DebtorWithLoan> {
        return debtorDoa.getDebtorWithLone()
    }

    override fun getAllDebtor(): Flow<List<Debtor>> {
        return debtorDoa.getAllDebtor()
    }

    override fun getAllDebtorLoans(): Flow<List<Loan>> {
        return debtorDoa.getAllDebtorLoans()
    }

    override fun getPayments(timestamp: Long, debtorId: Int): Flow<List<DebtorPayment>>? {
        return debtorDoa.getPayments(timestamp, debtorId)
    }

    override fun getDailyPayments(timestamp: Long): Flow<List<DailyPayment>> {
        return debtorDoa.getDailyPayments(timestamp)
    }

    override fun getLoansById(id: Int): Flow<List<Loan>> {
        return debtorDoa.getLoanByDebtorId(id)
    }

    override suspend fun deleteLona(debtorLoan: DebtorLoan) {
        debtorDoa.deleteLoan(debtorLoan)
    }

    override fun getPaymentsByIdAndTimestamp(id: Int, timestamp: Long): Flow<List<DailyPayment>> {
        return debtorDoa.getPaymentsByIdAndTimestamp(id,timestamp)
    }

    override fun getPaymentsById(id: Int, limit_st: Int, limit_ed: Int): Flow<List<DailyPayment>> {
        return debtorDoa.getPaymentsById(id,limit_st,limit_ed)
    }

    override fun getAllPayments(): Flow<List<DailyPayment>> {
        return debtorDoa.getAllPayments()
    }
}