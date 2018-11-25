package com.snt.phoney.ui.setup

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.snt.phoney.R
import com.snt.phoney.base.BaseFragment
import com.snt.phoney.domain.model.Sex
import com.snt.phoney.domain.model.User
import com.snt.phoney.extensions.addFragmentSafely
import com.snt.phoney.extensions.disposedBy
import com.snt.phoney.extensions.snackbar
import kotlinx.android.synthetic.main.fragment_signup_1.*

private const val ARG_USER = "user"

class SetupWizardOneFragment : BaseFragment() {

    lateinit var viewModel: SetupWizardViewModel

    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            user = it.getParcelable(ARG_USER) ?: User()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_signup_1, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(SetupWizardViewModel::class.java)

        Log.d("TTTT", "vm=======1==========================$viewModel")

        female.setOnClickListener { setSex(Sex.FEMALE) }
        male.setOnClickListener { setSex(Sex.MALE) }

        viewModel.setupSex.observe(this, Observer {
            activity?.addFragmentSafely(R.id.containerLayout, SetupWizardTwoFragment.newInstance(user), "step2", false)
        })

        viewModel.error.observe(this, Observer {
            snackbar(it)
        })
    }

    private fun setSex(sex: Sex) {
        user.sex = sex.value
        viewModel.setSex(sex.value)?.disposedBy(disposeBag)
    }

    companion object {
        @JvmStatic
        fun newInstance(user: User) = SetupWizardOneFragment().apply {
            arguments = Bundle().apply {
                putParcelable(ARG_USER, user)
            }
        }
    }
}
