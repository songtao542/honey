package com.snt.phoney.ui.setup

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.snt.phoney.R
import com.snt.phoney.base.BaseActivity
import com.snt.phoney.domain.model.User
import com.snt.phoney.extensions.addFragmentSafely
import com.snt.phoney.extensions.colorOf
import com.snt.phoney.extensions.setStatusBarColor

const val EXTRA_USER = "user"

class SetupWizardActivity : BaseActivity() {

    override fun onConfigureTheme(): Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        setStatusBarColor(colorOf(android.R.color.white))
        val user = intent?.getParcelableExtra<User>(EXTRA_USER)
                ?: throw IllegalArgumentException("Must have a user to set up")
        when (computeStep(user)) {
            1 -> addFragmentSafely(R.id.containerLayout, SetupWizardOneFragment.newInstance(user), "step1", false)
            2 -> addFragmentSafely(R.id.containerLayout, SetupWizardTwoFragment.newInstance(user), "step2", false)
            3 -> addFragmentSafely(R.id.containerLayout, SetupWizardThreeFragment.newInstance(user), "step3", false)
        }
    }

    private fun computeStep(user: User): Int {
        Log.d("TTTT", "set up wizard sex=${user.sex}")
        return if (user.sex == -1) 1 else if (user.sex == 1) {
            if (user.height == 0 || user.age == 0 || user.cup == null) 2 else 3
        } else {
            if (user.height == 0 || user.age == 0 || user.weight == 0.0) 2 else 3
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
