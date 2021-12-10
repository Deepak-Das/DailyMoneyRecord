package com.example.dailymoneyrecord.recorde_Book.presentation.Home.component

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.dailymoneyrecord.recorde_Book.presentation.theme.Shapes


@Composable
fun DefaultOption(
//    navController: NavController,
    changeRoute:()->Unit,
    context:Context= LocalContext.current,
    text1: String="Dumb Text",
    text2: String="Dumb Text",
    color: Color = Color.Cyan
) {

    Box(
        modifier = Modifier
            .size(100.dp, 80.dp)
            .background(color = color, shape = Shapes.medium)
            .clickable {
                changeRoute()
            },
        contentAlignment = Alignment.Center


    ) {
        Column(modifier = Modifier.fillMaxWidth(),verticalArrangement = Arrangement.Center,horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = text1,
                color = Color.White,
                style = MaterialTheme.typography.body1,
                maxLines = 1,
                fontWeight = FontWeight.Bold

            )
            Text(
                text = text2,
                color = Color.White,
                style = MaterialTheme.typography.body1,
                maxLines = 1,
                fontWeight = FontWeight.Bold

            )
        }


    }
}

