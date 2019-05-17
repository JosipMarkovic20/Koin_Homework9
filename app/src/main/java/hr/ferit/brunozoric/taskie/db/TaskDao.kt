package hr.ferit.brunozoric.taskie.db

import androidx.room.*
import androidx.room.OnConflictStrategy.IGNORE
import androidx.room.OnConflictStrategy.REPLACE
import hr.ferit.brunozoric.taskie.model.Priority
import hr.ferit.brunozoric.taskie.model.Task

@Dao
interface TaskDao {

    @Query("SELECT * FROM TASK" )
    fun loadAll(): MutableList<Task>

    @Query("SELECT * FROM TASK WHERE taskDbId = :taskId" )
    fun getTask(taskId: Int): Task

    @Insert(onConflict = IGNORE)
    fun insertTask(task: Task): Long

    @Update(onConflict = REPLACE)
    fun updateTask(task: Task)

    @Delete
    fun deleteTask(task: Task)

    @Query("DELETE FROM Task" )
    fun deleteAllTasks()

    @Query("UPDATE task SET description = :taskDescription WHERE taskDbId = :taskId " )
    fun changeTaskDescription (taskId: Int, taskDescription: String)

    @Query("UPDATE task SET title = :newTitle WHERE taskDbId = :taskId")
    fun changeTaskTitle(taskId: Int, newTitle: String)

    @Query("UPDATE task SET priority = :newPriority WHERE taskDbId = :taskId")
    fun changeTaskPriority(taskId: Int, newPriority: Priority)

    @Query("SELECT * FROM task ORDER BY priority")
    fun getTasksOrderedByPriority(): List<Task>
}