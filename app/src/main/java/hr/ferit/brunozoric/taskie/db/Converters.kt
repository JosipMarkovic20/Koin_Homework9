package hr.ferit.brunozoric.taskie.db

import androidx.room.TypeConverter
import hr.ferit.brunozoric.taskie.model.Priority
import java.time.Instant

class Converters {
    companion object {
        @TypeConverter
        @JvmStatic
        fun fromPriority(priority: Priority): String {
            return priority.toString()
        }

        @TypeConverter
        @JvmStatic
        fun toPriority(priority: String): Priority {
            return Priority.valueOf(priority)
        }
    }
}