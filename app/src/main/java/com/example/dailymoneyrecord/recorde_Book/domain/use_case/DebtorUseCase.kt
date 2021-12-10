package com.example.dailymoneyrecord.recorde_Book.domain.use_case

import com.example.dailymoneyrecord.recorde_Book.domain.use_case.cases.*

data class DebtorUseCase(
    val getDebtors: GetDebtors,
    val insertDebtor: InserDebtor,
    val insertPayment: InsertPayment,
    val deleteDebtor: DeleteDebtor,
    val getDailyPayments: GetDailyPayments,
    val deletePayment : DeletePayment,
    val deleteLoan : DeleteLoan,
    val getLoans: GetLoans,
    val insertLoan:AddLoan,
    val getLoansByName:GetLoansByName,
    val getPaymentsByIdAndTime: GetPaymetsByIdAndTime,
    val getPaymentsById:GetPaymentById,
    val getAllPayments: GetAllPayments

    )