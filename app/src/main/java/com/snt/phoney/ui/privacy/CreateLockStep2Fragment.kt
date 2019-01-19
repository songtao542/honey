package com.snt.phoney.ui.privacy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.snt.phoney.R
import com.snt.phoney.base.AppViewModel
import com.snt.phoney.base.BaseFragment
import com.snt.phoney.extensions.snackbar
import com.snt.phoney.ui.main.MainActivity
import com.snt.phoney.utils.data.Constants
import com.snt.phoney.utils.data.MD5
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.fragment_privacy_create_lock_step1.*

class CreateLockStep2Fragment : BaseFragment() {

    companion object {
        @JvmStatic
        fun newInstance(arguments: Bundle? = null) = CreateLockStep2Fragment().apply {
            this.arguments = arguments
        }

        @JvmStatic
        fun newInstance(pwd: String? = null, mode: Int) = CreateLockStep2Fragment().apply {
            this.arguments = Bundle().apply {
                putString(Constants.Extra.PASSWORD, pwd)
                putInt(Constants.Extra.MODE, mode)
            }
        }
    }

    private lateinit var password: String
    private lateinit var viewModel: AppViewModel

    private var mode: Int = MODE_CREATE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            password = it.getString(Constants.Extra.PASSWORD, "")
            mode = it.getInt(Constants.Extra.MODE, MODE_CREATE)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_privacy_create_lock_step1, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        toolbar.setNavigationOnClickListener { activity?.onBackPressed() }
        titleTextView.setText(R.string.input_password_again_title)
        if (mode == MODE_RESET) {
            setupResetMode()
        } else {
            setupCreateMode()
        }
    }

    private fun setupCreateMode() {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(CreateLockViewModel::class.java)
        divider.visibility = View.GONE
        resetTip.visibility = View.GONE

        confirmButton.setText(R.string.confirm)

        viewModel.success.observe(this, Observer {
            context?.let { context ->
                Toast.makeText(context.applicationContext, R.string.create_privacy_success, Toast.LENGTH_SHORT).show()
            }
            activity?.finish()
        })

        viewModel.error.observe(this, Observer {
            snackbar(it)
        })

        confirmButton.setOnClickListener {
            val pwd = inputPassword.password.toString()
            if (password != pwd) {
                inputPassword.clear()
                snackbar(getString(R.string.password_validate_no_equal))
                return@setOnClickListener
            }
            (viewModel as CreateLockViewModel).setPrivacyPassword(MD5.md5(pwd), MD5.md5(StringBuffer(pwd).reverse().toString()))
        }
    }

    private fun setupResetMode() {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ForgetPasswordViewModel::class.java)
        divider.visibility = View.VISIBLE
        resetTip.visibility = View.VISIBLE

        confirmButton.setText(R.string.confirm_reset)

        viewModel.success.observe(this, Observer {
            context?.let { ctx ->
                Toast.makeText(ctx.applicationContext, R.string.create_privacy_success, Toast.LENGTH_SHORT).show()
                startActivity(MainActivity.newIntent(ctx))
            }
            activity?.finish()
        })

        viewModel.error.observe(this, Observer {
            snackbar(it)
        })

        confirmButton.setOnClickListener {
            val pwd = inputPassword.password.toString()
            if (password != pwd) {
                inputPassword.clear()
                snackbar(getString(R.string.password_validate_no_equal))
                return@setOnClickListener
            }
            (viewModel as ForgetPasswordViewModel).resetPassword(MD5.md5(pwd), MD5.md5(StringBuffer(pwd).reverse().toString()))
        }
    }

}
