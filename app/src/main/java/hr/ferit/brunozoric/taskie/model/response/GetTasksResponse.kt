package hr.ferit.brunozoric.taskie.model.response

import hr.ferit.brunozoric.taskie.model.Task


data class GetTasksResponse(val notes: MutableList<Task>? = mutableListOf())