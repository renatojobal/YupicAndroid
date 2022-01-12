package com.yupic.yupic.ui

import com.yupic.yupic.R

sealed class BottomNavigationItem(var route: String, var icon: Int, var title: String){
    object Home : BottomNavigationItem("home", R.drawable.ic_check, "Home")
    object Form : BottomNavigationItem("form", R.drawable.ic_calculate, "Form")
    object Offset : BottomNavigationItem("offset", R.drawable.ic_money, "Offset")


}
