package hr.ferit.brunozoric.taskie.persistence

import hr.ferit.brunozoric.taskie.model.Priority
import hr.ferit.brunozoric.taskie.model.Task

interface TasksRepository {

    fun addTask(task: Task)
    fun getTasks(): List<Task>
    fun getTaskBy(id: Int): Task
    fun deleteTaskBy(task: Task)
    fun clearAllTasks()
    fun editTask(task: Task, newTitle: String, newDescription: String, newPriority: Priority)

}