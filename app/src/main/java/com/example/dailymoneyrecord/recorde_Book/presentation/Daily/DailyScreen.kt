package com.example.dailymoneyrecord.recorde_Book.presentation.Daily

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.dailymoneyrecord.recorde_Book.domain.model.DebtorLoan
import com.example.dailymoneyrecord.recorde_Book.domain.model.DebtorPayment
import com.example.dailymoneyrecord.recorde_Book.presentation.Daily.component.DefaultAutoComplete
import com.example.dailymoneyrecord.recorde_Book.presentation.Debtor.component.DefaultCard
import com.example.dailymoneyrecord.recorde_Book.presentation.Debtor.component.DefaultDisplayBox
import com.example.dailymoneyrecord.recorde_Book.presentation.theme.Pink500
import com.example.dailymoneyrecord.recorde_Book.presentation.util.Filter

@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
fun DailyScreen(
    viewModel: DailyViewModel = hiltViewModel()
) {
    var alertExpand by remember {
        mutableStateOf(false)
    }
    var alertName by remember {
        mutableStateOf("")
    }
    var alertAmount by remember {
        mutableStateOf("")
    }
    var ID by remember {
        mutableStateOf("")
    }
    val state = viewModel.dailyState.value
    var dateTimeStamp: Long
    Column(Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Pink500),
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            Filter(orderBy = state.orderBy,
                onOrderChange = { viewModel.onEvent(Event.OrderChange(it)) }
            )


            DefaultDisplayBox(
                textTotal = state.totalAmount,
                textEntries = state.payments.size,
                title="Daily_Payments",

                state.dateStamp,
                { viewModel.onEvent(Event.DateChange(it)) }
            )
        }

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(state.payments) {
                DefaultCard(
                    textName = it.debtorName,
                    textDate = it.timeStamp,
                    textAmount = it.amount.toString(),
                    textAmountType = "DAILY PAY ID#${it.debtorId}",
                    //only id required  to delete ,fake payment were made with actuall id
                    onClickDelete = {
                        viewModel.onEvent(
                            Event.onDelete(
                                DebtorPayment(
                                    it.paymentId,
                                    null,
                                    0,
                                    it.timeStamp
                                )
                            )
                        )
                    },
                    onClickEdit = {
                        viewModel.setName(it.debtorName)
                        viewModel.setDebId(it.debtorId)
                        viewModel.setPayId(it.paymentId)
                        viewModel.setAmount(it.amount.toString())
                    },
                    color = it.color, onClickLone = {
                        ID = it.debtorId.toString()
                        alertExpand = true
                        alertName = it.debtorName
                    }
                )
            }
        }



        if (alertExpand) {
            AlertDialog(onDismissRequest = { alertExpand = false }, title = {
                Text(text = "Enter Lone #$ID")
            },
                text = {
                    Column {
                        TextField(
                            enabled= false,
                            value = alertName,
                            onValueChange = { alertName = it },
                            placeholder = {
                                Text(text = "Name")
                            })
                        Spacer(modifier = Modifier.height(6.dp))
                        TextField(value = alertAmount,
                            onValueChange = { alertAmount = it },
                            placeholder = {
                                Text(text = "Amount")
                            },keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )

                    }
                }, confirmButton = {
                    Button(
                        onClick = {
                            viewModel.onEvent(Event.Loan(DebtorLoan(null,ID.toInt(),alertAmount.toInt(),state.dateStamp,"Running")))
                            alertName = ""
                            alertAmount = ""
                            ID = ""
                        }, colors = ButtonDefaults.buttonColors(
                            backgroundColor = Pink500, contentColor = Color.White
                        )
                    ) {
                        Text(text = "ADD")
                    }
                })

        }
        //AutoComplete
        DefaultAutoComplete(
            debtors = state.debtors,
            insertPay = {
                viewModel.onEvent(Event.onInsertPayment)
            },
            setDebtorId = {
                viewModel.setDebId(it)
            },
            name = state.textName,
            amount = state.textAmount.toString(),
            setName = { viewModel.setName(it) },
            setAmount = { viewModel.setAmount(it) }
        )

    }


}