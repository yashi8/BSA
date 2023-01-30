package com.yashishu.bsa

import android.content.Context
import android.content.SharedPreferences

class PrefUtil(private val context: Context, name: String = "settings") {
    private lateinit var prefs: SharedPreferences
    init {
        prefs = context.getSharedPreferences(name, Context.MODE_PRIVATE)
    }

    fun getUserType() = prefs.getInt(USER_TYPE, 0)
    fun setUserType(usrType: Int = 0) {
        prefs.edit().putInt(USER_TYPE, usrType).apply()
    }

    companion object {
        const val USER_TYPE = "usertype"
    }
}