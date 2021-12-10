package com.example.dailymoneyrecord.di

import android.app.Application
import androidx.room.Room
import com.example.dailymoneyrecord.recorde_Book.data.data_soruce.DebtorDatabase
import com.example.dailymoneyrecord.recorde_Book.data.repository.DebtorRepositoryImp
import com.example.dailymoneyrecord.recorde_Book.domain.repository.DebtorRepository
import com.example.dailymoneyrecord.recorde_Book.domain.use_case.*
import com.example.dailymoneyrecord.recorde_Book.domain.use_case.cases.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDebtorDatabase(app: Application): DebtorDatabase {
        return Room.databaseBuilder(
            app,
            DebtorDatabase::class.java,
            DebtorDatabase.DATABASE_NAME
        ).build()

    }

    @Provides
    @Singleton
    fun provideDebtorRepository(db: DebtorDatabase): DebtorRepository {
        return DebtorRepositoryImp(db.debtorDoa)
    }

    @Provides
    @Singleton
    fun provideDebtorUseCase(repository : DebtorRepository): DebtorUseCase {
        return DebtorUseCase(
            getDebtors = GetDebtors(repository),
            insertDebtor = InserDebtor(repository),
            deleteDebtor = DeleteDebtor(repository),
            getDailyPayments = GetDailyPayments(repository),
            deletePayment = DeletePayment(repository),
            insertPayment = InsertPayment(repository),
            getLoans = GetLoans(repository),
            insertLoan = AddLoan(repository),
            getLoansByName = GetLoansByName(repository),
            deleteLoan = DeleteLoan(repository),
            getPaymentsByIdAndTime = GetPaymetsByIdAndTime(repository),
            getPaymentsById = GetPaymentById(repository),
            getAllPayments = GetAllPayments(repository)
        )
    }
}