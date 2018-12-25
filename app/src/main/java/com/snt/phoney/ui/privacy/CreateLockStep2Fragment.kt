package com.snt.phoney.ui.privacy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.snt.phoney.R
import com.snt.phoney.base.BaseFragment
import com.snt.phoney.extensions.snackbar
import com.snt.phoney.utils.data.Constants
import com.snt.phoney.utils.data.MD5
import kotlinx.android.synthetic.main.fragment_create_lock_step1.*

class CreateLockStep2Fragment : BaseFragment() {

    companion object {
        @JvmStatic
        fun newInstance(arguments: Bundle? = null) = CreateLockStep2Fragment().apply {
            this.arguments = arguments
        }

        @JvmStatic
        fun newInstance(pwd: String? = null) = CreateLockStep2Fragment().apply {
            this.arguments = Bundle().apply {
                putString(Constants.Extra.PASSWORD, pwd)
            }
        }
    }

    private lateinit var password: String
    private lateinit var viewModel: CreateLockViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            password = it.getString(Constants.Extra.PASSWORD, "")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_create_lock_step1, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(CreateLockViewModel::class.java)
        toolbar.setNavigationOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }
        toolbar.setTitle(R.string.input_password_again_title)
        confirmAgain.setText(R.string.confirm)

        viewModel.success.observe(this, Observer {
            snackbar(getString(R.string.create_privacy_success))
            activity?.finish()
        })

        viewModel.error.observe(this, Observer {
            snackbar(it)
        })

        confirmAgain.setOnClickListener {
            val pwd = inputPassword.password.toString()
            if (password != pwd) {
                snackbar(getString(R.string.password_validate_no_equal))
                return@setOnClickListener
            }
            viewModel.setPrivacyPassword(MD5.md5(pwd), MD5.md5(StringBuffer(pwd).reverse().toString()))
        }
    }

}
