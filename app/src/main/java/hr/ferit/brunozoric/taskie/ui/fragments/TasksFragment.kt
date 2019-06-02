package hr.ferit.brunozoric.taskie.ui.fragments

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hr.ferit.brunozoric.taskie.R
import hr.ferit.brunozoric.taskie.common.*
import hr.ferit.brunozoric.taskie.model.Task
import hr.ferit.brunozoric.taskie.model.response.DeleteTaskResponse
import hr.ferit.brunozoric.taskie.model.response.GetTasksResponse
import hr.ferit.brunozoric.taskie.networking.BackendFactory
import hr.ferit.brunozoric.taskie.persistence.TasksRoomRepository
import hr.ferit.brunozoric.taskie.ui.activities.ContainerActivity
import hr.ferit.brunozoric.taskie.ui.adapters.TaskAdapter
import hr.ferit.brunozoric.taskie.ui.fragments.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_tasks.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout



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
        swipeToRefresh()
    }

    private fun initUi() {
        tasksRecyclerView.layoutManager = LinearLayoutManager(context)
        tasksRecyclerView.adapter = adapter

        val swipeHandler = object : SwipeToDeleteCallback(context!!.applicationContext) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val task = adapter.getTaskAtPosition(position)
                showDeleteTaskDialog(task, position)
                refreshTasks()
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(tasksRecyclerView)

        getAllTasks()

    }

    private fun initListeners() {
        addTask.setOnClickListener { addTask() }
    }

    private fun getAllTasks() {
        if(isConnected()){
            progress.visible()
            interactor.getTasks(getTaskieCallback())
        }else{
            adapter.setData(repository.getTasks())
        }
    }



    private fun swipeToRefresh(){
        val pullToRefresh = taskSwipeRefresh
        pullToRefresh.setOnRefreshListener {
            getAllTasks()
            pullToRefresh.setRefreshing(false)
        }
    }

    private fun isConnected(): Boolean {
        val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        val isConnected: Boolean = activeNetwork?.isConnected == true
        return isConnected
    }

    private fun getTaskieCallback(): Callback<GetTasksResponse> = object : Callback<GetTasksResponse> {
        override fun onFailure(call: Call<GetTasksResponse>?, t: Throwable?) {
            //TODO : handle default error
        }

        override fun onResponse(call: Call<GetTasksResponse>?, response: Response<GetTasksResponse>) {
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
            onTasksReceived(this)
        }
    }

    private fun onTasksReceived(tasks: MutableList<Task>){
        repository.clearAllTasks()
        tasks.forEach {
            repository.addTask(it)
        }
        val data = repository.getTasks()
        adapter.setData(data)
        if(data.isEmpty()){
            noData?.visible()
        }else{
            noData?.gone()
        }
        progress?.gone()
    }

    private fun handleSomethingWentWrong() {
        val data = repository.getTasks()
        if(data.isEmpty()){
            noData?.visible()
        }else{
            noData?.gone()
        }
        this.activity?.displayToast("Something went wrong!")
    }

    private fun deleteTask(request: String){
        progress.visible()
        interactor.deleteTask(request, deleteTaskCallback())
        val data = repository.getTasks()
        if(data.isEmpty()){
            noData?.visible()
        }else{
            noData?.gone()
        }
    }

    private fun deleteTaskCallback(): Callback<DeleteTaskResponse> = object : Callback<DeleteTaskResponse> {
        override fun onFailure(call: Call<DeleteTaskResponse>?, t: Throwable?) {
            progress.gone()
            //TODO : handle default error
        }

        override fun onResponse(call: Call<DeleteTaskResponse>?, response: Response<DeleteTaskResponse>) {
            progress.gone()
            if (response.isSuccessful) {
                when (response.code()) {
                    RESPONSE_OK -> handleOkDeleteResponse()
                    else -> handleSomethingWentWrong()
                }
            }
        }
    }

    private fun handleOkDeleteResponse(){
        this.activity?.displayToast("Task successfully deleted")
    }

    private fun showDeleteTaskDialog(task: Task, position: Int){
        AlertDialog.Builder(context!!)
            .setTitle(getString(R.string.delete_task_dialog))
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setPositiveButton(android.R.string.yes) { _, _ ->
                deleteTask(task.id)
                repository.deleteTaskBy(task)
                adapter.removeAt(position)
            }
            .setNegativeButton(android.R.string.no){ _,_->refreshTasks()}.show()
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
        val data = repository.sortTasks()
        if (data.isNotEmpty()) {
            noData.gone()
        } else {
            noData.visible()
        }
        adapter.setData(data.toMutableList())
    }

    private fun clearAllTasks(){
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