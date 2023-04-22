package com.yashishu.bsa.models

data class CartItem(
    val product: Product = Product(),
    val uid: String = "",
    val qty: Int = 1,
    val price: Float = 0f,
    val timestamp: Long = System.currentTimeMillis(),
    val is_paid: Boolean = false,
) {
    fun incQty() = copy(qty = qty + 1)
    fun decQty() = copy(qty = qty - 1)
}
