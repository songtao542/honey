package com.snt.phoney.ui.setup

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.snt.phoney.R
import com.snt.phoney.base.BaseActivity
import com.snt.phoney.extensions.addFragmentSafely
import com.snt.phoney.extensions.colorOf
import com.snt.phoney.extensions.setStatusBarColor

class SetupWizardActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        setStatusBarColor(colorOf(android.R.color.white))
        //setContentFragment(R.id.containerLayout) { SetupWizardFragment.newInstance() }
        addFragmentSafely(R.id.containerLayout, SetupWizardOneFragment.newInstance(), "step1", false)
    }

    companion object {
        fun newIntent(context: Context): Intent = Intent(context, SetupWizardActivity::class.java)
    }
}
