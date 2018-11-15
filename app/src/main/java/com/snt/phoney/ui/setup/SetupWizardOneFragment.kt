package com.snt.phoney.ui.setup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.snt.phoney.R
import com.snt.phoney.base.BaseFragment
import com.snt.phoney.extensions.addFragmentSafely
import com.snt.phoney.extensions.disposedBy
import com.snt.phoney.extensions.snackbar
import kotlinx.android.synthetic.main.fragment_signup_1.*


class SetupWizardOneFragment : BaseFragment() {

    lateinit var viewModel: SetupWizardViewModel

    var userSex: Int = -1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_signup_1, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(SetupWizardViewModel::class.java)

        female.setOnClickListener { setSex(1) }
        male.setOnClickListener { setSex(0) }

        viewModel.setupSex.observe(this, Observer {
            activity?.addFragmentSafely(R.id.containerLayout, SetupWizardTwoFragment.newInstance(userSex), "step2", true)
        })

        viewModel.error.observe(this, Observer {
            snackbar(it)
        })
    }

    private fun setSex(sex: Int) {
        userSex = sex
        viewModel.setSex(sex)?.disposedBy(disposeBag)
    }

    companion object {
        @JvmStatic
        fun newInstance() = SetupWizardOneFragment()
    }
}
