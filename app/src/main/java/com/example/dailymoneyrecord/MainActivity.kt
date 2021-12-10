package com.example.dailymoneyrecord

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.dailymoneyrecord.recorde_Book.presentation.Daily.DailyScreen
import com.example.dailymoneyrecord.recorde_Book.presentation.HomeScreen
import com.example.dailymoneyrecord.recorde_Book.presentation.Lone.DebtorLoanScreen
import com.example.dailymoneyrecord.recorde_Book.presentation.Payments.PaymentScreen
import com.example.dailymoneyrecord.recorde_Book.presentation.theme.DailyMoneyRecordTheme
import com.example.dailymoneyrecord.recorde_Book.presentation.util.DebtorScreen
import com.example.dailymoneyrecord.recorde_Book.presentation.util.Screens
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @ExperimentalMaterialApi
    @ExperimentalComposeUiApi
    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContent {
            val navController = rememberNavController()

            DailyMoneyRecordTheme(darkTheme = false) {

                Column(modifier = Modifier.fillMaxSize()) {
                    NavHost(
                        navController = navController,
                        startDestination = Screens.HomeScreen.route
                    ) {
                        composable(Screens.HomeScreen.route) {
                            HomeScreen(navController = navController)
                        }
                        composable(Screens.DebtorScreen.route) {
                            DebtorScreen()
                        }
                        composable(Screens.DailyScreen.route) {
                            DailyScreen()
                        }
                        composable(Screens.LoneScreen.route) {
                            DebtorLoanScreen(navController = navController)
                        }
                        composable(
                            Screens.PaymentsScreen.route + "?debtorId={debtorId}&timeStamp={timeStamp}&debtorName={debtorName}&loan={loan}",
                            arguments = listOf(
                                navArgument("debtorId") { type = NavType.IntType },
                                navArgument("timeStamp") { type = NavType.LongType },
                                navArgument("debtorName") { type = NavType.StringType },
                                navArgument("loan") { type = NavType.IntType },

                            )
                        ) {
                            var id = it.arguments?.getInt("debtorId")
                            var time_stamp = it.arguments?.getLong("timeStamp")
                            if (id != null && time_stamp != null) {
                                PaymentScreen(debtorID = id, loneDate = time_stamp)
                            }
                        }
                    }

                }

            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
//    DailyMoneyRecordTheme {
//        Greeting("Android")
//    }
//}