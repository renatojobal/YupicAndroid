package com.yupic.yupic.model

data class Node (
    val title: String = "",
    var weight : Double = 0.0,
    val nestedNodes : List<Node> = listOf()

)