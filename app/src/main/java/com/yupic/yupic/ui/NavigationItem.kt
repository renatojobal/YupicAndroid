package com.yupic.yupic.ui

import com.yupic.yupic.R

sealed class NavigationItem(var route: String, var icon: Int, var title: String){
    object Home : NavigationItem("home", R.drawable.ic_check, "Home")
    object Form : NavigationItem("form", R.drawable.ic_calculate, "Form")
    object Offset : NavigationItem("offset", R.drawable.ic_money, "Offset")


}
