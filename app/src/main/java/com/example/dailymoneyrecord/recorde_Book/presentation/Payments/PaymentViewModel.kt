package com.example.dailymoneyrecord.recorde_Book.presentation.Payments


import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.FileProvider
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dailymoneyrecord.R
import com.example.dailymoneyrecord.recorde_Book.domain.model.DailyPayment
import com.example.dailymoneyrecord.recorde_Book.domain.use_case.DebtorUseCase
import com.example.dailymoneyrecord.recorde_Book.domain.util.OrderBy
import com.example.dailymoneyrecord.recorde_Book.domain.util.OrderType
import com.example.dailymoneyrecord.recorde_Book.presentation.Daily.Event
import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.kernel.colors.ColorConstants
import com.itextpdf.kernel.geom.PageSize
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Cell
import com.itextpdf.layout.element.Image
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.property.BorderRadius
import com.itextpdf.layout.property.HorizontalAlignment
import com.itextpdf.layout.property.TextAlignment
import com.itextpdf.layout.property.UnitValue
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File
import java.sql.Date
import java.text.SimpleDateFormat
import javax.inject.Inject


@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val useCase: DebtorUseCase,
    savedStateHandle: SavedStateHandle

) : ViewModel() {

    val _paymentState = mutableStateOf(PaymentState())
    val paymentState: State<PaymentState> = _paymentState

    var getPaysJob: Job? = null
    var getDebtorsJob: Job? = null

    var flag: Boolean = false
    var name: String? = null
    var Amount: Int? = null


    init {
        savedStateHandle.get<Int>("debtorId")?.let { setId(it) }
        savedStateHandle.get<Long>("timeStamp")?.let { setloan(it) }
        savedStateHandle.get<Int>("loan")?.let { Amount = it }
        savedStateHandle.get<String>("debtorName")?.let { name = it }
        getPays(paymentState.value.orderBy)
        useCase.getDebtors(OrderBy.Name(OrderType.Ascending)).onEach {
            _paymentState.value = paymentState.value.copy(
                debtorList = it
            )
        }.launchIn(viewModelScope)

    }

    fun onEvent(event: Event) {
        when (event) {
            is Event.OrderChange -> {
                if (event.orderBy !== paymentState.value.orderBy ||
                    event.orderBy.orderType !== paymentState.value.orderBy.orderType
                ) {
                    _paymentState.value = paymentState.value.copy(
                        orderBy = event.orderBy
                    )
                    if (flag) getPaysNameLimit() else getPays(event.orderBy)
                }
            }
            is Event.Limit -> {
                flag = true
                getPaysNameLimit(event.id, event.start, event.end)
            }

        }
    }


    private fun getPays(orderBy: OrderBy) {
        getPaysJob?.cancel()
        getPaysJob = useCase.getPaymentsByIdAndTime(
            paymentState.value.debtorId,
            paymentState.value.loanDate,
            orderBy
        ).onEach {
            _paymentState.value = paymentState.value.copy(
                paymentList = it
            )
            totalPays(it)
        }.launchIn(viewModelScope)

    }

    private fun getPaysNameLimit(
        id: Int = paymentState.value.debtorId,
        start: Int = 0,
        end: Int = 60
    ) {
        name = null
        Amount = null
        getPaysJob?.cancel()
        getPaysJob = useCase.getPaymentsById(
            id,
            start,
            end,
            paymentState.value.orderBy
        ).onEach {
            _paymentState.value = paymentState.value.copy(
                paymentList = it
            )
            totalPays(it)
        }.launchIn(viewModelScope)


    }

    var sum = 0

    fun totalPays(list: List<DailyPayment>) {
        sum = 0
        list.onEach {
            sum += it.amount
        }
        _paymentState.value = paymentState.value.copy(
            totalAmount = sum
        )
    }

    fun setId(id: Int) {
        _paymentState.value = paymentState.value.copy(
            debtorId = id
        )
    }

    fun setloan(date: Long) {
        _paymentState.value = paymentState.value.copy(
            loanDate = date
        )
    }

    fun pdfGenerate(mContext: Context) {

        if (paymentState.value.paymentList.isEmpty()) {
            Toast.makeText(mContext, "PayList is Empty", Toast.LENGTH_SHORT).show()
            return
        }

        viewModelScope.launch {


            var file_name = "${paymentState.value.paymentList[0].debtorName}_${
                SimpleDateFormat("dd-MM-YY").format(Date(System.currentTimeMillis()))
            }.pdf"

            val path = mContext.getExternalFilesDir(null)!!.absolutePath + "/Pays_PDF"


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

            document.add(img)
            val title = "JAI MAA TERA"
            document.add(
                Paragraph(title)
                    .setHorizontalAlignment(HorizontalAlignment.CENTER)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER)
            )


            if (Amount !== null && name != null) {
                var namePDf = Paragraph(name)
                var amoutPDF = Paragraph("Rs. $Amount /-")
                document.add(namePDf.setBold())
                document.add(amoutPDF.setBold())
            } else {
                var namepdf2 = paymentState.value.paymentList[0].debtorName
                document.add(Paragraph(namepdf2).setBold())
            }


            val table =
                Table(
                    UnitValue.createPercentArray(
                        floatArrayOf(
                            15f,
                            15f,
                            12f
                        )
                    )
                ).useAllAvailableWidth()

            table.addHeaderCell(
                Cell().add(
                    Paragraph("SL NO.").setTextAlignment(TextAlignment.CENTER).setBold()
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

            paymentState.value.paymentList.onEachIndexed { index, entry ->
                table.addCell(
                    Cell().add(
                        Paragraph((index + 1).toString()).setTextAlignment(
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
                    Paragraph(paymentState.value.totalAmount.toString()).setTextAlignment(
                        TextAlignment.CENTER
                    ).setBold()
                        .setBackgroundColor(ColorConstants.GREEN)
                )
            )

            document.add(table)
            document.close()


            val uri = FileProvider.getUriForFile(
                mContext,
                mContext.packageName.toString() + ".provider",
                file
            )
            val i = Intent()
            i.action = Intent.ACTION_VIEW
            i.data = uri
//           i.type="application/pdf"
            i.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            i.component = ComponentName("com.adobe.reader", "com.adobe.reader.AdobeReader")
            Log.i(TAG, "pdfGenerate: $uri")
            try {
                mContext.startActivity(i)
            } catch (e: ActivityNotFoundException) {
                Log.i("App_Tag", "Exception - " + e.message)
            }
        }

    }


}