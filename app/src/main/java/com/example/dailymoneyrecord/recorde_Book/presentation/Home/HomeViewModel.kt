package com.example.dailymoneyrecord.recorde_Book.presentation.Home

import android.content.ActivityNotFoundException
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dailymoneyrecord.recorde_Book.domain.use_case.DebtorUseCase
import com.example.dailymoneyrecord.recorde_Book.domain.util.OrderType
import com.example.dailymoneyrecord.recorde_Book.domain.util.Status
import com.example.dailymoneyrecord.recorde_Book.presentation.Home.component.HomeState
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.io.File
import java.sql.Date
import java.text.SimpleDateFormat
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val useCase: DebtorUseCase,
) : ViewModel() {

    var pathCSV = ""

    private val _homeState = mutableStateOf(HomeState())
    val homesate: State<HomeState> = _homeState

    var job: Job? = null
    var job2: Job? = null

    init {
        getLoan()
        getPays()
    }

    fun getLoan() {
        job?.cancel()
        job = useCase.getLoans(Status.All(OrderType.Ascending)).onEach {
            _homeState.value = homesate.value.copy(
                listloans = it
            )
            Log.i(TAG, "getLoan: ${homesate.value.listloans}")
        }.launchIn(viewModelScope)
    }

    fun getPays() {
        job2?.cancel()
        job2 = useCase.getAllPayments().onEach {
            _homeState.value = homesate.value.copy(
                listpay = it
            )
        }.launchIn(viewModelScope)
    }


    fun csvGenerate(mContext: Context) {
        viewModelScope.launch {
            _homeState.value = homesate.value.copy(
                loanList = mutableListOf(),
                paysList = mutableListOf()
            )

            var file_name = "Backup_"+SimpleDateFormat("dd-MM-yy").format(Date(System.currentTimeMillis()))
                .toString() + ".csv"

            pathCSV = mContext.getExternalFilesDir(null)!!.absolutePath + "/Backup_CSV"
            val dir = File(pathCSV)
            if (!dir.exists()) {
                dir.mkdir()
            }
            homesate.value.listloans.onEachIndexed { index, it ->
                homesate.value.loanList.add(
                    listOf(
                        (index+1).toString(),
                        it.DebtorName,
                        it.LoneAmount.toString(),
                        SimpleDateFormat("dd-MM-yyyy").format(it.timeStamp),
                        it.status
                    )
                )
            }
//            Log.i(TAG, "csvLoan: ${homesate.value.listloans.toString()}")
            val file = File(pathCSV, file_name)

            csvWriter().open(file) {
                writeRow("", "", "LOAN_RECORD", "", "")
                writeRow("")
                writeRow(listOf("SL NO.", "Name", "Loan_Amount", "Date", "Status"))
                writeRows(homesate.value.loanList)
                writeRow("")
            }

            homesate.value.listpay.onEachIndexed { index, it ->
                homesate.value.paysList.add(
                    listOf(
                        (index+1).toString(),
                        it.debtorName,
                        it.amount.toString(),
                        SimpleDateFormat("dd-MM-yyyy").format(it.timeStamp)
                    )
                )
//                Log.i(TAG, "csvGenerate: ${homesate.value.paysList[0].toString()}")
            }

//            Log.i(TAG, "csvGenerate: ${homesate.value.listpay}")


            csvWriter().open(file, true) {
                writeRow("", "", "PAYMENTS_RECORD", "", "")
                writeRow("")
                writeRow(listOf("SL NO.", "Name", "Pay_Amount", "Date"))
                writeRows(homesate.value.paysList)
            }


            Toast.makeText(mContext, "Successful write", Toast.LENGTH_SHORT).show()


            val intent = Intent(Intent.ACTION_VIEW);
            intent.data = FileProvider.getUriForFile(
                mContext,
                mContext.packageName.toString() + ".provider",
                file
            )
            intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            delay(500)
            try {
                mContext.startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                Log.i("App_Tag", "Exception - " + e.message)
            }


        }
    }

}
