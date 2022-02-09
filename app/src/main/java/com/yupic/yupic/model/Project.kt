package com.yupic.yupic.model

data class Project(
    val name : String? = "",
    val description: String? = "",
    var thumbnail : String? = null,
    var kgPerDollar : Double = 0.0
)
