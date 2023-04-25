package com.yashishu.bsa.models

import java.util.Date

data class Order(
    val uid: String = "",
    val items: List<CartItem> = listOf(),
    val total: Float = 0f,
    val status: String = "pending",
    val date: Date = Date(),
    var orderId: String = ""
)
