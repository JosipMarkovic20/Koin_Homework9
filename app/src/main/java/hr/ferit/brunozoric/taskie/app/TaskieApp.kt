package hr.ferit.brunozoric.taskie.app

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import hr.ferit.brunozoric.taskie.persistence.PREFERENCES_NAME

class TaskieApp : Application() {

    companion object {
        private lateinit var instance: TaskieApp

        fun getAppContext(): Context = instance.applicationContext

        fun providePreferences(): SharedPreferences = TaskieApp.instance.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
    }

    override fun onCreate() {
        instance = this
        super.onCreate()
    }



}