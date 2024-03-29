package hr.ferit.brunozoric.taskie.ui.taskDetails

import android.view.View
import hr.ferit.brunozoric.taskie.R
import hr.ferit.brunozoric.taskie.common.EXTRA_SCREEN_TYPE
import hr.ferit.brunozoric.taskie.common.EXTRA_TASK_ID
import hr.ferit.brunozoric.taskie.ui.base.BaseActivity
import hr.ferit.brunozoric.taskie.ui.taskDetails.fragment.TaskDetailsFragment
import kotlinx.android.synthetic.main.activity_main.*

class ContainerActivity: BaseActivity() {

    override fun getLayoutResourceId() = R.layout.activity_main

    override fun setUpUi() {
        val screenType = intent.getStringExtra(EXTRA_SCREEN_TYPE)
        val id = intent.getIntExtra(EXTRA_TASK_ID, -1)
        if (screenType.isNotEmpty()) {
            when (screenType) {
                SCREEN_TASK_DETAILS -> showFragment(TaskDetailsFragment.newInstance(id))
            }
        } else {
            finish()
        }
        navigationView.visibility= View.INVISIBLE
    }

    companion object{
        const val SCREEN_TASK_DETAILS = "task_details"
    }
}

