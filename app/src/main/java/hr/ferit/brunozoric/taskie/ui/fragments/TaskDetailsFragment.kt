package hr.ferit.brunozoric.taskie.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat.recreate
import hr.ferit.brunozoric.taskie.R
import hr.ferit.brunozoric.taskie.common.EXTRA_TASK_ID
import hr.ferit.brunozoric.taskie.common.displayToast
import hr.ferit.brunozoric.taskie.common.gone
import hr.ferit.brunozoric.taskie.common.visible
import hr.ferit.brunozoric.taskie.model.Task
import hr.ferit.brunozoric.taskie.persistence.TasksRoomRepository
import hr.ferit.brunozoric.taskie.ui.fragments.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_dialog_edit_task.*
import kotlinx.android.synthetic.main.fragment_task_details.*
import kotlinx.android.synthetic.main.fragment_tasks.*



class TaskDetailsFragment : BaseFragment(), EditTaskFragmentDialog.TaskEditedListener {

    private val repository = TasksRoomRepository()
    private var taskID = NO_TASK


    override fun getLayoutResourceId(): Int {
        return R.layout.fragment_task_details
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getInt(EXTRA_TASK_ID)?.let { taskID = it }
        tryDisplayTask(taskID)
        initListeners()
    }


    private fun tryDisplayTask(id: Int) {
        try {
            val task = repository.getTaskBy(id)
            displayTask(task)
        } catch (e: NoSuchElementException) {
            context?.displayToast(getString(R.string.noTaskFound))
        }
    }

    private fun initListeners() {
        editTask.setOnClickListener { editTask() }
    }

    private fun editTask() {
        val dialog = EditTaskFragmentDialog.newInstance(taskID)
        dialog.setTaskEditedListener(this)
        dialog.show(childFragmentManager, dialog.tag)

    }

    override fun onTaskEdited(task: Task) {
        refreshTask()
    }

    private fun refreshTask() {
        tryDisplayTask(taskID)
    }

    private fun displayTask(task: Task) {
        detailsTaskTitle.text = task.title
        detailsTaskDescription.text = task.description
        detailsPriorityView.setBackgroundResource(task.priority.getColor())
    }

    companion object {
        const val NO_TASK = -1

        fun newInstance(taskId: Int): TaskDetailsFragment {
            val bundle = Bundle().apply { putInt(EXTRA_TASK_ID, taskId) }
            return TaskDetailsFragment().apply { arguments = bundle }
        }
    }
}
