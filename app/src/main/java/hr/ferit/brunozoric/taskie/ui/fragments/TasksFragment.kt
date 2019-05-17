package hr.ferit.brunozoric.taskie.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.SimpleAdapter
import androidx.core.app.ActivityCompat.recreate
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hr.ferit.brunozoric.taskie.R
import hr.ferit.brunozoric.taskie.common.*
import hr.ferit.brunozoric.taskie.model.Task
import hr.ferit.brunozoric.taskie.persistence.TasksRoomRepository
import hr.ferit.brunozoric.taskie.ui.activities.ContainerActivity
import hr.ferit.brunozoric.taskie.ui.activities.MainActivity
import hr.ferit.brunozoric.taskie.ui.adapters.TaskAdapter
import hr.ferit.brunozoric.taskie.ui.fragments.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_tasks.*

class TasksFragment : BaseFragment(), AddTaskFragmentDialog.TaskAddedListener {

    private val repository = TasksRoomRepository()
    private val adapter by lazy { TaskAdapter {onItemSelected(it)} }

    override fun getLayoutResourceId() = R.layout.fragment_tasks

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        initUi()
        initListeners()
        refreshTasks()
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
                repository.deleteTaskBy(task)
                adapter.removeAt(position)

            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(tasksRecyclerView)
    }

    private fun initListeners() {
        addTask.setOnClickListener { addTask() }
    }

    private fun onItemSelected(task: Task){
        val detailsIntent = Intent(context, ContainerActivity::class.java).apply {
            putExtra(EXTRA_SCREEN_TYPE, ContainerActivity.SCREEN_TASK_DETAILS)
            putExtra(EXTRA_TASK_ID, task.taskDbId)
        }
        startActivity(detailsIntent)
    }

    private fun refreshTasks() {
        progress.gone()
        val data = repository.getTasks()
        if (data.isNotEmpty()) {
            noData.gone()
        } else {
            noData.visible()
        }
        adapter.setData(data)
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
                clearAllTasks()
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