package com.yashishu.bsa.models

data class Product(
    var title:String="",
    var desc:String="",
    val img_url:String = "",
    val vid:String = "",
    var category:String = "",
    var price:String=""
)
