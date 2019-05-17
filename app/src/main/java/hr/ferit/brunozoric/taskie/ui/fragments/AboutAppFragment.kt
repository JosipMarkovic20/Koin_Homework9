package hr.ferit.brunozoric.taskie.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import hr.ferit.brunozoric.taskie.R
import hr.ferit.brunozoric.taskie.ui.adapters.TaskPagerAdapter
import kotlinx.android.synthetic.main.fragment_about_app.*
import kotlinx.android.synthetic.main.fragment_about_me.*


class AboutAppFragment: Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val result = inflater.inflate(R.layout.fragment_about_app, container, false)
        return result}


    companion object {
        fun newInstance(): Fragment {
            return AboutAppFragment()
        }
    }
}