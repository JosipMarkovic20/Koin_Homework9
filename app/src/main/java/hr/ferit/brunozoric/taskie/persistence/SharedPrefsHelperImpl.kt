package hr.ferit.brunozoric.taskie.persistence


import hr.ferit.brunozoric.taskie.app.TaskieApp

const val KEY_USER_TOKEN = "user_token"
const val PREFERENCES_NAME = "taskie_prefs"

class SharedPrefsHelperImpl : SharedPrefsHelper {

    private val preferences = TaskieApp.providePreferences()

    // API authentication token
    override fun getUserToken(): String = preferences.getString(KEY_USER_TOKEN, "")

    override fun storeUserToken(token: String) = preferences.edit().putString(KEY_USER_TOKEN, token).apply()

    override fun clearUserToken() = preferences.edit().remove(KEY_USER_TOKEN).apply()
}

fun provideSharedPrefs(): SharedPrefsHelper {
    return SharedPrefsHelperImpl()
}