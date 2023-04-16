package com.yashishu.bsa.ui.utils

import java.security.Timestamp
import java.text.SimpleDateFormat
import java.util.*

object Time {

    fun timeStamp(): String {

        val timeStamp = Timestamp(System.currentTimeMillis())
        val sdf = SimpleDateFormat("HH:mm")
        val time = sdf.format(Date(timeStamp.time))

        return time.toString()
    }
}
