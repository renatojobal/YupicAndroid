package com.yupic.yupic.model

data class Donation(
    var amount: Double = 0.0,
    val project: Project,
    val user: User
)
