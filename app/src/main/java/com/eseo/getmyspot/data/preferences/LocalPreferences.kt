package com.eseo.getmyspot.data.preferences

import android.content.Context
import android.content.SharedPreferences
import android.location.Location
import androidx.appcompat.app.AppCompatDelegate
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken


class LocalPreferences private constructor(context: Context) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE)

    companion object {
        const val THEME = "theme"
        const val LANGUAGE = "language"
        const val PSEUDO = "pseudo"

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

    // set theme value
    fun setThemeValue(yourValue: Int) {
        sharedPreferences.edit().putInt(THEME, yourValue).apply()
    }

    // get theme value
    fun getThemeValue(): Int {
        return sharedPreferences.getInt(THEME, AppCompatDelegate.MODE_NIGHT_NO)
    }

    // save float data (for language)
    fun saveStringValue(key: String, yourValue: String) {
        sharedPreferences.edit().putString(key, yourValue).apply()
    }

    // get string data (for language)
    fun getStringValue(key: String): String? {
        return sharedPreferences.getString(key, null)
    }
}
