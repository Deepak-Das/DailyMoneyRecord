package com.example.dailymoneyrecord.recorde_Book.presentation.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavigationItem(var route:String, var icon: ImageVector, var title:String){
    object Home:NavigationItem("home", Icons.Default.Home,"Home")
}
