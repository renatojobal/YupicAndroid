package com.yupic.yupic.model

import com.yupic.yupic.ui.NODE_TYPE_MULTIPLE_CHOICE

data class Node (
    var key : String = "",
    val subtitle: String = "",
    val thumbnail: String? = "",
    var response: Double? = 0.0,
    var factor : Double = 0.0,
    val type : String = "",
    var result : Double = 0.0,
    var options : List<Option>? = null,
    var category: Category? = null
){

    fun calculateResult (): Double{

        if(type == NODE_TYPE_MULTIPLE_CHOICE){

            var optionsResult = 0.0
            this.options?.forEach { option ->
                if(option.selected){
                    optionsResult = option.value
                }
            }
            this.result = this.factor * optionsResult
        }else{
            this.result = this.factor * (this.response ?: 0.0)
        }

        return this.result
    }
}