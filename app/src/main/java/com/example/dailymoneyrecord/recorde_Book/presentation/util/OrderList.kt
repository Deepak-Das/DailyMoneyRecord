package com.example.dailymoneyrecord.recorde_Book.presentation.util

import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.dailymoneyrecord.recorde_Book.domain.util.OrderBy
import com.example.dailymoneyrecord.recorde_Book.domain.util.OrderType

@Composable
fun OrderList(
    orderBy:OrderBy,
    onSelected: (OrderBy)->Unit
) {
    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Card(modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        backgroundColor = Color.White,
        elevation = 10.dp) {
            Column(
                Modifier
                    .fillMaxWidth(.9f)
                    .padding(6.dp)) {

                Row(Modifier.fillMaxWidth(.8f), verticalAlignment = Alignment.CenterVertically) {
                    DefaultRadioButton(label = "ID",
                        selected = orderBy is OrderBy.Id,
                        onSelected = { onSelected(OrderBy.Id(orderBy.orderType)) }
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    DefaultRadioButton(label = "Name",
                        selected = orderBy is OrderBy.Name,
                        onSelected = { onSelected(OrderBy.Name(orderBy.orderType)) }
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    DefaultRadioButton(label = "Date",
                        selected = orderBy is OrderBy.TimeStamp,
                        onSelected = { onSelected(OrderBy.TimeStamp(orderBy.orderType)) }
                    )
                }
                Row(Modifier.fillMaxWidth(.8f), verticalAlignment = Alignment.CenterVertically) {
                    DefaultRadioButton(
                        label = "Ascending",
                        selected = orderBy.orderType is OrderType.Ascending,
                        onSelected = { onSelected(orderBy.changeOrderType(OrderType.Ascending)) }
                    )

                    Spacer(modifier = Modifier.width(5.dp))
                    DefaultRadioButton(
                        label = "Descending",
                        selected = orderBy.orderType is OrderType.Descending,
                        onSelected = { onSelected(orderBy.changeOrderType(OrderType.Descending)) }

                    )
                }
            }
        }
    }

}

@Preview
@Composable
fun perviw(){
    OrderList(orderBy = OrderBy.Id(OrderType.Descending), onSelected ={} )
}