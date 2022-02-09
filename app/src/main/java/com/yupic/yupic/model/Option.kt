package com.yupic.yupic.model

data class Option(
    var key : String = "",
    var title: String = "",
    var value: Double = 0.0,
    var selected: Boolean = false
) {

    constructor(map: HashMap<String, *>) : this() {
        this.title = map["title"] as String
        try {
            this.value = (map["value"] as Long).toDouble()
        }catch (e: java.lang.ClassCastException){
            this.value = map["value"] as Double
        }
        this.selected = map["selected"] as Boolean? ?: false

    }

}
