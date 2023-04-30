package com.yashishu.bsa.models

data class Product(
    var title: String = "",
    var desc: String = "",
    val img_url: String = "",
    val vid: String = "",
    var category: String = "",
    var price: String = "",
    var lat: Double = 0.0,
    var lng: Double = 0.0,
    var address: String = ""
) {

}
