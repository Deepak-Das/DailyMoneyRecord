package com.example.dailymoneyrecord.recorde_Book.presentation.Debtor

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dailymoneyrecord.recorde_Book.domain.model.Debtor
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
class DebtorViewModel @Inject constructor(
    private val debtorUseCase: DebtorUseCase,
) : ViewModel() {

    private val _debtorState = mutableStateOf(DebtorState())
    val debtorState: State<DebtorState> = _debtorState
    private var getDebtorJob: Job? = null

    init {
        getDebtors(OrderBy.Id(OrderType.Descending))
    }

    fun insert(debtor:Debtor){
        if(!debtor.name.contains(regex = Regex(" $")))
        viewModelScope.launch {
            debtorUseCase.insertDebtor(debtor)
        }
    }

    fun delete(debtor: Debtor){
        viewModelScope.launch {
            debtorUseCase.deleteDebtor(debtor)
        }
    }

    fun onOrderClick(orderBy: OrderBy){
        getDebtors(orderBy)
    }

    private fun getDebtors(orderBy: OrderBy) {
        getDebtorJob?.cancel()
        getDebtorJob = debtorUseCase.getDebtors(orderBy).onEach {
            _debtorState.value=debtorState.value.copy(
                it,orderBy
            )
        }.launchIn(viewModelScope)

    }

}