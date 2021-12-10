package com.example.dailymoneyrecord.recorde_Book.presentation.Lone.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Sort
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.dailymoneyrecord.recorde_Book.domain.util.OrderBy
import com.example.dailymoneyrecord.recorde_Book.domain.util.OrderType
import com.example.dailymoneyrecord.recorde_Book.domain.util.Status
import com.example.dailymoneyrecord.recorde_Book.presentation.util.DefaultRadioButton

@ExperimentalAnimationApi
@Composable
fun FilterStatus(
    status: Status,
    onOrderChange: (Status) -> Unit,
) {

    var isExpand by remember {
        mutableStateOf(false)
    }
    Column(modifier = Modifier.fillMaxWidth()) {

        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                shape = MaterialTheme.shapes.medium,
                elevation = 10.dp,
            ) {
                Row(
                    modifier = Modifier
                        .padding(5.dp)
                        .padding(horizontal = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Filter", style = MaterialTheme.typography.h6)
                    Icon(
                        imageVector = Icons.Default.Sort,
                        contentDescription = "filter",
                        Modifier.clickable {
                            isExpand = !isExpand
                        })

                }
            }


        }
        AnimatedVisibility(visible = isExpand) {

            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    backgroundColor = Color.White,
                    elevation = 10.dp
                ) {
                    Column(
                        Modifier
                            .fillMaxWidth(.9f)
                            .padding(6.dp)
                    ) {

                        Row(
                            Modifier.fillMaxWidth(.8f),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            DefaultRadioButton(label = "Running",
                                selected = status is Status.Running,
                                onSelected = { onOrderChange(Status.Running(status.orderType)) }
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            DefaultRadioButton(label = "Paid",
                                selected = status is Status.Paid,
                                onSelected = { onOrderChange(Status.Paid(status.orderType)) }
                            )
                            DefaultRadioButton(label = "All",
                                selected = status is Status.All,
                                onSelected = { onOrderChange(Status.All(status.orderType)) }
                            )
                        }
                        Row(
                            Modifier.fillMaxWidth(.8f),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            DefaultRadioButton(label = "Ascending",
                                selected = status.orderType is OrderType.Ascending ,
                                onSelected = { onOrderChange(status.copy(OrderType.Ascending)) }
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            DefaultRadioButton(label = "Descending",
                                selected = status.orderType is OrderType.Descending,
                                onSelected = { onOrderChange(status.copy(OrderType.Descending)) }
                            )
                        }
                    }


                }
            }

        }
    }
}
