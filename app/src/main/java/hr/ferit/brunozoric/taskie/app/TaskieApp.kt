package hr.ferit.brunozoric.taskie.app

import android.app.Application
import android.content.Context

class TaskieApp : Application() {

    companion object {
        private lateinit var instance: TaskieApp

        fun getAppContext(): Context = instance.applicationContext
    }

    override fun onCreate() {
        instance = this
        super.onCreate()
    }

}