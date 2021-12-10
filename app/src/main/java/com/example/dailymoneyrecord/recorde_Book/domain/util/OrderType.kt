package com.example.dailymoneyrecord.recorde_Book.domain.util

sealed class OrderType(){
    object Ascending:OrderType()
    object Descending:OrderType()
}
