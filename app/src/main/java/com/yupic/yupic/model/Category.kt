package com.yupic.yupic.model

data class Category(
    var key : String = "",
    var percentage: Double? = 0.0,
    var title: String = "",
    var thumbnail: String? = "",
    var categoryCarbonFootprintKg: Double = 0.0
){

    /**
     * Method that calculates the percentage for the given total
     */
    fun calculatePortion(total : Double) : Double {
        this.percentage = (100 * this.categoryCarbonFootprintKg) / total
        return this.percentage!!
    }

    override fun equals(other: Any?): Boolean {
        if(other is Category){
            return this.key == other.key
        }
        return false
    }

    constructor(hashMap: HashMap<String, *>) : this() {
        this.key = hashMap["key"] as String
        this.percentage = (hashMap["percentage"] as Double?) ?: 0.0
        this.title = hashMap["title"] as String
        this.thumbnail = hashMap["thumbnail"] as String
        this.categoryCarbonFootprintKg = (hashMap["categoryCarbonFootprintKg"] as Double?) ?: 0.0
    }

}