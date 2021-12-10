package com.example.dailymoneyrecord.recorde_Book.presentation.Home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dailymoneyrecord.R

@Preview(showBackground = true)
@Composable
fun Header() {


    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {
        Card(
            modifier = Modifier
                .size(100.dp)
                .padding(10.dp),
            shape = CircleShape,
            elevation = 3.dp
        ) {
            Image(
                painterResource(R.drawable.maa_tera),
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
        Spacer(modifier = Modifier.padding(10.dp))
        Text(text = "JAI MAA TARA", fontSize = 18.sp)
    }

}

