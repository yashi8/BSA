package com.yashishu.bsa.ui.vendor

import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import coil.load
import com.yashishu.bsa.R

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
    val priceInt = price?.toInt()
    val priceString = "₹ ${priceInt?.times(10)}.00"
    tv.text = "$priceString x 10"
}

@BindingAdapter("price_calculated")
fun setPriceCalculated(tv: TextView, price: String?) {
    val priceInt = price?.toInt()
    //Rs. 88.00 (excluding GST)
    val priceString = "₹ ${priceInt}.00 (excluding GST)"
    tv.text = priceString
}

@BindingAdapter("price_calculated_cart")
fun setPriceCalculatedCart(tv: TextView, price: Int?) {
    val priceInt = price?.toInt()
    val priceString = "₹ ${priceInt}.00"
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