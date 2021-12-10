package com.example.dailymoneyrecord.recorde_Book.domain.util

sealed class OrderBy(val orderType: OrderType) {
    class Id(orderType: OrderType) : OrderBy(orderType)
    class Name(orderType: OrderType) : OrderBy(orderType)
    class TimeStamp(orderType: OrderType) : OrderBy(orderType)


    fun changeOrderType(orderType: OrderType): OrderBy {
        return when (this) {
            is Id -> OrderBy.Id(orderType)
            is Name -> OrderBy.Name(orderType)
            is TimeStamp -> OrderBy.TimeStamp(orderType)
        }
    }
}
