package com.example.dailymoneyrecord.recorde_Book.presentation.Daily

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dailymoneyrecord.recorde_Book.domain.model.DailyPayment
import com.example.dailymoneyrecord.recorde_Book.domain.model.DebtorPayment
import com.example.dailymoneyrecord.recorde_Book.domain.use_case.DebtorUseCase
import com.example.dailymoneyrecord.recorde_Book.domain.util.OrderBy
import com.example.dailymoneyrecord.recorde_Book.domain.util.OrderType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DailyViewModel @Inject constructor(
    private val useCase: DebtorUseCase,
):ViewModel() {

    private val _dailyState = mutableStateOf(DailyState())
    val dailyState:State<DailyState> = _dailyState

    private var getDebtorJob: Job? = null
    private var getPaysJob: Job? = null
    private var total:Int=0


    init {
        getDebtor()
        getPays(dailyState.value.dateStamp)
        Log.d("payments", "DailyScreen: ${dailyState.value.payments}")

    }

    fun onEvent(event:Event){
        when(event){
            is Event.OrderChange->{
                _dailyState.value=dailyState.value.copy(
                    orderBy = event.orderBy
                )
                getPays(dailyState.value.dateStamp)
            }
            is Event.onInsertPayment->{
                if(dailyState.value.debtorId!=null&&dailyState.value.textAmount.isNotEmpty())
                {
                    viewModelScope.launch {
                        useCase.insertPayment(DebtorPayment(
                            dailyState.value.paymentId,
                            dailyState.value.debtorId,
                            dailyState.value.textAmount.toInt(),
                            dailyState.value.dateStamp
                        ))
                    }
                }
                setDebId(null)
                setName("")
                setAmount("")
            }
            is Event.onDelete->{
                viewModelScope.launch { useCase.deletePayment(event.payment) }
            }
            is Event.DateChange->{
                _dailyState.value=dailyState.value.copy(
                    dateStamp = event.date
                )
                viewModelScope.launch { getPays(dailyState.value.dateStamp) }
            }
            is Event.Loan->{
                viewModelScope.launch { useCase.insertLoan(event.debtorLoan) }
            }
        }
    }

    private fun getPays(timeStamp: Long){
        getPaysJob?.cancel()
        getPaysJob =useCase.getDailyPayments(timeStamp,dailyState.value.orderBy).onEach {
            _dailyState.value=dailyState.value.copy(
                payments = it, dateStamp = timeStamp
            )
            totalAmount(it)

        }.launchIn(viewModelScope)

    }

    private fun totalAmount(pays:List<DailyPayment>){
        total=0;
        pays.forEach{
            total=total+it.amount
        }
        _dailyState.value=dailyState.value.copy(
            totalAmount = total
        )
    }

    private fun getDebtor(){
        getDebtorJob?.cancel()
        getDebtorJob = useCase.getDebtors(OrderBy.Name(OrderType.Ascending)).onEach {
            _dailyState.value=dailyState.value.copy(
                debtors = it
            )
        }.launchIn(viewModelScope)
    }

    fun setName(name:String){
        _dailyState.value=dailyState.value.copy(
            textName = name
        )
    }
    fun setAmount(amount:String){
        if (amount.isDigitsOnly()||amount.isEmpty())
        _dailyState.value=dailyState.value.copy(
            textAmount = amount
        )
    }
    fun setPayId(payId:Int?){
        _dailyState.value=dailyState.value.copy(
            paymentId= payId
        )
    }
    fun setDebId(debId: Int?){
        _dailyState.value=dailyState.value.copy(
            debtorId = debId
        )
    }

}