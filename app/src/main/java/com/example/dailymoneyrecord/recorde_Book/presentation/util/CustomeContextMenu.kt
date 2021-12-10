package com.example.dailymoneyrecord.recorde_Book.presentation.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Money
import androidx.compose.ui.graphics.vector.ImageVector

sealed class CustomeContextMenu(val label:String, val icon: ImageVector){

    object DeleteMenu:CustomeContextMenu("Delete", Icons.Default.Delete)
    object EditMenu:CustomeContextMenu("Edit", Icons.Default.Edit)
    object LoneMenu:CustomeContextMenu("Edit", Icons.Default.Money)
    object PaymentsMenu:CustomeContextMenu("Edit", Icons.Default.AttachMoney)

}
