package com.example.dailymoneyrecord.recorde_Book.presentation.Daily.component

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.dailymoneyrecord.recorde_Book.domain.model.Debtor
import com.example.dailymoneyrecord.recorde_Book.presentation.theme.Pink500
import com.example.dailymoneyrecord.recorde_Book.presentation.theme.Shapes
import com.example.dailymoneyrecord.recorde_Book.presentation.theme.buttonEdit
import com.example.dailymoneyrecord.recorde_Book.presentation.theme.option1

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Composable
fun DefaultAutoComplete(
    debtors: List<Debtor>,
    insertPay: () -> Unit,
    setDebtorId: (Int?) -> Unit,
    name: String,
    amount: String,
    setName: (String) -> Unit,
    setAmount: (String) -> Unit,
) {
    var expand by remember {
        mutableStateOf(false)
    }


    val focusRequesterName = FocusRequester()
    val focusRequesterAmount = FocusRequester()

    Column(
        Modifier
            .fillMaxWidth()
            .background(Pink500, shape = Shapes.medium),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
//        BackHandler(onBack ={flag=false})

        Spacer(modifier = Modifier.height(10.dp))


        ExposedDropdownMenuBox(expanded = expand, onExpandedChange = { expand = !expand }) {


            OutlinedTextField(
                modifier = Modifier
                    .focusRequester(focusRequesterName).onFocusChanged { focusState ->
                        expand=focusState.isFocused
                    },
                value = name,
                onValueChange = { setName(it) },
                placeholder = { Text(text = "Name") },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    backgroundColor = Color.White,
                    unfocusedBorderColor = option1,
                    unfocusedLabelColor = buttonEdit
                ),
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = expand
                    )
                },
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        if (name.isBlank()) {
                            focusRequesterName.captureFocus()
                        }

                    })
            )


//=============================================================
            val filterOptions =
                debtors.filter { it.name.contains(name, true) }
            if (filterOptions.isNotEmpty()) {
                ExposedDropdownMenu(expanded = expand, onDismissRequest = { expand = false }) {
                    filterOptions.forEach {
                        DropdownMenuItem(onClick = {
                            setName(it.name)
                            expand = false
                            focusRequesterAmount.requestFocus()
                            focusRequesterName.freeFocus()
                            if (it.name.isNotBlank()) setDebtorId(it.debtorId) else setDebtorId(null)
                        }) {
                            Text(text = it.name)
                        }
                    }
                }
            }

        }

        Spacer(modifier = Modifier.height(6.dp))

        OutlinedTextField(
            modifier = Modifier
                .focusRequester(focusRequesterAmount),
            value = amount,
            onValueChange = { setAmount(it) },
            placeholder = { Text(text = "Amount") },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                backgroundColor = Color.White,
                unfocusedBorderColor = option1,
                unfocusedLabelColor = buttonEdit
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    insertPay()
                    focusRequesterAmount.freeFocus()
                    focusRequesterName.requestFocus()
                })

        )
        Spacer(modifier = Modifier.height(10.dp))


    }

}
