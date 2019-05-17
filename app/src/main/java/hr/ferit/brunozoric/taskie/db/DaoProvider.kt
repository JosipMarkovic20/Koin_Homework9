package hr.ferit.brunozoric.taskie.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import hr.ferit.brunozoric.taskie.model.Task

@Database(entities = [Task::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class DaoProvider : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    companion object {
        private var instance: DaoProvider? = null
        fun getInstance(context: Context): DaoProvider {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context. applicationContext,
                    DaoProvider::class.java,
                    "TaskDb"
                ).allowMainThreadQueries().build()
            }
            return instance as DaoProvider
        }
    }
}