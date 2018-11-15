package com.snt.phoney.ui.setup

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.snt.phoney.R
import com.snt.phoney.base.BaseActivity
import com.snt.phoney.domain.model.User
import com.snt.phoney.extensions.addFragmentSafely
import com.snt.phoney.extensions.colorOf
import com.snt.phoney.extensions.setStatusBarColor
import java.lang.IllegalArgumentException

const val EXTRA_USER = "user"

class SetupWizardActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        setStatusBarColor(colorOf(android.R.color.white))
        val user = intent?.getParcelableExtra<User>(EXTRA_USER)
                ?: throw IllegalArgumentException("Must have a user to set up")
        var step = if (user.sex == -1) 1 else 2
        when (step) {
            1 -> addFragmentSafely(R.id.containerLayout, SetupWizardOneFragment.newInstance(user), "step1", false)
            2 -> addFragmentSafely(R.id.containerLayout, SetupWizardTwoFragment.newInstance(user), "step2", false)
            3 -> addFragmentSafely(R.id.containerLayout, SetupWizardThreeFragment.newInstance(user), "step3", false)
        }
    }

    companion object {
        fun newIntent(context: Context, user: User): Intent {
            return Intent(context, SetupWizardActivity::class.java).apply {
                putExtra(EXTRA_USER, user)
            }
        }
    }
}
