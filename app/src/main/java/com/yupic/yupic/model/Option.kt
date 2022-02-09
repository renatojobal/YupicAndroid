package com.yupic.yupic.model

data class Option(
    var title: String = "",
    var value: Double = 0.0,
    var selected: Boolean = false
) {

    constructor(map: HashMap<String, *>) : this() {
        this.title = map["title"] as String
        this.value = map["value"] as Double
        this.selected = map["selected"] as Boolean? ?: false

    }

}
