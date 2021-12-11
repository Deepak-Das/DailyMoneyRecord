package com.example.dailymoneyrecord.recorde_Book.presentation.Daily

import android.content.Context
import android.widget.DatePicker
import android.widget.Toast
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.dailymoneyrecord.recorde_Book.domain.model.DebtorLoan
import com.example.dailymoneyrecord.recorde_Book.domain.model.DebtorPayment
import com.example.dailymoneyrecord.recorde_Book.presentation.Daily.component.DefaultAutoComplete
import com.example.dailymoneyrecord.recorde_Book.presentation.Debtor.component.DefaultCard
import com.example.dailymoneyrecord.recorde_Book.presentation.Debtor.component.DefaultDisplayBox
import com.example.dailymoneyrecord.recorde_Book.presentation.theme.*
import com.example.dailymoneyrecord.recorde_Book.presentation.util.Filter
import java.sql.Date
import java.text.SimpleDateFormat

@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
fun DailyScreen(
    viewModel: DailyViewModel = hiltViewModel(),
    mcontext: Context = LocalContext.current
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


    var dateselect: Long by remember {
        mutableStateOf(SimpleDateFormat("yyyy-MM-dd").parse(Date(System.currentTimeMillis()).toString()).time)
    }

    val state = viewModel.dailyState.value
    var dateTimeStamp: Long

    var picker by remember {
        mutableStateOf(false)
    }

    if (picker) {
        AlertDialog(onDismissRequest = { picker = false }, text = {
            AndroidView(
                { DatePicker(it) }, update = { view ->
                    view.setOnDateChangedListener { calendarView, y, m, d ->
                        dateselect = SimpleDateFormat("dd-MM-yyyy").parse("$d-${m + 1}-$y").time
                        picker = false
                    }
                }
            )
        }, confirmButton = {}
        )
    }

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
                title = "Daily_Payments",

                state.dateStamp,
                { viewModel.onEvent(Event.DateChange(it)) }
            )
            //TODO: Add button for daily_Pdf
            Button(
                onClick = { viewModel.pdfGenerate(mcontext) },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = option2,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(50)
            ) {
                Text(text = "GENERATE PDF")
            }
        }

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(state.payments) {
                DefaultCard(
                    textName = it.debtorName,
                    textDate = it.timeStamp,
                    textAmount = "Rs. ${it.amount} /-",
                    textAmountType = "DAILY PAY",
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
                Text(text = "Enter Lone Amount\n",fontWeight = FontWeight.Bold)
            },
                text = {
                    Column {
                        TextField(
                            enabled = false,
                            value = alertName,
                            onValueChange = { alertName = it },
                            placeholder = {
                                Text(text = "Name")
                            })
                        Spacer(modifier = Modifier.height(6.dp))
                        TextField(
                            value = alertAmount,
                            onValueChange = { alertAmount = it },
                            placeholder = {
                                Text(text = "Amount")
                            }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            modifier = Modifier.onFocusChanged { picker = it.hasFocus },
                            value = SimpleDateFormat("dd-MM-yyyy").format(Date(dateselect)).toString(), onValueChange = {},
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Default.DateRange,
                                    contentDescription = "Date",
                                    Modifier.clickable { picker = !picker })
                            },
                            maxLines = 1,
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                trailingIconColor = Pink800,
                                focusedBorderColor = Pink500
                            ),
                            readOnly = true,
                        )

                    }
                }, confirmButton = {
                    Button(
                        onClick = {
                            viewModel.onEvent(
                                Event.Loan(
                                    DebtorLoan(
                                        null,
                                        ID.toInt(),
                                        alertAmount.toInt(),
                                        dateselect,
                                        "Running"
                                    )
                                )
                            )
                            alertName = ""
                            alertAmount = ""
                            ID = ""
                            dateselect=SimpleDateFormat("yyyy-MM-dd").parse(Date(System.currentTimeMillis()).toString()).time
                        }, colors = ButtonDefaults.buttonColors(
                            backgroundColor = Pink500, contentColor = Color.White
                        )
                    ) {
                        Text(text = "ADD")
                    }
                })

        }
        DefaultAutoComplete(
            debtors = state.debtors,
            insertPay = {
                if(state.debtorId!=null)
                viewModel.onEvent(Event.onInsertPayment)
                else
                    Toast.makeText(mcontext,"ID is null pleas select form list",Toast.LENGTH_SHORT).show()
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