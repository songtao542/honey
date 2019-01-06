package com.snt.phoney.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.snt.phoney.di.Injectable
import com.umeng.analytics.MobclickAgent
import javax.inject.Inject

abstract class BaseFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    //override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    //    super.onViewCreated(view, savedInstanceState)
    //    ViewCompat.requestApplyInsets(view)
    //}

    fun enableOptionsMenu(toolbar: Toolbar, showTitle: Boolean = true, menu: Int = 0) {
        toolbar?.let { toolbar ->
            activity?.let { activity ->
                setHasOptionsMenu(true)
                if (activity is AppCompatActivity) {
                    activity.setSupportActionBar(toolbar)
                    if (menu != 0) {
                        toolbar.inflateMenu(menu)
                    }
                    activity.supportActionBar?.setDisplayShowTitleEnabled(showTitle)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        MobclickAgent.onPageStart(javaClass.simpleName)
    }

    override fun onPause() {
        super.onPause()
        MobclickAgent.onPageEnd(javaClass.simpleName)
    }

    companion object {
        inline fun <reified T : Fragment> newInstance(arguments: Bundle? = null) = T::class.java.newInstance().apply {
            this.arguments = arguments
        }
    }
}