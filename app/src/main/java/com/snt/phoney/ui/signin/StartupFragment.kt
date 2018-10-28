package com.snt.phoney.ui.signin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.snt.phoney.R
import com.snt.phoney.base.BaseActivity
import com.snt.phoney.base.BaseFragment
import com.snt.phoney.extensions.addFragmentSafely
import com.snt.phoney.extensions.disposedBy
import com.snt.phoney.ui.signup.SignupActivity
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_startup.*


class StartupFragment : BaseFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.activity_startup, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        signin.setOnClickListener {
            activity?.let {
                it.addFragmentSafely(R.id.containerLayout, SigninFragment.newInstance(), "signin")
            }
        }

        qq.setOnClickListener {
            context?.let {
                startActivity(SignupActivity.newIntent(it))
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = StartupFragment()
    }
}
