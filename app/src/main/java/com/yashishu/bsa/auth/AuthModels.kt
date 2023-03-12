package com.yashishu.bsa.auth

data class Customer(
    val uid: String = "",
    val name: String? = "",
    val email: String = "",
    val phone: String = ""
)
data class Vendor (
    val uid: String = "",
    val name: String? = "",
    val email: String = "",
    val phone: String = "",
    val lat:Double = 0.0,
    val lng:Double = 0.0
)