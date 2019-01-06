package com.snt.phoney.ui.password

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.snt.phoney.R
import com.snt.phoney.base.BaseFragment
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.fragment_forget_password.*

/**
 *
 */
class ForgetPasswordFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_forget_password, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        toolbar.setNavigationOnClickListener { activity?.onBackPressed() }
        titleTextView.setText(R.string.forget_password_title)

        uploadConfirm.setOnClickListener {
            uploadLayout.visibility = View.GONE
            successLayout.visibility = View.VISIBLE
        }

        backHome.setOnClickListener {
            activity?.let { it.supportFragmentManager.popBackStack() }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onDetach() {
        super.onDetach()
    }


    companion object {
        @JvmStatic
        fun newInstance() = ForgetPasswordFragment()
    }
}
