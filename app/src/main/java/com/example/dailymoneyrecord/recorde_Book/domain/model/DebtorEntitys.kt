package com.example.dailymoneyrecord.recorde_Book.domain.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.example.dailymoneyrecord.recorde_Book.presentation.theme.*

@Entity
data class Debtor(
    @PrimaryKey(autoGenerate = true) val debtorId: Int? = null,
    val name: String,
    val address: String?,
    val timestamp: Long,
    val color: Int


//    val date:Date = Date(System.currentTimeMillis())

) {
    companion object{
        fun colorCode() = listOf(option1, option2, option3, option4, option5, option6).random()
    }
}

@Entity
data class DebtorLoan(
    @PrimaryKey(autoGenerate = true) val loneId: Int?,
    val loneHolder: Int,
    val amount: Int,
    val timestamp: Long,
    val status:String
)

data class DebtorWithLoan(
    @Embedded val debtor: Debtor,
    @Relation(
        parentColumn = "debtorId",
        entityColumn = "loneHolder",
    )
    val loan_list: List<DebtorLoan>
)

@Entity
data class DebtorPayment(
    @PrimaryKey(autoGenerate = true) val paymentId: Int?,
    val paymentHolder: Int?,
    val amount: Int?,
    val timestamp: Long
)

data class DebtorWithPayments(
    @Embedded val debtor: Debtor,
    @Relation(
        parentColumn = "debtorId",
        entityColumn = "paymentHolder"
    )
    val payments: List<DebtorPayment>
)
