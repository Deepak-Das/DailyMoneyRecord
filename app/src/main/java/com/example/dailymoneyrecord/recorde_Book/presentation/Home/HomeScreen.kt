package com.example.dailymoneyrecord.recorde_Book.presentation

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.dailymoneyrecord.recorde_Book.presentation.Home.HomeViewModel
import com.example.dailymoneyrecord.recorde_Book.presentation.Home.component.DefaultOption
import com.example.dailymoneyrecord.recorde_Book.presentation.Home.component.Header
import com.example.dailymoneyrecord.recorde_Book.presentation.theme.*
import com.example.dailymoneyrecord.recorde_Book.presentation.util.NavigationItem
import com.example.dailymoneyrecord.recorde_Book.presentation.util.Screens

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel(),
    context: Context = LocalContext.current
) {

    Scaffold(bottomBar = {
        BottomNavigation(backgroundColor = Pink800, contentColor = Color.White) {
            BottomNavigationItem(
                selected = true, onClick = { /*TODO*/ },
                icon = { Icon(imageVector = NavigationItem.Home.icon, contentDescription = "j") },
                label = { Text(text = NavigationItem.Home.title) }
            )
        }
    }) {
        Header()
        Column(
            Modifier.fillMaxSize(),
            Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row {
                DefaultOption(
                    text1 = "DAILY",
                    text2 = "RECORD",
                    color = option1,
                    changeRoute = { navController.navigate(Screens.DailyScreen.route) })
                Spacer(modifier = Modifier.padding(20.dp))
                DefaultOption(
                    text1 = "DEBTOR",
                    text2 = "RECORD",
                    color = option2,
                    changeRoute = { navController.navigate(Screens.DebtorScreen.route) })
            }
            Spacer(modifier = Modifier.padding(10.dp))
            Row {
                DefaultOption(
                    text1 = "LONE",
                    text2 = "RECORDE",
                    color = option3,
                    changeRoute = { navController.navigate(Screens.LoneScreen.route) })
                Spacer(modifier = Modifier.padding(20.dp))
                DefaultOption(
                    text1 = "PAYMENTS",
                    text2 = "RECORD",
                    color = option4,
                    changeRoute = { navController.navigate(Screens.PaymentsScreen.route + "?debtorId=${-1}&timeStamp=${System.currentTimeMillis()}&debtorName=${null}&loan=${0}") })
            }
            Spacer(modifier = Modifier.padding(10.dp))

            Row {
                DefaultOption(text1 = "GENERATE", text2 = "PDF", color = option5, changeRoute = {})
                Spacer(modifier = Modifier.padding(20.dp))
                DefaultOption(text1 = "BACKUP", text2 = "", color = option6, changeRoute = {})
            }

            Button(
                onClick = { viewModel.csvGenerate(context) },
                colors = ButtonDefaults.buttonColors(contentColor = Color.White),
                shape = RoundedCornerShape(50)
            ) {
                Text("BACK-UP ALL DATA")
            }

        }
    }
}