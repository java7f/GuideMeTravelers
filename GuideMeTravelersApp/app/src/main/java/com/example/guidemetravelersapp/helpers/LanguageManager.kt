package com.example.guidemetravelersapp.helpers

import android.content.Context
import android.content.SharedPreferences
import com.example.guidemetravelersapp.R
import java.util.*

class LanguageManager (context: Context) {
    private var prefs: SharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)

    companion object {
        const val CURRENT_LANGUAGE = "current_language"
    }

    /**
     * Function to save a change in the apps language
     * @param locale the locale of the new language
     */
    fun updateLanguage(locale: String) {
        val editor = prefs.edit()
        editor.putString(CURRENT_LANGUAGE, locale)
        editor.apply()
    }

    /**
     * Function to get the current app locale
     * @return String with the current locale
     */
    fun getCurrentLanguage(): Locale {
        val languageCode = prefs.getString(CURRENT_LANGUAGE, null)
        return Locale(languageCode!!)
    }
}