package com.example.dailymoneyrecord.recorde_Book.domain.util

sealed class Status(val orderType:OrderType){
    class Running(orderType: OrderType) :Status(orderType)
    class Paid(orderType: OrderType) :Status(orderType)
    class All(orderType: OrderType) :Status(orderType)

    fun copy(orderType: OrderType):Status{
        return when(this){
                is Status.Running-> Running(orderType)
                is Status.Paid->Paid(orderType)
                is Status.All->All(orderType)

        }
    }
}
