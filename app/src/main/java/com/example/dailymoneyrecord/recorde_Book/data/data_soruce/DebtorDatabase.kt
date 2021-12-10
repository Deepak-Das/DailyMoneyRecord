package com.example.dailymoneyrecord.recorde_Book.data.data_soruce

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.dailymoneyrecord.recorde_Book.domain.model.*

@Database(
    entities = [
        Debtor::class,
        DebtorLoan::class,
        DebtorPayment::class],
    version = 1
)
abstract class DebtorDatabase : RoomDatabase() {

    abstract val debtorDoa: DebtorDoa

    companion object {
        const val DATABASE_NAME = "debtor_Recorde_DB"
    }
}