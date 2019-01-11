package com.snt.phoney.ui.privacy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.snt.phoney.R
import kotlinx.android.synthetic.main.app_toolbar.*

/**
 */
class ForgetLockFragment : Fragment() {

    companion object {
        @JvmStatic
        fun newInstance(arguments: Bundle? = null) = ForgetLockFragment().apply {
            this.arguments = arguments
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_privacy_forget_lock_password, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        toolbar.setNavigationOnClickListener { activity?.onBackPressed() }
        titleTextView.setText(R.string.reset_lock_title)

        toolbar.navigationIcon = null
    }

}
