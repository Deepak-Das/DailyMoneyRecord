package com.example.dailymoneyrecord.recorde_Book.presentation.util

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.dailymoneyrecord.recorde_Book.presentation.Debtor.DebtorViewModel
import com.example.dailymoneyrecord.recorde_Book.presentation.Debtor.component.AutoCompleteDebtor
import com.example.dailymoneyrecord.recorde_Book.presentation.Debtor.component.DefaultCard
import com.example.dailymoneyrecord.recorde_Book.presentation.Debtor.component.DefaultDisplayBox
import com.example.dailymoneyrecord.recorde_Book.presentation.theme.Pink500

@ExperimentalMaterialApi
@ExperimentalAnimationApi
@Preview(showBackground = true)
@ExperimentalComposeUiApi
@Composable
fun DebtorScreen(
    viewModel: DebtorViewModel = hiltViewModel(),
    mContext: Context = LocalContext.current
) {

//    val focusRequester = FocusRequester.createRefs()

    val state = viewModel.debtorState.value


    var dateTimeStamp by remember {
        mutableStateOf(System.currentTimeMillis())
    }




    Column(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Pink500),
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            Filter(orderBy = state.orderBy,
                onOrderChange = { viewModel.onOrderClick(it) }
            )

            DefaultDisplayBox(textTotal = 50000,
                textEntries = state.debtors.size,
                title = "Debtor",
                dateTimeStamp,
                { dateTimeStamp = it })
        }

        Box(modifier = Modifier.weight(1f)) {

            LazyColumn {
                items(state.debtors) { debtor ->

                    DefaultCard(
                        textName = debtor.name,
                        textDate = debtor.timestamp,
                        textAmount = "N/A",
                        textAmountType = "ID#${debtor.debtorId}",
                        color = debtor.color,
                        onClickDelete = { viewModel.delete(debtor) },
                        onClickEdit = {}, edit = false,
                        delete = false,
                        lone = false
                    )

                }
            }
        }


        AutoCompleteDebtor(
            label="Edit debtor name here..",
            debtors = state.debtors,
            insertDebtor = {
                    viewModel.insert(it,mContext)
            },
            timeStamp = dateTimeStamp
        )

        Spacer(modifier = Modifier.height(6.dp))


    }


}


