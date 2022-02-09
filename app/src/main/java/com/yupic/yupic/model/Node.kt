package com.yupic.yupic.model

data class Node (
    val title: String = "",
    val subtitle: String = "",
    val thumbnail: String? = "",
    var response: Double? = 0.0,
    var factor : Double = 0.0,
    val type : String = "",
    var result : Double = 0.0,
    var options : List<Option>? = null
)