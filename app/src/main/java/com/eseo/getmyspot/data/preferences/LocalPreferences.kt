package com.eseo.getmyspot.data.preferences

import android.content.Context
import android.content.SharedPreferences
import android.location.Location
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken


class LocalPreferences private constructor(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE)

    companion object {
        private var INSTANCE: LocalPreferences? = null

        // get localpreferences from other files
        fun getInstance(context: Context): LocalPreferences {
            return INSTANCE?.let {
                INSTANCE
            } ?: run {
                INSTANCE =
                        LocalPreferences(context)
                return INSTANCE!!
            }
        }
    }

    // save float data (for language)
    fun saveStringValue(key: String, yourValue: String) {
        sharedPreferences.edit().putString(key, yourValue).apply()
    }

    // get string data (for language)
    fun getStringStringValue(key: String): String? {
        return sharedPreferences.getString(key, null)
    }
}
