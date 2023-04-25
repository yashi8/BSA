package com.yashishu.bsa.ui.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

object Time {

    fun timeStamp(): String {

        val timeStamp = Calendar.getInstance().time
        val sdf = SimpleDateFormat("HH:mm")
        val time = sdf.format(Date(timeStamp.time))

        return time.toString()
    }
}
