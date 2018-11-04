package com.snt.phoney.ui.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.snt.phoney.R
import com.snt.phoney.base.BaseFragment
import com.snt.phoney.domain.model.User
import kotlinx.android.synthetic.main.fragment_signup_3.*
import kotlinx.android.synthetic.main.fragment_signup_3.view.*

private const val ARG_USER = "user"

class StepThreeFragment : BaseFragment() {

    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            user = it.getParcelable(ARG_USER)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_signup_3, container, false)
        when (user.sex) {
            0 -> {
                view.confirmStep3.setBackgroundResource(R.drawable.button_femail_circle_corner_selector)
            }
            else -> {
                view.confirmStep3.setBackgroundResource(R.drawable.button_mail_circle_corner_selector)
            }
        }
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        back3.setNavigationOnClickListener { activity?.supportFragmentManager?.popBackStack() }
        confirmStep3.setOnClickListener {
            context?.let {
                BindPhoneFragment.newInstance().show(childFragmentManager, "bindPhone")
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(user: User) = StepThreeFragment().apply {
            arguments = Bundle().apply {
                putParcelable(ARG_USER, user)
            }
        }
    }
}
