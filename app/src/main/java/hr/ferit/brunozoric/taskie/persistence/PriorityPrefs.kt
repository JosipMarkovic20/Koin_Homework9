package hr.ferit.brunozoric.taskie.persistence

import android.preference.PreferenceManager
import hr.ferit.brunozoric.taskie.Taskie
import hr.ferit.brunozoric.taskie.app.TaskieApp

object PriorityPrefs {

    const val KEY_PRIORITY = "KEY_PRIORITY"

    private fun sharedPrefs() =
        PreferenceManager.getDefaultSharedPreferences(TaskieApp.getAppContext())

    fun store(key: String, value: String) {
        val editor = sharedPrefs().edit()
        editor.putString(key , value).apply()
    }

    fun getString(key: String, defaultValue: String ): String? =
        sharedPrefs().getString(key ,defaultValue )
}