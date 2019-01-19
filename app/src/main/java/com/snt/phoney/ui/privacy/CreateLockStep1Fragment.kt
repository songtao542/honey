package com.snt.phoney.ui.privacy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.snt.phoney.R
import com.snt.phoney.base.BaseFragment
import com.snt.phoney.extensions.addFragmentSafely
import com.snt.phoney.utils.data.Constants
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.fragment_privacy_create_lock_step1.*

const val MODE_CREATE = 0
const val MODE_RESET = 1

class CreateLockStep1Fragment : BaseFragment() {

    companion object {
        fun newInstance(arguments: Bundle? = null) = CreateLockStep1Fragment().apply {
            this.arguments = arguments
        }
    }

    @Suppress("PrivatePropertyName")
    private val PASSWORD_LENGTH = 4

    private var mode: Int = MODE_CREATE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mode = it.getInt(Constants.Extra.MODE, MODE_CREATE)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_privacy_create_lock_step1, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //viewModel = ViewModelProviders.of(this,viewModelFactory).get(CreateLockViewModel::class.java)
        toolbar.setNavigationOnClickListener { activity?.onBackPressed() }

        if (mode == MODE_RESET) {
            titleTextView.setText(R.string.reset_lock_title)
            resetTip.visibility = View.VISIBLE
            divider.visibility = View.VISIBLE
            //toolbar.navigationIcon = null
        } else {
            titleTextView.setText(R.string.set_lock_title)
            resetTip.visibility = View.GONE
            divider.visibility = View.GONE
        }

        inputPassword.setOnInputOverListener {
            toNextStep()
        }

        confirmButton.setOnClickListener {
            toNextStep()
        }
    }

    private fun toNextStep() {
        val pwd = inputPassword.password.toString()
        if (pwd.length >= PASSWORD_LENGTH) {
            addFragmentSafely(CreateLockStep2Fragment.newInstance(pwd, mode), "step2", true,
                    enterAnimation = R.anim.slide_in_right, popExitAnimation = R.anim.slide_out_right)
        }
    }

}
