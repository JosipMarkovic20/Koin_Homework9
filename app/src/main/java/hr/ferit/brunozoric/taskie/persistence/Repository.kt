package hr.ferit.brunozoric.taskie.persistence

import hr.ferit.brunozoric.taskie.model.Priority
import hr.ferit.brunozoric.taskie.model.Task

// object Repository{
//
// private val tasks = mutableListOf<Task>()
// private var currentId: Long = 0
//
// public fun save(title: String, description: String, priority: Priority): Task {
// val task = Task(currentId, title, description, priority)
// task.taskDbId = currentId
// tasks.add(task)
// currentId++
// return task
// }
//
// fun deleteBy(id: Int){
// tasks.removeAll { (it.taskDbId) == id }
// }
//
// fun count() = tasks.size
//
// fun get(id: Int): Task {
// return tasks.first { it.id == id }
// }
//
// fun getAllTasks() = tasks
// }