package hr.ferit.brunozoric.taskie.persistence

import hr.ferit.brunozoric.taskie.app.TaskieApp
import hr.ferit.brunozoric.taskie.db.DaoProvider
import hr.ferit.brunozoric.taskie.db.TaskDao
import hr.ferit.brunozoric.taskie.model.Priority
import hr.ferit.brunozoric.taskie.model.Task

class TasksRoomRepository : TasksRepository {
    private var db: DaoProvider = DaoProvider.getInstance(TaskieApp.getAppContext())
    private var taskDao: TaskDao = db.taskDao()
    override fun addTask(task: Task) {
        taskDao.insertTask(task)
    }

    override fun getTasks(): MutableList<Task> {
        return taskDao.loadAll()
    }

    override fun getTaskBy(id: Int): Task {
        return taskDao.getTask(id)
    }

    override fun deleteTaskBy(task: Task) {
        taskDao.deleteTask(task)
    }

    override fun clearAllTasks() {
        taskDao.deleteAllTasks()
    }

    override fun editTask(task: Task, newTitle: String, newDescription: String, newPriority: Priority) {
        taskDao.changeTaskTitle(task.taskDbId!!,newTitle)
        taskDao.changeTaskDescription(task.taskDbId!!, newDescription)
        taskDao.changeTaskPriority(task.taskDbId!!,newPriority)
    }
}