package hr.ferit.brunozoric.taskie.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.SimpleAdapter
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat.recreate
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hr.ferit.brunozoric.taskie.R
import hr.ferit.brunozoric.taskie.common.*
import hr.ferit.brunozoric.taskie.model.Task
import hr.ferit.brunozoric.taskie.model.request.DeleteTaskRequest
import hr.ferit.brunozoric.taskie.model.response.GetTasksResponse
import hr.ferit.brunozoric.taskie.networking.BackendFactory
import hr.ferit.brunozoric.taskie.persistence.TasksRoomRepository
import hr.ferit.brunozoric.taskie.ui.activities.ContainerActivity
import hr.ferit.brunozoric.taskie.ui.activities.MainActivity
import hr.ferit.brunozoric.taskie.ui.adapters.TaskAdapter
import hr.ferit.brunozoric.taskie.ui.fragments.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_tasks.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TasksFragment : BaseFragment(), AddTaskFragmentDialog.TaskAddedListener {

    private val repository = TasksRoomRepository()
    private val adapter by lazy { TaskAdapter {onItemSelected(it)} }
    private val interactor = BackendFactory.getTaskieInteractor()

    override fun getLayoutResourceId() = R.layout.fragment_tasks

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        initUi()
        initListeners()
        getAllTasks()
    }


    private fun initUi() {
        progress.visible()
        noData.visible()
        tasksRecyclerView.layoutManager = LinearLayoutManager(context)
        tasksRecyclerView.adapter = adapter

        val swipeHandler = object : SwipeToDeleteCallback(context!!.applicationContext) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val task = adapter.getTaskAtPosition(position)
                showDeleteTaskDialog(task, position)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(tasksRecyclerView)
    }

    private fun initListeners() {
        addTask.setOnClickListener { addTask() }
    }

    private fun getAllTasks() {
        progress.visible()
        interactor.getTasks(getTaskieCallback())
    }

    private fun getTaskieCallback(): Callback<GetTasksResponse> = object : Callback<GetTasksResponse> {
        override fun onFailure(call: Call<GetTasksResponse>?, t: Throwable?) {
            progress.gone()
            //TODO : handle default error
        }

        override fun onResponse(call: Call<GetTasksResponse>?, response: Response<GetTasksResponse>) {
            progress.gone()
            noData.gone()
            if (response.isSuccessful) {
                when (response.code()) {
                    RESPONSE_OK -> handleOkResponse(response)
                    else -> handleSomethingWentWrong()
                }
            }
        }
    }

    private fun handleOkResponse(response: Response<GetTasksResponse>) {
        response.body()?.notes?.run {
            repository.clearAllTasks()
            this.forEach{
                repository.addTask(it)
            }
            adapter.setData(this)
        }
    }

    private fun handleSomethingWentWrong() = this.activity?.displayToast("Something went wrong!")

    private fun showDeleteTaskDialog(task: Task, position: Int){
        AlertDialog.Builder(context!!)
            .setTitle(getString(R.string.delete_task_dialog))
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setPositiveButton(android.R.string.yes) { _, _ ->
                repository.deleteTaskBy(task)
                adapter.removeAt(position) }
            .setNegativeButton(android.R.string.no, null).show()
        refreshTasks()
    }



    private fun showClearAllTasksDialog(){
        AlertDialog.Builder(context!!)
            .setTitle(getString(R.string.delete_all_tasks_dialog))
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setPositiveButton(android.R.string.yes) { _, _ -> clearAllTasks()}
            .setNegativeButton(android.R.string.no, null).show()
        refreshTasks()
    }

    private fun onItemSelected(task: Task){
        val detailsIntent = Intent(context, ContainerActivity::class.java).apply {
            putExtra(EXTRA_SCREEN_TYPE, ContainerActivity.SCREEN_TASK_DETAILS)
            putExtra(EXTRA_TASK_ID, task.taskDbId)
        }
        startActivity(detailsIntent)
    }

    private fun refreshTasks() {
        getAllTasks()
    }

    private fun addTask() {
        val dialog = AddTaskFragmentDialog.newInstance()
        dialog.setTaskAddedListener(this)
        dialog.show(childFragmentManager, dialog.tag)
    }

    override fun onTaskAdded(task: Task) {
        refreshTasks()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.sorting_and_clearing_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.sortMenuItem -> {
                sortByPriority()
            }
            R.id.deleteMenuItem -> {
                showClearAllTasksDialog()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun sortByPriority(){
        progress.gone()
        val data = repository.sortTasks()
        if (data.isNotEmpty()) {
            noData.gone()
        } else {
            noData.visible()
        }
        adapter.setData(data.toMutableList())
    }

    private fun clearAllTasks(){
        progress.gone()
        repository.clearAllTasks()
        val data: MutableList<Task> = mutableListOf()
        adapter.setData(data)
        noData.visible()
    }

    companion object {
        fun newInstance(): Fragment {
            return TasksFragment()
        }
    }
}