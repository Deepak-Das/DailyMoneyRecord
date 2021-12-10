package com.example.dailymoneyrecord.recorde_Book.presentation.util

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.dailymoneyrecord.recorde_Book.domain.util.OrderBy
import com.example.dailymoneyrecord.recorde_Book.domain.util.OrderType
import com.example.dailymoneyrecord.recorde_Book.presentation.theme.radioSelected
import com.example.dailymoneyrecord.recorde_Book.presentation.theme.radioUnselected

@Composable
fun DefaultRadioButton(
    label: String,
    selected:Boolean,
    onSelected: ()->Unit
) {
    Row (verticalAlignment = Alignment.CenterVertically){

        RadioButton(
            selected = selected,
            colors = RadioButtonDefaults.colors(
                selectedColor = radioSelected,
                unselectedColor = radioSelected

            ),
            onClick =  onSelected,
        )
        Spacer(modifier = Modifier.width(1.dp))
        Text(text= label)
    }


}

@Preview(showBackground = true)
@Composable
fun radio() {
//    DefaultRadioButton(label = "ID",selected = true)
}