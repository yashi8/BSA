package com.yashishu.bsa.models

data class CartItem(
    val product: Product = Product(),
    val uid: String = "",
    val qty: Int = 1,
    val price: Int = 0,
    val timestamp: Long = System.currentTimeMillis(),
) {
    fun incQty() = copy(qty = qty + 1)
    fun decQty() = copy(qty = qty - 1)
}
