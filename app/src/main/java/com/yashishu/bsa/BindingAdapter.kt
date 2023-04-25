package com.yashishu.bsa.ui.vendor

import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import coil.load
import com.yashishu.bsa.R
import com.yashishu.bsa.models.CartItem
import com.yashishu.bsa.models.Order
import java.lang.Math.round
import java.util.Calendar
import java.util.Date

@BindingAdapter("load")
fun ImageView.loadImage(url: String?) {
    url.let {
        load(it) {
            crossfade(true)
            placeholder(R.drawable.default_image)
            error(R.drawable.error_image)
        }
    }
}

@BindingAdapter("single_item")
fun setSingleItemPrice(tv: TextView, price: String?) {
    // form price string to indian currency
    val priceString = "₹ $price.00"
    tv.text = "$priceString x 1"
}

@BindingAdapter("ten_item")
fun setTenItemPrice(tv: TextView, price: String?) {
    // multiply price by 10
    val priceInt = price?.toFloat()
    val priceString = "₹ ${priceInt?.times(10)}.00"
    tv.text = "$priceString x 10"
}

@BindingAdapter("price_calculated")
fun setPriceCalculated(tv: TextView, price: String?) {
    val priceInt = price?.toFloat()
    //Rs. 88.00 (excluding GST)
    val priceString = "₹ ${priceInt}.00 (excluding GST)"
    tv.text = priceString
}

@BindingAdapter("price_calculated_cart")
fun setPriceCalculatedCart(tv: TextView, cartItem: CartItem) {
    val priceInt = cartItem.price?.toInt()
    val priceString = "₹ ${priceInt?.times(cartItem.qty)}.00"
    tv.text = priceString
}


@BindingAdapter("set_visibility")
fun ProgressBar.setVisibility(isVisible: Boolean) {
    visibility = if (isVisible) {
        ProgressBar.VISIBLE
    } else {
        ProgressBar.GONE
    }
}

@BindingAdapter("set_save_state")
fun ProgressBar.setVisiblity(state: ProductState) {
    visibility = if (state == ProductState.LOADING) {
        ProgressBar.VISIBLE
    } else {
        ProgressBar.GONE
    }
}

@BindingAdapter("display_state")
fun TextView.displayState(size: Int) {
    if (size == 0) {
        visibility = TextView.VISIBLE
    } else {
        visibility = TextView.GONE
    }
}

@BindingAdapter("show_order_text")
fun TextView.showOrderText(order: Order) {
    if (order.status == "Shipped") {
        setTextColor(resources.getColor(R.color.lgreen, null))
    }
    text = "Order # ${order.orderId} (${order.status})"
}

@BindingAdapter("display_date")
fun TextView.displayDate(date: Date) {
    // date format: 2021-05-01 12:00
    val cal = Calendar.getInstance()
    cal.time = date
    val day = cal.get(Calendar.DAY_OF_MONTH)
    val month = cal.get(Calendar.MONTH) + 1
    val year = cal.get(Calendar.YEAR)
    val hour = cal.get(Calendar.HOUR_OF_DAY)
    val minute = cal.get(Calendar.MINUTE)
    val time = "$hour:$minute"
    val date = "$day/$month/$year"
    text = "$date\n$time"
}

@BindingAdapter("dispay_price")
fun TextView.displayPrice(price: Float) {
    text = "₹ ${round(price)}.00"
}

@BindingAdapter("show_price")
fun TextView.showPrice(price: String) {
    text = "₹ $price.00"
}