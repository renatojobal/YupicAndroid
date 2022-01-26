package com.yupic.yupic.model

data class Project(
    val name : String,
    val description: String,
    var thumnail : String? = null,
    var kgPerDollar : Float = 0f
)
