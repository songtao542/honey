package com.snt.phoney.ui.privacy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.snt.phoney.R
import com.snt.phoney.base.BaseFragment
import com.snt.phoney.extensions.addFragmentSafely
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.fragment_create_lock_step1.*

class CreateLockStep1Fragment : BaseFragment() {

    companion object {
        fun newInstance(arguments: Bundle? = null) = CreateLockStep1Fragment().apply {
            this.arguments = arguments
        }
    }

    @Suppress("PrivatePropertyName")
    private val PASSWORD_LENGTH = 4

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_create_lock_step1, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //viewModel = ViewModelProviders.of(this,viewModelFactory).get(CreateLockViewModel::class.java)
        toolbar.setNavigationOnClickListener { activity?.onBackPressed() }
        titleTextView.setText(R.string.set_lock_title)

        inputPassword.setOnInputOverListener {
            toNextStep()
        }

        confirmAgain.setOnClickListener {
            toNextStep()
        }
    }

    private fun toNextStep() {
        val pwd = inputPassword.password.toString()
        if (pwd.length >= PASSWORD_LENGTH) {
            addFragmentSafely(CreateLockStep2Fragment.newInstance(pwd), "step2", true,
                    enterAnimation = R.anim.slide_in_right, popExitAnimation = R.anim.slide_out_right)
        }
    }

}
