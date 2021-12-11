package com.example.dailymoneyrecord.recorde_Book.presentation.Debtor.component

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Money
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.example.dailymoneyrecord.recorde_Book.presentation.theme.text_amount
import java.sql.Date
import java.text.SimpleDateFormat


@SuppressLint("UnrememberedMutableState")
@Composable
fun DefaultCard(
    modifier: Modifier = Modifier
        .padding(6.dp)
        .fillMaxWidth(),
    textstatus: String = "",
    textName: String,
    color: Int,
    textDate: Long,
    textAmount: String,
    textAmountType: String = "",
    onClickDelete: () -> Unit,
    onClickEdit: () -> Unit,
    onClickLone: () -> Unit = {},
    delete: Boolean = true,
    edit: Boolean = true,
    lone: Boolean = true,
    pays: Boolean = false,
    onPaysClick: () -> Unit = {},
//    deleteItem: Debtor = Debtor(null, "XYZ", "ABC", 114522223346)
) {

    var isExpand by remember {
        mutableStateOf(false)
    }

    var size by remember { mutableStateOf(Size.Zero) }
    Card(modifier = modifier, shape = MaterialTheme.shapes.medium, elevation = 10.dp) {
        Row(modifier = Modifier
            .padding(6.dp)
            .pointerInput(Unit) {
                detectTapGestures(onLongPress = {
                    isExpand = !isExpand
                })
            }
            .onGloballyPositioned {
                size = it.size.toSize()
            },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        )
        {

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "${textName[0].uppercase()}${textName[textName.lastIndex].uppercase()}",
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier
                        .background(color = Color(color), shape = CircleShape)
                        .padding(10.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    var status by remember { mutableStateOf("") }
                    status = if (textstatus.isNotBlank()) "($textstatus)" else ""

                    Text(
                        text = "$textName$status",
                        style = MaterialTheme.typography.body1,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = SimpleDateFormat("dd-MM-yyyy").format(Date(textDate)),
                        style = MaterialTheme.typography.body2,
                        color = Color.LightGray
                    )
                }

            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = textAmount, color = text_amount, style = MaterialTheme.typography.h6)
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = textAmountType,
                    color = Color.LightGray,
                    style = MaterialTheme.typography.body2
                )
            }

        }
        DropdownMenu(
            expanded = isExpand, onDismissRequest = { isExpand = false },
            modifier = Modifier
                .width(120.dp), offset = DpOffset(200.dp, -10.dp)
        ) {


            DropdownMenuItem(onClick = {
                onClickDelete()
                isExpand = false
            }, enabled = delete
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Text(text = "Delete")
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "delete")
                }
            }
            DropdownMenuItem(
                onClick = {
                    onClickEdit()
                    isExpand = false
                },
                enabled = edit,
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Text(text = "Edit")
                    Icon(imageVector = Icons.Default.Edit, contentDescription = "edit")
                }
            }

            DropdownMenuItem(
                onClick = onClickLone,
                enabled = lone,

                ) {
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Text(text = "AddLoan")
                    Icon(imageVector = Icons.Default.Money, contentDescription = "loan")
                }
            }
            DropdownMenuItem(
                onClick = onPaysClick,
                enabled = pays,
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Text(text = "GetPays")
                    Icon(imageVector = Icons.Default.MonetizationOn, contentDescription = "pays")

                }
            }

        }

    }
}


//@Preview
//@Composable
//fun preve() {
//    DefaultCard(
//        textName = "Ashok",
//        textDate = System.currentTimeMillis(),
//        textAmount = "₹. ${10000}/-",
//        textAmountType = "Lone.",
//        onClickDelete = {},
//        onClickEdit = {},
//        color = option1
//        )
//}