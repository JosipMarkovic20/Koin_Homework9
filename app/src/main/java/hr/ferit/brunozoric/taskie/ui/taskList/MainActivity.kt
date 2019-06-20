package hr.ferit.brunozoric.taskie.ui.taskList

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.appcompat.app.AlertDialog
import hr.ferit.brunozoric.taskie.R
import hr.ferit.brunozoric.taskie.ui.base.BaseActivity
import hr.ferit.brunozoric.taskie.ui.pager.PagerFragment
import hr.ferit.brunozoric.taskie.ui.taskList.fragment.TasksFragment
import kotlinx.android.synthetic.main.activity_main.*




class MainActivity : BaseActivity() {

    override fun getLayoutResourceId() = R.layout.activity_main

    override fun setUpUi() {
        showFragment(TasksFragment.newInstance())
        selectFragment()
        if(!isConnected()) {
            noInternetDialog()
        }
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

    private fun noInternetDialog() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.noInternet))
            .setMessage(getString(R.string.noInternetDescription))
            .setNeutralButton(getString(R.string.ok),null)
            .show()
    }

    private fun isConnected(): Boolean {
        val connectivityManager = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        val isConnected: Boolean = activeNetwork?.isConnected == true
        return isConnected
    }

}