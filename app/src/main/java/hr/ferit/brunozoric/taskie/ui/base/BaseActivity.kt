package hr.ferit.brunozoric.taskie.ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import hr.ferit.brunozoric.taskie.R
import hr.ferit.brunozoric.taskie.common.showFragment


abstract class BaseActivity: AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutResourceId())
        setUpUi()

    }

    protected fun showFragment(fragment: Fragment){
        showFragment(R.id.navigationContainer, fragment)
    }

    abstract fun getLayoutResourceId(): Int
    abstract fun setUpUi()






}