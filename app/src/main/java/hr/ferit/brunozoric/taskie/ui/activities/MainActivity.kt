package hr.ferit.brunozoric.taskie.ui.activities

import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import hr.ferit.brunozoric.taskie.R
import hr.ferit.brunozoric.taskie.persistence.TasksRoomRepository
import hr.ferit.brunozoric.taskie.ui.activities.base.BaseActivity
import hr.ferit.brunozoric.taskie.ui.adapters.TaskAdapter
import hr.ferit.brunozoric.taskie.ui.fragments.PagerFragment
import hr.ferit.brunozoric.taskie.ui.fragments.TasksFragment
import kotlinx.android.synthetic.main.activity_main.*




class MainActivity : BaseActivity() {

    override fun getLayoutResourceId() = R.layout.activity_main

    override fun setUpUi() {
        showFragment(TasksFragment.newInstance())
        selectFragment()

    }


    private fun selectFragment(){
        navigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.tasks_navigation -> {
                    val fragment = TasksFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.navigationContainer, fragment, fragment.javaClass.getSimpleName())
                        .commit()
                    true
                }
                R.id.about_navigation ->{
                    val fragment = PagerFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.navigationContainer, fragment, fragment.javaClass.getSimpleName())
                        .commit()
                    true
                }else -> false
            }
        }
    }


    override fun onRestart() {
        super.onRestart()
        recreate()
    }





}