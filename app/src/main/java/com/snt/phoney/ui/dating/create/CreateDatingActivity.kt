package com.snt.phoney.ui.dating.create

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.snt.phoney.R
import com.snt.phoney.base.BaseActivity
import com.snt.phoney.extensions.colorOf
import com.snt.phoney.extensions.setStatusBarColor

class CreateDatingActivity : BaseActivity() {
    companion object {
        fun newIntent(context: Context): Intent = Intent(context, CreateDatingActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_dating)
        setStatusBarColor(colorOf(android.R.color.white))
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, CreateDatingFragment.newInstance())
                    .commitNow()
        }
    }

}
