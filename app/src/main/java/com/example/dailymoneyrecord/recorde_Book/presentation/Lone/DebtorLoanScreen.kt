package com.example.dailymoneyrecord.recorde_Book.presentation.Lone

import android.widget.DatePicker
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.dailymoneyrecord.recorde_Book.domain.model.DebtorLoan
import com.example.dailymoneyrecord.recorde_Book.presentation.Daily.Event
import com.example.dailymoneyrecord.recorde_Book.presentation.Debtor.component.DefaultCard
import com.example.dailymoneyrecord.recorde_Book.presentation.Debtor.component.DefaultDisplayBox
import com.example.dailymoneyrecord.recorde_Book.presentation.Lone.component.FilterStatus
import com.example.dailymoneyrecord.recorde_Book.presentation.Lone.component.LoanAutoCompleteDebtor
import com.example.dailymoneyrecord.recorde_Book.presentation.theme.Pink500
import com.example.dailymoneyrecord.recorde_Book.presentation.theme.Pink800
import com.example.dailymoneyrecord.recorde_Book.presentation.theme.option2
import com.example.dailymoneyrecord.recorde_Book.presentation.util.Screens
import java.sql.Date
import java.text.SimpleDateFormat

@ExperimentalMaterialApi
@ExperimentalAnimationApi
@Composable
fun DebtorLoanScreen(
    viewModel: LoneViewModel = hiltViewModel(),
    navController: NavController
) {

    var Loan_amount by remember {
        mutableStateOf("")
    }

    var picker by remember {
        mutableStateOf(false)
    }
    var addLoan by remember {
        mutableStateOf(false)
    }
    var dateNow: Long by remember {
        mutableStateOf(SimpleDateFormat("yyyy-MM-dd").parse(Date(System.currentTimeMillis()).toString()).time)
    }

    var LONE_ID: Int? by remember {
        mutableStateOf(null)
    }

    var editAlert by remember {
        mutableStateOf(false)
    }

    val state = viewModel.loneState.value
    var LONE_STATUS by remember {
        mutableStateOf("Running")
    }
    var LONE_NAME by remember {
        mutableStateOf("")
    }
    var LONE_AMOUNT by remember {
        mutableStateOf("")
    }
    var LONE_TIME by remember {
        mutableStateOf(-255445445451)
    }
    var LONE_Holder by remember {
        mutableStateOf(-1)
    }

    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Column(Modifier.background(Pink500), horizontalAlignment = Alignment.CenterHorizontally) {

            FilterStatus(
                status = state.status,
                onOrderChange = { viewModel.onEvent(Event.StatusFilter(it)) }
            )

            DefaultDisplayBox(
                textTotal = state.totalLoan,
                textEntries = state.loans.size,
                title="Loans",
                timeStamp = state.dateStamp,
                dateChange = { viewModel.onEvent(Event.DateChange(it)) }
            )

            Button(
                onClick = {
                    addLoan = !addLoan
                },
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = option2,
                    contentColor = Color.White
                )
            ) {
                Text(text = "CLICK TO ADD LOAN")
            }

        }

        if (addLoan) {
            AlertDialog(onDismissRequest = { addLoan = false },
                title = { Text(text = "Add New LoanID#$LONE_Holder") },
                text = {
                    Column {
                        LoanAutoCompleteDebtor(
                            debtors = state.debtors,
                            getLoans = { LONE_Holder = it ?: -1 })
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            value = Loan_amount,
                            onValueChange = { Loan_amount = it },
                            maxLines = 1,
                            placeholder = { Text(text = "Amount") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            modifier = Modifier.onFocusChanged { picker = it.hasFocus },
                            value = SimpleDateFormat("dd-MM-yyyy").format(Date(dateNow)).toString(), onValueChange = {},
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
                },
                confirmButton = {
                    Button(onClick = {
                        if (LONE_Holder != -1 && Loan_amount!=""){
                            viewModel.onEvent(
                                Event.AddLoan(
                                    DebtorLoan(
                                        null,
                                        LONE_Holder,
                                        Loan_amount.toInt(),
                                        dateNow,
                                        "Running"
                                    )
                                )
                            )
                            addLoan=false
                            Loan_amount=""
                            LONE_Holder=-1
                        }

                    }) {
                        Text(text = "SUBMIT LOAN")
                    }

                })
        }

        if (picker) {
            AlertDialog(onDismissRequest = { picker = false }, text = {
                AndroidView(
                    { DatePicker(it) }, update = { view ->
                        view.setOnDateChangedListener { calendarView, y, m, d ->
                            dateNow = SimpleDateFormat("dd-MM-yyyy").parse("$d-${m + 1}-$y").time
                            picker = false
                        }
                    }
                )
            }, confirmButton = {}
            )
        }
        Box(modifier = Modifier.weight(1f)) {
            LazyColumn {
                items(state.loans) {
                    DefaultCard(
                        textName = it.DebtorName,
                        textstatus = it.status,
                        color = it.color,
                        textDate = it.timeStamp,
                        textAmount = it.LoneAmount.toString(),
                        textAmountType = "Lone. ID#${it.debtorId}",
                        onClickDelete = { viewModel.onEvent(Event.LoanDelete(DebtorLoan(it.loanId,it.debtorId,it.LoneAmount,it.timeStamp,it.status))) },
                        onClickEdit = {
                            LONE_ID = it.loanId
                            LONE_NAME = it.DebtorName
                            LONE_STATUS = it.status
                            LONE_AMOUNT = it.LoneAmount.toString()
                            LONE_TIME = it.timeStamp
                            dateNow=it.timeStamp
                            LONE_Holder = it.debtorId
                            editAlert = true


                        },
                        lone = false,
                        pays = true,
                        delete=false,
                        onPaysClick = { navController.navigate(Screens.PaymentsScreen.route + "?debtorId=${it.debtorId}&timeStamp=${it.timeStamp}&debtorName=${it.DebtorName}&loan=${it.LoneAmount}") }
                    )
                }
            }
        }

        if (editAlert) {

            AlertDialog(onDismissRequest = { editAlert = false },
                title = { Text(text = "CHANGE STATUS ID#$LONE_ID \n") },
                text = {
                    Column(Modifier.fillMaxWidth()) {
                        TextField(value = LONE_NAME, onValueChange = {}, enabled = false)
                        Spacer(modifier = Modifier.height(6.dp))
                        TextField(
                            value = LONE_AMOUNT, onValueChange = { LONE_AMOUNT = it },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)

                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        //TODO: Date problem
                        OutlinedTextField(
                            modifier = Modifier.onFocusChanged { picker = it.hasFocus },
                            value = SimpleDateFormat("dd-MM-yyyy").format(Date(dateNow)).toString(), onValueChange = {},
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
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(Modifier.fillMaxWidth()) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = LONE_STATUS.equals("Running", true),
                                    onClick = { LONE_STATUS = "Running" })
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(text = "Running")

                            }

                            Spacer(modifier = Modifier.width(6.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = LONE_STATUS.equals("Paid", true),
                                    onClick = { LONE_STATUS = "Paid" })
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(text = "Paid")

                            }
                        }
                    }
                },
                confirmButton = {
                    Button(onClick = {
                        viewModel.onEvent(
                            Event.Loan(
                                DebtorLoan(
                                    LONE_ID,
                                    LONE_Holder,
                                    LONE_AMOUNT.toInt(),
                                    dateNow,
                                    LONE_STATUS
                                )
                            )
                        )
                        LONE_ID=null
                        editAlert = false
                        dateNow=SimpleDateFormat("yyyy-MM-dd").parse(Date(System.currentTimeMillis()).toString()).time
                    },
                    colors=ButtonDefaults.buttonColors(backgroundColor = option2, contentColor = Color.White )
                        ) {
                        Text("Done")

                    }
                }
            )
        }

        LoanAutoCompleteDebtor(debtors = state.debtors, getLoans = {
            viewModel.onEvent(Event.LoneByName(it))
        },color = Pink500)



    }


}



