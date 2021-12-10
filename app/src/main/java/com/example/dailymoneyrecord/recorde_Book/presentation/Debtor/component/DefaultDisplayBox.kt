package com.example.dailymoneyrecord.recorde_Book.presentation.Debtor.component

import android.widget.CalendarView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.dailymoneyrecord.recorde_Book.presentation.theme.option1
import com.example.dailymoneyrecord.recorde_Book.presentation.theme.text_Amount_highlight
import java.sql.Date
import java.text.SimpleDateFormat

@Composable
fun DefaultDisplayBox(
    textTotal: Int,
    textEntries: Int,
    title: String = "Title",
    timeStamp: Long,
    dateChange: (new: Long) -> Unit,
) {

    var isExpandCalendar by remember {
        mutableStateOf(false)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth(.8f)
            .background(color = Color.White, shape = MaterialTheme.shapes.medium),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.Bold,
                        color = Color.Red
                    )
                ) {
                    append(title)
                }

            }
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {

                Box(
                    modifier = Modifier
                        .size(100.dp), contentAlignment = Alignment.Center
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = textTotal.toString(),
                            color = text_Amount_highlight,
                            style = MaterialTheme.typography.h6
                        )
                        Text(
                            text = "Total",
                            color = Color.LightGray,
                            style = MaterialTheme.typography.body2,
                            fontWeight = FontWeight.Bold

                        )

                    }
                }

                Canvas(
                    modifier = Modifier
                        .width(3.dp)
                        .height(130.dp)
                ) {
                    val canvasWidth = size.width
                    val canvasHeight = size.height

                    drawLine(
                        start = Offset(x = canvasWidth / 2, y = 50f),
                        end = Offset(x = canvasWidth / 2, y = canvasHeight - 50f),
                        color = Color.LightGray,
                        strokeWidth = 10F
                    )
                }
                Box(
                    modifier = Modifier
                        .size(100.dp), contentAlignment = Alignment.Center
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = textEntries.toString(),
                            color = text_Amount_highlight,
                            style = MaterialTheme.typography.h6
                        )
                        Text(
                            text = "Entries",
                            color = Color.LightGray,
                            style = MaterialTheme.typography.body2,
                            fontWeight = FontWeight.Bold
                        )

                    }
                }


            }

            Text(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(fontWeight = FontWeight.W900, color = option1)
                    ) {
                        append("DATE #${SimpleDateFormat("dd-MM-yyyy").format(Date(timeStamp))}")
                    }
                }, modifier = Modifier.clickable { isExpandCalendar = !isExpandCalendar }
            )
        }


    }

    AnimatedVisibility(visible = isExpandCalendar) {
        AndroidView(
            { CalendarView(it) },
            modifier = Modifier.fillMaxWidth(1f),
            update = { views ->
                views.setOnDateChangeListener { calendarView, y, m, d ->
                    val date = SimpleDateFormat("dd-MM-yyyy").parse("$d-${m + 1}-$y")
                    dateChange(date.time)
                    isExpandCalendar = !isExpandCalendar
                }
            }
        )
    }

    Spacer(modifier = Modifier.height(10.dp))
}
