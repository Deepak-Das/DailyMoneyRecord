package com.example.dailymoneyrecord.recorde_Book.presentation.util

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.example.dailymoneyrecord.recorde_Book.domain.model.Debtor
import com.example.dailymoneyrecord.recorde_Book.presentation.theme.Pink500
import com.example.dailymoneyrecord.recorde_Book.presentation.theme.Pink800

@Composable
fun DefaultTextField(
    modifier: Modifier = Modifier.fillMaxWidth(),
    onAdd: (Debtor) -> Unit,
    localContext: Context = LocalContext.current,
    debtors: List<Debtor>,
) {
    var inputName by remember {
        mutableStateOf("")
    }
    var inputAmount by remember {
        mutableStateOf("")
    }
    var isExpand by remember {
        mutableStateOf(false)
    }
    var textfieldSize by remember { mutableStateOf(Size.Zero) }
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {

        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Spacer(modifier = Modifier.width(10.dp))

            TextField(
                modifier = Modifier
                    .onGloballyPositioned { coordinates ->
                        textfieldSize = coordinates.size.toSize()
                    }
                    .weight(1f),
                label = {
                    Text(text = "Name")
                },
                value = inputName,
                onValueChange = {
                    inputName = it
                },
                placeholder = {
                    Text(text = "Name...")
                },
                shape = RoundedCornerShape(10.dp),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.White,
                    cursorColor = Pink500,
                    focusedIndicatorColor = Pink800
                )


            )
            Spacer(modifier = Modifier.width(10.dp))
            TextField(
                modifier = Modifier.width(150.dp),
                label = {
                    Text(text = "Amount")
                },
                value = inputAmount,
                onValueChange = { inputAmount = it },
                placeholder = {
                    Text(text = "Amount...")
                },
                shape = RoundedCornerShape(10.dp),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.White,
                    cursorColor = Pink500,
                    focusedIndicatorColor = Pink800

                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = {
                    if (!inputName.isBlank()) {

                        onAdd(Debtor(null, inputName, "Goglghar", System.currentTimeMillis(),Debtor.colorCode().toArgb()))
                    } else {
                        Toast.makeText(localContext, "fill name", Toast.LENGTH_SHORT)
                    }
                })
            )
            Spacer(modifier = Modifier.width(10.dp))
            DropdownMenu(expanded = !isExpand,
                onDismissRequest = { isExpand = !isExpand },
                modifier = Modifier
                    .width(with(LocalDensity.current) { textfieldSize.width.toDp() })
            ) {

                debtors.onEach {

                    Column {
                        DropdownMenuItem(onClick = {
                            inputName = it.name
                            isExpand = false
                        }) {
                            Row(
                                modifier = Modifier.width(100.dp),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(text = "ID#${it.debtorId} ${it.name}")

                            }

                        }
                    }
                }
            }

        }
    }


}
