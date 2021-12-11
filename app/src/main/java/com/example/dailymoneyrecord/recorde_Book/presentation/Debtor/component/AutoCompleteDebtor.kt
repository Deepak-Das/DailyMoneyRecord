package com.example.dailymoneyrecord.recorde_Book.presentation.Debtor.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import com.example.dailymoneyrecord.recorde_Book.domain.model.Debtor

@ExperimentalMaterialApi
@Composable
fun AutoCompleteDebtor(
    debtors: List<Debtor>,
    insertDebtor: (Debtor) -> Unit,
    timeStamp: Long,

) {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        val options = debtors
        var textName by remember { mutableStateOf("") }
        var id: Int? by remember {
            mutableStateOf(null)
        }
        var isExpand by remember {
            mutableStateOf(false)
        }
//            Log.d("Checkid1", "DebtorScreen: $id")
        if (textName.isBlank())
            id = null

        ExposedDropdownMenuBox(
            expanded = isExpand,
            onExpandedChange = { isExpand = !isExpand }
        ) {
            OutlinedTextField(
                value = textName,
                onValueChange = {
                    textName = it
                },
                label = { Text("Name") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = isExpand
                    )
                },
                colors = ExposedDropdownMenuDefaults.textFieldColors(),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = {
                    insertDebtor(
                        Debtor(
                            id,
                            textName.trim(),
                            "N/A",
                            timeStamp,
                            Debtor.colorCode().toArgb()
                        )
                    )
                    id = null;
                    textName = ""
                    isExpand=false
                })
            )
            // filter options based on text field value
            val filteringOptions =
                options.filter { it.name.contains(textName, ignoreCase = true) }
            if (filteringOptions.isNotEmpty()) {
                ExposedDropdownMenu(
                    expanded = isExpand,
                    onDismissRequest = { isExpand = false }
                ) {
                    filteringOptions.forEach { debtor ->
                        DropdownMenuItem(
                            onClick = {
                                textName = debtor.name
                                isExpand = false
                                id = debtor.debtorId
                            }
                        ) {
                            Text(text = debtor.name)
                        }
                    }
                }
            }
        }
    }

}