package com.snt.phoney.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.snt.phoney.extensions.ClearableCompositeDisposable
import com.snt.phoney.extensions.autoCleared
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

abstract class NoInjectBaseFragment : Fragment(), CoroutineScope {

    override val coroutineContext = Job() + Dispatchers.Main

    private var clearableDisposeBag: ClearableCompositeDisposable by autoCleared(ClearableCompositeDisposable(CompositeDisposable()))

    protected val disposeBag: CompositeDisposable
        get() = clearableDisposeBag.compositeDisposable


//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        //ViewCompat.requestApplyInsets(view)
//    }

    fun enableOptionsMenu(toolbar: Toolbar, showTitle: Boolean = true) {
        toolbar?.let { toolbar ->
            activity?.let { activity ->
                setHasOptionsMenu(true)
                if (activity is AppCompatActivity) {
                    activity.setSupportActionBar(toolbar)
                    activity.supportActionBar?.setDisplayShowTitleEnabled(showTitle)
                }
            }
        }
    }

    companion object {
        inline fun <reified T : Fragment> newInstance(arguments: Bundle? = null) = T::class.java.newInstance().apply {
            this.arguments = arguments
        }
    }
}