package com.snt.phoney.ui.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.snt.phoney.R
import com.snt.phoney.base.BaseFragment
import com.snt.phoney.extensions.addFragmentSafely
import kotlinx.android.synthetic.main.fragment_signup_1.*


class StepOneFragment : BaseFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_signup_1, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        female.setOnClickListener { toStep2(0) }
        male.setOnClickListener { toStep2(1) }

    }

    private fun toStep2(sex: Int) {
        activity?.addFragmentSafely(R.id.containerLayout, StepTwoFragment.newInstance(sex), "step2", true)
    }

    companion object {
        @JvmStatic
        fun newInstance() = StepOneFragment()
    }
}
