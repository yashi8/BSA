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
    val phone: String = ""
)