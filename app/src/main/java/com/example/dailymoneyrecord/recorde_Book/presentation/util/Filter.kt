package com.example.dailymoneyrecord.recorde_Book.presentation.util

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
import androidx.compose.ui.unit.dp
import com.example.dailymoneyrecord.recorde_Book.domain.util.OrderBy
import com.example.dailymoneyrecord.recorde_Book.domain.util.OrderType

@ExperimentalAnimationApi
@Composable
fun Filter(
    orderBy: OrderBy,
    onOrderChange: (ordBy:OrderBy)->Unit,
) {

    var isExpand by remember {
        mutableStateOf(false)
    }
    Column(modifier=Modifier.fillMaxWidth()){

    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Card(
            modifier = Modifier.fillMaxWidth().padding(10.dp),
            shape = MaterialTheme.shapes.medium,
            elevation = 10.dp,
        ) {
            Row(
                modifier = Modifier.padding(5.dp).padding(horizontal = 10.dp),
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
        OrderList(orderBy = orderBy, onSelected = { onOrderChange(it) })
    }
}

}

