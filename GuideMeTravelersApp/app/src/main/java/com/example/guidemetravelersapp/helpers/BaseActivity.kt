package com.example.guidemetravelersapp.helpers

import android.content.Context
import android.content.ContextWrapper
import android.preference.PreferenceManager
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity

open class BaseActivity: ComponentActivity() {
    override fun attachBaseContext(newBase: Context) {
        // get chosen language from shread preference
        val localeToSwitchTo = LanguageManager(newBase).getCurrentLanguage()
        val localeUpdatedContext: ContextWrapper = ContextHelper.updateLocale(newBase, localeToSwitchTo)
        super.attachBaseContext(localeUpdatedContext)
    }
}