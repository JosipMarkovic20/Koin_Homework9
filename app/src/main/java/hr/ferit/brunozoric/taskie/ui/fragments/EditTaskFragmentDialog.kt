package hr.ferit.brunozoric.taskie.ui.fragments

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.RadioButton
import androidx.fragment.app.DialogFragment
import hr.ferit.brunozoric.taskie.R
import hr.ferit.brunozoric.taskie.common.displayToast
import hr.ferit.brunozoric.taskie.model.Priority
import hr.ferit.brunozoric.taskie.model.Task
import hr.ferit.brunozoric.taskie.persistence.PriorityPrefs
import hr.ferit.brunozoric.taskie.persistence.TasksRoomRepository
import kotlinx.android.synthetic.main.fragment_dialog_edit_task.*
import kotlinx.android.synthetic.main.fragment_dialog_new_task.*
import kotlinx.android.synthetic.main.fragment_task_details.*


class EditTaskFragmentDialog : DialogFragment() {

    private var repository = TasksRoomRepository()
    private var taskEditedListener: TaskEditedListener? = null
    lateinit var task: Task


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.FragmentDialogTheme)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dialog_edit_task, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
        initUI()
    }

    private fun initUI() {

        context?.let {
            editPrioritySelector.adapter = ArrayAdapter<Priority>(it, android.R.layout.simple_spinner_dropdown_item, Priority.values())
            val selection = when(getCurrentPriority()){
                "LOW" -> 0
                "MEDIUM" -> 1
                "HIGH" -> 2
                else -> 0
            }
            editPrioritySelector.setSelection(selection)
        }
    }

    private fun getCurrentPriority (): String? {
        return PriorityPrefs.getString(PriorityPrefs. KEY_PRIORITY, "")
    }

    private fun savePriority(priority: String) {
        PriorityPrefs.store(PriorityPrefs. KEY_PRIORITY, priority)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
    }

    private fun initListeners() {
        saveEditTaskAction.setOnClickListener {

            editTask()
        }
    }

    private fun isInputEmpty(): Boolean = TextUtils.isEmpty(editTaskTitleInput.text) || TextUtils.isEmpty(
        editTaskDescriptionInput.text
    )

    private fun editTask() {
        if (isInputEmpty()){
            context?.displayToast(getString(R.string.emptyFields))
            return
        }

        val title = editTaskTitleInput.text.toString()
        val description = editTaskDescriptionInput.text.toString()
        val priority = editPrioritySelector.selectedItem as Priority
        val task = Task(title = title, description = description, priority = priority)

        repository.editTask(task)

        savePriority(priority.toString())

        clearUi()

        taskEditedListener?.onTaskEdited()
        dismiss()
    }

    fun setTaskEditedListener(taskEditedListener: TaskEditedListener) {
        this.taskEditedListener = taskEditedListener
    }

    private fun clearUi() {
        editTaskTitleInput.text.clear()
        editTaskDescriptionInput.text.clear()
        editPrioritySelector.setSelection(0)
    }

    companion object {
        fun newInstance() = EditTaskFragmentDialog()
    }

}