package com.example.dailymoneyrecord.recorde_Book.presentation.util

import android.content.Context
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.viewModelScope
import com.example.dailymoneyrecord.recorde_Book.domain.use_case.DebtorUseCase
import com.example.dailymoneyrecord.recorde_Book.domain.util.OrderBy
import com.example.dailymoneyrecord.recorde_Book.domain.util.OrderType
import com.example.dailymoneyrecord.recorde_Book.domain.util.Status
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.job
import java.io.File
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext


class BackupData @Inject constructor(
    private val useCase: DebtorUseCase
) {

    companion object{
        var pathCsvDir:String = ""
        var job:Job?=null
    }

    operator fun invoke(mContext:Context){
        pathCsvDir=mContext.getExternalFilesDir(null)!!.absolutePath+"Pays_CSV"
        val dir= File(pathCsvDir)
        if (!dir.exists()){
            dir.mkdir()
        }

    }

    fun loanCSV(){
       val LoanList= listOf(listOf("")).toMutableList()


    }
}