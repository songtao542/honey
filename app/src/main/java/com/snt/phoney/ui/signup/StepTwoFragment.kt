package com.snt.phoney.ui.signup

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.snt.phoney.R
import com.snt.phoney.base.BaseFragment
import com.snt.phoney.domain.model.User
import com.snt.phoney.extensions.addFragmentSafely
import kotlinx.android.synthetic.main.fragment_signup_2.*
import kotlinx.android.synthetic.main.fragment_signup_2.view.*

private const val ARG_SEX = "sex"

class StepTwoFragment : BaseFragment() {

    private var sex: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            sex = it.getInt(ARG_SEX, 0)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_signup_2, container, false)
        when (sex) {
            0 -> {
                view.confirmStep2.setBackgroundResource(R.drawable.button_femail_circle_corner_selector)
            }
            else -> {
                view.confirmStep2.setBackgroundResource(R.drawable.button_mail_circle_corner_selector)
            }
        }
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        back2.setNavigationOnClickListener { activity?.supportFragmentManager?.popBackStack() }
        confirmStep2.setOnClickListener {
            var user = User()
            user.age = 20
            user.height = 175
            user.weight = 60f
            user.sex = sex
            activity?.addFragmentSafely(R.id.containerLayout, StepThreeFragment.newInstance(user), "step3", true)
        }

        heightButton.setOnClickListener {
            PickerFragment.newInstance().show(childFragmentManager, "height_picker")
        }
        ageButton.setOnClickListener {
            PickerFragment.newInstance().show(childFragmentManager, "age_picker")
        }
        weightButton.setOnClickListener {
            PickerFragment.newInstance().show(childFragmentManager, "weight_picker")
        }
    }

    companion object {
        /**
         * argument 0,female; 1,male
         */
        @JvmStatic
        fun newInstance(sex: Int) = StepTwoFragment().apply {
            arguments = Bundle().apply {
                putInt(ARG_SEX, sex)
            }
        }
    }
}
