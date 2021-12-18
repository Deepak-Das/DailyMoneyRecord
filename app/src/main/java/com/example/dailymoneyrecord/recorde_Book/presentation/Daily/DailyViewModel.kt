package com.example.dailymoneyrecord.recorde_Book.presentation.Daily

import android.content.*
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.FileProvider
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dailymoneyrecord.R
import com.example.dailymoneyrecord.recorde_Book.domain.model.DailyPayment
import com.example.dailymoneyrecord.recorde_Book.domain.model.DebtorPayment
import com.example.dailymoneyrecord.recorde_Book.domain.use_case.DebtorUseCase
import com.example.dailymoneyrecord.recorde_Book.domain.util.OrderBy
import com.example.dailymoneyrecord.recorde_Book.domain.util.OrderType
import com.example.dailymoneyrecord.recorde_Book.domain.util.Status
import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.kernel.colors.ColorConstants
import com.itextpdf.kernel.geom.PageSize
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.*
import com.itextpdf.layout.property.BorderRadius
import com.itextpdf.layout.property.HorizontalAlignment
import com.itextpdf.layout.property.TextAlignment
import com.itextpdf.layout.property.UnitValue
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.SimpleDateFormat
import javax.inject.Inject

@HiltViewModel
class DailyViewModel @Inject constructor(
    private val useCase: DebtorUseCase,
) : ViewModel() {

    private val _dailyState = mutableStateOf(DailyState())
    val dailyState: State<DailyState> = _dailyState

    private var getDebtorJob: Job? = null
    private var getPaysJob: Job? = null
    private var total: Int = 0
    private var getloanJob:Job?=null


    init {
        getDebtor()
        getPays(dailyState.value.dateStamp)

    }

    fun onEvent(event: Event) {
        when (event) {
            is Event.OrderChange -> {
                _dailyState.value = dailyState.value.copy(
                    orderBy = event.orderBy
                )
                getPays(dailyState.value.dateStamp)
            }
            is Event.onInsertPayment -> {
                if (dailyState.value.debtorId != null && dailyState.value.textAmount.isNotEmpty()) {
                    viewModelScope.launch {
                        useCase.insertPayment(
                            DebtorPayment(
                                dailyState.value.paymentId,
                                dailyState.value.debtorId,
                                dailyState.value.textAmount.toInt(),
                                dailyState.value.dateStamp
                            )
                        )
                        setDebId(null)
                        setPayId(null)
                        setName("")
                        setAmount("")
                    }
                }

            }
            is Event.onDelete -> {
                viewModelScope.launch { useCase.deletePayment(event.payment) }
            }
            is Event.DateChange -> {
                _dailyState.value = dailyState.value.copy(
                    dateStamp = event.date
                )
                viewModelScope.launch { getPays(dailyState.value.dateStamp) }
            }
            is Event.Loan -> {
                viewModelScope.launch { useCase.insertLoan(event.debtorLoan) }
            }
        }
    }

    private fun getPays(timeStamp: Long) {
        getPaysJob?.cancel()
        getPaysJob = useCase.getDailyPayments(timeStamp, dailyState.value.orderBy).onEach {
            _dailyState.value = dailyState.value.copy(
                payments = it, dateStamp = timeStamp
            )
            totalAmount(it)

//            Log.i("TAG", "daily : ${dailyState.value.payments.toString()}")
        }.launchIn(viewModelScope)

        getloanJob?.cancel()
        getloanJob=useCase.getLoans(Status.Running(OrderType.Descending)).onEach {
            _dailyState.value = dailyState.value.copy(
                loan_list = it
            )
//            Log.i("TAG", "loan : ${dailyState.value.loan_list.toString()}")

        }.launchIn(viewModelScope)

    }

    private fun totalAmount(pays: List<DailyPayment>) {
        total = 0
        pays.forEach {
            total = total + it.amount
        }
        _dailyState.value = dailyState.value.copy(
            totalAmount = total
        )
    }

    private fun getDebtor() {
        getDebtorJob?.cancel()
        getDebtorJob = useCase.getDebtors(OrderBy.Name(OrderType.Ascending)).onEach {
            _dailyState.value = dailyState.value.copy(
                debtors = it
            )
        }.launchIn(viewModelScope)
    }

    fun setName(name: String) {
        _dailyState.value = dailyState.value.copy(
            textName = name
        )
    }

    fun setAmount(amount: String) {
        if (amount.isDigitsOnly() || amount.isEmpty())
            _dailyState.value = dailyState.value.copy(
                textAmount = amount
            )
    }

    fun setPayId(payId: Int?) {
        _dailyState.value = dailyState.value.copy(
            paymentId = payId
        )
    }

    fun setDebId(debId: Int?) {
        _dailyState.value = dailyState.value.copy(
            debtorId = debId
        )
    }

    fun pdfGenerate(mContext: Context) {

        if (dailyState.value.payments.isEmpty()) {
            Toast.makeText(mContext, "Payments is Empty", Toast.LENGTH_SHORT).show()
            return
        }

        viewModelScope.launch {

            var total_loan=0

            var file_name = "daily pays_${
                SimpleDateFormat("dd-MM-YY").format(dailyState.value.dateStamp)
            }.pdf"

            val path = mContext.getExternalFilesDir(null)!!.absolutePath + "/Daily_PDF"

            val s = Environment.getExternalStorageState()
            Log.i(ContentValues.TAG, "pdfGenerate: $s")

            val dir = File(path)
            if (!dir.exists()) {
                dir.mkdir()
            }
            val file = File(path, file_name)


            val pdfDocument = PdfDocument(PdfWriter(file))
            pdfDocument.defaultPageSize = PageSize.A4

            val document = Document(pdfDocument)


            val bitmap = BitmapFactory.decodeResource(mContext.resources, R.drawable.maa_tera)
            val stream = ByteArrayOutputStream()
            bitmap.compress(
                Bitmap.CompressFormat.JPEG,
                100, stream
            )
            val b: ByteArray = stream.toByteArray()

            val data = ImageDataFactory.create(b)
            val img = Image(data)
            img.scaleAbsolute(150F, 150F)
            img.setHorizontalAlignment(HorizontalAlignment.CENTER)
            img.setBorderRadius(BorderRadius(16F))
            img.setMarginBottom(10F)
            img.strokeWidth = 2F

            document.add(Paragraph("Date :- ${SimpleDateFormat("dd-MM-YY").format(dailyState.value.dateStamp)}").setBold())

            document.add(img)
            val title = "JAI MAA TERA\nDAILY_PAYMENTS"
            document.add(
                Paragraph(title)
                    .setHorizontalAlignment(HorizontalAlignment.CENTER)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER)
            )


            val table =
                Table(
                    UnitValue.createPercentArray(
                        floatArrayOf(
                            5f,
                            15f,
                            15f,
                            12f
                        )
                    )
                ).useAllAvailableWidth()
            table.addHeaderCell(
                Cell().add(
                    Paragraph("Sl no.").setTextAlignment(TextAlignment.CENTER).setBold()
                        .setBackgroundColor(ColorConstants.ORANGE)
                )
            )

            table.addHeaderCell(
                Cell().add(
                    Paragraph("Name").setTextAlignment(TextAlignment.CENTER).setBold()
                        .setBackgroundColor(ColorConstants.ORANGE)
                )
            )
            table.addHeaderCell(
                Cell().add(
                    Paragraph("Date").setTextAlignment(TextAlignment.CENTER).setBold()
                        .setBackgroundColor(ColorConstants.ORANGE)
                )
            )
            table.addHeaderCell(
                Cell().add(
                    Paragraph("Pays_Amount").setTextAlignment(TextAlignment.CENTER).setBold()
                        .setBackgroundColor(ColorConstants.ORANGE)
                )
            )

            var daily_pays: List<DailyPayment> =
                dailyState.value.payments.sortedBy { it.debtorName }

            daily_pays.onEachIndexed { index, entry ->
                table.addCell(
                    Cell().add(
                        Paragraph("${index+1}").setTextAlignment(
                            TextAlignment.CENTER
                        )
                    )
                )
                table.addCell(
                    Cell().add(
                        Paragraph(entry.debtorName).setTextAlignment(
                            TextAlignment.CENTER
                        )
                    )
                )
                table.addCell(
                    Cell().add(
                        Paragraph(SimpleDateFormat("dd-MM-yyyy").format(entry.timeStamp)).setTextAlignment(
                            TextAlignment.CENTER
                        )
                    )
                )
                table.addCell(
                    Cell().add(
                        Paragraph("Rs. ${entry.amount} /-").setTextAlignment(
                            TextAlignment.CENTER
                        )
                    )
                )
            }
            table.addCell(
                Cell().add(
                    Paragraph("Total").setTextAlignment(TextAlignment.CENTER).setBold()
                        .setBackgroundColor(ColorConstants.GREEN)
                )
            )
            table.addCell(
                Cell().add(
                    Paragraph("-").setTextAlignment(TextAlignment.CENTER).setBold()
                        .setBackgroundColor(ColorConstants.GREEN)
                )
            )
            table.addCell(
                Cell().add(
                    Paragraph("-").setTextAlignment(TextAlignment.CENTER).setBold()
                        .setBackgroundColor(ColorConstants.GREEN)
                )
            )
            table.addCell(
                Cell().add(
                    Paragraph("Rs. ${dailyState.value.totalAmount} /-").setTextAlignment(
                        TextAlignment.CENTER
                    ).setBold()
                        .setBackgroundColor(ColorConstants.GREEN)
                )
            )

            document.add(table)

            //TODO:Loan List


            val table_loan = Table(
                UnitValue.createPercentArray(
                    floatArrayOf(
                        5f,
                        15f,
                        15f,
                        12f
                    )
                )
            ).useAllAvailableWidth()

            table_loan.addHeaderCell(
                Cell().add(
                    Paragraph("Sl no.").setTextAlignment(TextAlignment.CENTER).setBold()
                        .setBackgroundColor(ColorConstants.ORANGE)
                )
            )

            table_loan.addHeaderCell(
                Cell().add(
                    Paragraph("Name").setTextAlignment(TextAlignment.CENTER).setBold()
                        .setBackgroundColor(ColorConstants.ORANGE)
                )
            )
            table_loan.addHeaderCell(
                Cell().add(
                    Paragraph("Date").setTextAlignment(TextAlignment.CENTER).setBold()
                        .setBackgroundColor(ColorConstants.ORANGE)
                )
            )
            table_loan.addHeaderCell(
                Cell().add(
                    Paragraph("Loan_Amount").setTextAlignment(TextAlignment.CENTER).setBold()
                        .setBackgroundColor(ColorConstants.ORANGE)
                )
            )



            dailyState.value.loan_list.onEachIndexed { i, it ->
                total_loan+=it.LoneAmount

                table_loan.addCell(
                    Cell().add(
                        Paragraph("${i+1}").setTextAlignment(TextAlignment.CENTER).setBold()

                    )
                )
                table_loan.addCell(
                    Cell().add(
                        Paragraph(it.DebtorName).setTextAlignment(TextAlignment.CENTER).setBold()

                    )
                )

                table_loan.addCell(
                    Cell().add(
                        Paragraph(SimpleDateFormat("dd-MM-yyyy").format(it.timeStamp)).setTextAlignment(
                            TextAlignment.CENTER
                        )
                    )
                )

                table_loan.addCell(
                    Cell().add(
                        Paragraph("Rs. ${it.LoneAmount} /-").setTextAlignment(TextAlignment.CENTER).setBold()

                    )
                )



            }

            table_loan.addCell(
                Cell().add(
                    Paragraph("Total").setTextAlignment(TextAlignment.CENTER).setBold()
                        .setBackgroundColor(ColorConstants.GREEN)
                )
            )
            table_loan.addCell(
                Cell().add(
                    Paragraph("-").setTextAlignment(TextAlignment.CENTER).setBold()
                        .setBackgroundColor(ColorConstants.GREEN)
                )
            )
            table_loan.addCell(
                Cell().add(
                    Paragraph("-").setTextAlignment(TextAlignment.CENTER).setBold()
                        .setBackgroundColor(ColorConstants.GREEN)
                )
            )
            table_loan.addCell(
                Cell().add(
                    Paragraph("Rs. ${total_loan} /-").setTextAlignment(
                        TextAlignment.CENTER
                    ).setBold()
                        .setBackgroundColor(ColorConstants.GREEN)
                )
            )
            document.add(AreaBreak())
            document.add(img)
            document.add(
                Paragraph("LIST OF ALL RUNNING LOAN")
                    .setHorizontalAlignment(HorizontalAlignment.CENTER)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER)
            )

            document.add(table_loan)
            document.close()

            total_loan=0


            Toast.makeText(mContext, "wait pdf in progress...", Toast.LENGTH_SHORT).show()


            val uri = FileProvider.getUriForFile(
                mContext,
                mContext.packageName.toString() + ".provider",
                file
            )
            val i = Intent()
            i.action = Intent.ACTION_VIEW
            i.data = uri
            i.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            i.component = ComponentName("com.adobe.reader", "com.adobe.reader.AdobeReader")
            Log.i(ContentValues.TAG, "pdfGenerate: $uri")
            try {
                mContext.startActivity(i)
            } catch (e: ActivityNotFoundException) {
                Log.i("App_Tag", "Exception - " + e.message)
            }
        }

    }


}