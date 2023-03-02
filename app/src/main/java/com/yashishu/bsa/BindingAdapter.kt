package com.yashishu.bsa.ui.vendor

import android.widget.ImageView
import android.widget.ProgressBar
import androidx.databinding.BindingAdapter
import coil.load

@BindingAdapter("load")
fun ImageView.loadImage(url: String?) {
    url.let {
        load(it)
    }
}

@BindingAdapter("set_visibility")
fun ProgressBar.setVisibility(isVisible: Boolean) {
    visibility = if (isVisible) {
        ProgressBar.VISIBLE
    } else {
        ProgressBar.GONE
    }
}