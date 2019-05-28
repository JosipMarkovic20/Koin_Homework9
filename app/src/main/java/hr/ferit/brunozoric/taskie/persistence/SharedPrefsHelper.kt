package hr.ferit.brunozoric.taskie.persistence

interface SharedPrefsHelper {

    fun getUserToken(): String

    fun storeUserToken(token: String)

    fun clearUserToken()
}