package com.example.dailymoneyrecord.recorde_Book.presentation.Payments

import android.content.Context
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.dailymoneyrecord.recorde_Book.domain.model.DebtorPayment
import com.example.dailymoneyrecord.recorde_Book.presentation.Daily.Event
import com.example.dailymoneyrecord.recorde_Book.presentation.Debtor.component.DefaultCard
import com.example.dailymoneyrecord.recorde_Book.presentation.Debtor.component.DefaultDisplayBox
import com.example.dailymoneyrecord.recorde_Book.presentation.Lone.component.LoanAutoCompleteDebtor
import com.example.dailymoneyrecord.recorde_Book.presentation.theme.Pink500
import com.example.dailymoneyrecord.recorde_Book.presentation.util.Filter

@ExperimentalMaterialApi
@ExperimentalAnimationApi
@Composable
fun PaymentScreen(
    viewModel: PaymentViewModel = hiltViewModel(),
    debtorID: Int,
    loneDate: Long,
    mContext: Context = LocalContext.current
) {

    val state = viewModel.paymentState.value

    var paystoggle by remember {
        mutableStateOf(false)
    }


    Column(Modifier.fillMaxWidth()) {
//        Toast.makeText(LocalContext.current,"#$debtorID(${Date(loneDate)})",Toast.LENGTH_SHORT).show()

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
                textEntries = state.paymentList.size,
                title="Payments",
                state.loanDate,
                { viewModel.onEvent(Event.DateChange(it)) }
            )

            Button(
                onClick = { paystoggle = !paystoggle },
                colors = ButtonDefaults.buttonColors(contentColor = Color.White)
            ) {
                Text(text = "CLICK TO GET PAYS BY NAME")
            }
        }

        var st by remember {
            mutableStateOf("0")
        }
        var ed by remember {
            mutableStateOf("60")
        }


        if (paystoggle) {
            AlertDialog(
                onDismissRequest = { paystoggle = false },
                title = { Text(text = "Type Name and set limit") },
                text = {

                    Column(Modifier.fillMaxWidth()) {
                        Spacer(modifier = Modifier.height(6.dp))
                        LoanAutoCompleteDebtor(
                            debtors = state.debtorList,
                            getLoans = {
                                if (it != null) {
                                    viewModel.setId(it)
                                }
                            })
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            label = { Text(text = "Offset Start") },
                            value = st,
                            onValueChange = { st = it },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            label = { Text(text = "Limit") },
                            value = ed,
                            onValueChange = { ed = it },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            paystoggle = false
                            st = if (st.isEmpty()) "0" else st
                            viewModel.onEvent(Event.Limit(state.debtorId, st.toInt(), ed.toInt()))
                        },
                        colors = ButtonDefaults.buttonColors(contentColor = Color.White)
                    ) {
                        Text(text = "Search")
                    }

                },
            )
        }

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(state.paymentList) {
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
                    onClickEdit = {},
                    color = it.color, onClickLone = {}
                )
            }
        }


//        LoanAutoCompleteDebtor(debtors = state.debtorList, getLoans = {
//            viewModel.onEvent(Event.LoneByName(it))
//        })
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
            Button(
                onClick = { viewModel.pdfGenerate(mContext) },
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(contentColor = Color.White)
            ) {
                Text(text = "Generate PDF")
            }
            Button(
                onClick = { },
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(contentColor = Color.White)
            ) {
                Text(text = "Generate CSV")
            }
        }
    }


}