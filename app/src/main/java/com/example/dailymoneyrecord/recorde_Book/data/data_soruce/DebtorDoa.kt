package com.example.dailymoneyrecord.recorde_Book.data.data_soruce

import androidx.room.*
import com.example.dailymoneyrecord.recorde_Book.domain.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface DebtorDoa {

    //Insert
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDebtor(debtor: Debtor)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLone(debtorLoan: DebtorLoan)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPayment(debtorPayment: DebtorPayment)

    //Delete
    @Delete
    suspend fun deleteDebtor(debtor: Debtor)

    @Delete
    suspend fun deleteLoan(debtorLoan: DebtorLoan)

    @Delete
    suspend fun deleteDebtorPayment(debtorPayment: DebtorPayment)

    //Query
    @Transaction
    @Query("SELECT * FROM Debtor")
    suspend fun getDebtorWithPayments(): List<DebtorWithPayments>

    @Transaction
    @Query("SELECT * FROM Debtor")
    suspend fun getDebtorWithLone(): List<DebtorWithLoan>

    @Query("SELECT * FROM debtor")
    fun getAllDebtor(): Flow<List<Debtor>>

    @Query("SELECT L.loneId as loanId,D.debtorId as debtorId ,D.name as DebtorName, L.amount as LoneAmount , L.timestamp as timeStamp, D.color as color,L.status as status FROM DebtorLoan as L INNER JOIN Debtor as D WHERE L.loneHolder== D.debtorId ")
    fun getAllDebtorLoans(): Flow<List<Loan>>

    @Query("SELECT L.loneId as loanId,D.debtorId as debtorId ,D.name as DebtorName, L.amount as LoneAmount , L.timestamp as timeStamp, D.color as color,L.status as status FROM DebtorLoan as L INNER JOIN Debtor as D WHERE L.loneHolder== D.debtorId AND D.debtorId==:id ")
    fun getLoanByDebtorId(id: Int): Flow<List<Loan>>

    @Query("SELECT * FROM debtorPayment WHERE timestamp > :timestamp AND paymentHolder == :debtorId")
    fun getPayments(timestamp: Long, debtorId: Int): Flow<List<DebtorPayment>>

    //TODO("daily payments list")
    @Query(
        "SELECT D.color as color,D.debtorId as debtorId,paymentId as paymentId,D.name as debtorName,P.timestamp as timeStamp ,p.amount as amount FROM DebtorPayment as P INNER JOIN Debtor as D on P.paymentHolder=D.debtorId where p.timestamp== :timestamp "
    )
    fun getDailyPayments(timestamp: Long): Flow<List<DailyPayment>>


    @Query("SELECT D.color as color,D.debtorId as debtorId,paymentId as paymentId,D.name as debtorName,P.timestamp as timeStamp ,p.amount as amount FROM DebtorPayment as P INNER JOIN Debtor as D on P.paymentHolder=D.debtorId where P.paymentHolder==:id AND p.timestamp > :timestamp ")
    fun getPaymentsByIdAndTimestamp(id: Int, timestamp: Long): Flow<List<DailyPayment>>


    @Query("SELECT D.color as color,D.debtorId as debtorId,paymentId as paymentId,D.name as debtorName,P.timestamp as timeStamp ,p.amount as amount FROM DebtorPayment as P INNER JOIN Debtor as D on P.paymentHolder=D.debtorId where P.paymentHolder == :id LIMIT :limit_st,:limit_ed ")
    fun getPaymentsById(id: Int, limit_st: Int, limit_ed: Int): Flow<List<DailyPayment>>

    @Query(
        "SELECT D.color as color,D.debtorId as debtorId,paymentId as paymentId,D.name as debtorName,P.timestamp as timeStamp ,p.amount as amount FROM DebtorPayment as P INNER JOIN Debtor as D on P.paymentHolder=D.debtorId  ORDER BY D.name ASC , P.timestamp DESC "
    )
    fun getAllPayments():Flow<List<DailyPayment>>
}