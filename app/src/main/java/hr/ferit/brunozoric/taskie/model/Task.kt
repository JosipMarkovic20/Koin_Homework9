package hr.ferit.brunozoric.taskie.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Task(
    @PrimaryKey(autoGenerate = true)
    var taskDbId: Int? = null,
    val title: String,
    val description: String,
    val priority: Priority
)