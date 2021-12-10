package com.example.dailymoneyrecord.recorde_Book.presentation.util

sealed class Screens(val route:String){
    object HomeScreen:Screens("home_screen")
    object LoneScreen:Screens("lone_screen")
    object DebtorScreen:Screens("debtor_screen")
    object PaymentsScreen:Screens("payments_screen")
    object DailyScreen:Screens("daily_screen")
}

