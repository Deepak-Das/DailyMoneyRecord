package com.example.dailymoneyrecord.recorde_Book.presentation.Lone

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dailymoneyrecord.recorde_Book.domain.model.Loan
import com.example.dailymoneyrecord.recorde_Book.domain.use_case.DebtorUseCase
import com.example.dailymoneyrecord.recorde_Book.domain.util.OrderBy
import com.example.dailymoneyrecord.recorde_Book.domain.util.OrderType
import com.example.dailymoneyrecord.recorde_Book.domain.util.Status
import com.example.dailymoneyrecord.recorde_Book.presentation.Daily.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoneViewModel @Inject constructor(
    private val useCase: DebtorUseCase
) : ViewModel() {

    private val _loneState = mutableStateOf(LoneState())
    val loneState: State<LoneState> = _loneState

    var getLoneJob: Job? = null
    var getDebtorJob: Job? = null

    init {
        getLones(loneState.value.status)

        getDebtorJob = useCase.getDebtors(OrderBy.Name(OrderType.Ascending)).onEach {
            _loneState.value = loneState.value.copy(
                debtors = it
            )
        }.launchIn(viewModelScope)

    }

    fun onEvent(event: Event) {
        when (event) {
            is Event.StatusFilter -> {

                _loneState.value = loneState.value.copy(
                    status = event.Status
                )
                Log.d("status", "onEvent: ${loneState.value.status}")

                if (loneState.value.flagChange) getLones(event.Status) else getLoanByName(
                    loneState.value.id ?: -1,event.Status
                )
            }
            is Event.Loan -> {
                viewModelScope.launch {
                    useCase.insertLoan(event.debtorLoan)
                }
            }
            is Event.LoneByName -> {
                _loneState.value = loneState.value.copy(
                    id = event.ID
                )
                getLoanByName(event.ID ?: -1,loneState.value.status)
            }
            is Event.DateChange -> {
                _loneState.value = loneState.value.copy(
                    dateStamp = event.date
                )
            }
            is Event.AddLoan -> {
                viewModelScope.launch {
                    useCase.insertLoan(event.DebtorLoan)
                }
            }
            is Event.LoanDelete -> {
                viewModelScope.launch {
                    useCase.deleteLoan(event.DebtorLoan)
                }
            }
        }

    }

    private fun getLones(status: Status) {
        _loneState.value = loneState.value.copy(
            flagChange = true
        )
        getLoneJob?.cancel()
        getLoneJob = useCase.getLoans(status).onEach {
            _loneState.value = loneState.value.copy(
                loans = it
            )
            totalLoan(it)

        }.launchIn(viewModelScope)
    }

    private fun getLoanByName(id: Int,status: Status) {
        _loneState.value = loneState.value.copy(
            flagChange = false
        )
        if (id > 0) {
            getLoneJob?.cancel()
            getLoneJob = useCase.getLoansByName(id, status).onEach {
                _loneState.value = loneState.value.copy(
                    loans = it
                )
                totalLoan(it)
            }.launchIn(viewModelScope)
        } else
            getLones(loneState.value.status)
    }

    private fun totalLoan(loanList: List<Loan>) {
        var sum = 0
        loanList.onEach {
            sum = sum + it.LoneAmount
        }
        _loneState.value = loneState.value.copy(
            totalLoan = sum
        )
    }

}