package com.snt.phoney.ui.setup

import android.os.Bundle
import android.text.TextUtils
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
import com.snt.phoney.extensions.snackbar
import com.snt.phoney.utils.Picker
import kotlinx.android.synthetic.main.fragment_signup_2.*
import kotlinx.android.synthetic.main.fragment_signup_2.view.*

private const val ARG_USER = "user"

class SetupWizardTwoFragment : BaseFragment() {

    lateinit var viewModel: SetupWizardViewModel

    private lateinit var user: User
    private lateinit var cupNumberArray: Array<Int>
    private lateinit var cupSizeArray: Array<String>

    private var cupNumber: Int = 0
    private var cupSize: Int = 0

    private fun initCupArray() {
        cupNumberArray = Array(11) { 30 + it }
        cupSizeArray = arrayOf("A", "B", "C", "D", "E", "F", "G", "H")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initCupArray()
        arguments?.let {
            user = it.getParcelable(ARG_USER) ?: User()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_signup_2, container, false)
        when (Sex.from(user.sex)) {
            Sex.MALE -> {
                view.cupButton.visibility = View.GONE
                view.weightButton.visibility = View.VISIBLE
                view.confirmStep2.setBackgroundResource(R.drawable.button_mail_circle_corner_selector)
            }
            else -> {
                view.cupButton.visibility = View.VISIBLE
                view.weightButton.visibility = View.GONE
                view.confirmStep2.setBackgroundResource(R.drawable.button_femail_circle_corner_selector)
            }
        }
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(SetupWizardViewModel::class.java)
        Log.d("TTTT", "vm=======2==========================$viewModel")
        back2.setNavigationOnClickListener {
             activity?.onBackPressed()
        }
        confirmStep2.setOnClickListener {
            if (isValid()) {
                viewModel.setUserFeatures(user.height, user.weight.toInt(), user.age, user.safeCup)

                //for test
                activity?.addFragmentSafely(R.id.containerLayout, SetupWizardThreeFragment.newInstance(user), "step3", true)
            }
        }

        heightButton.setOnClickListener {
            Picker.showPicker(activity, getString(R.string.pick_height), 150, 230, user.height) { value, _ ->
                height.text = getString(R.string.height_value_template, value)
                user.height = value
            }
        }
        weightButton.setOnClickListener {
            Picker.showPicker(activity, getString(R.string.pick_weight), 40, 150, user.weight.toInt()) { value, _ ->
                weight.text = getString(R.string.weight_value_template, value)
                user.weight = value.toDouble()
            }
        }
        cupButton.setOnClickListener {
            Picker.showPicker(activity, getString(R.string.pick_cup), cupNumberArray, cupNumber, cupSizeArray, cupSize) { value1, value2 ->
                val cupValue = "${cupNumberArray[value1]}${cupSizeArray[value2]}"
                cupNumber = value1
                cupSize = value2
                cup.text = cupValue
                user.cup = cupValue
            }
        }
        ageButton.setOnClickListener {
            Picker.showPicker(activity, getString(R.string.pick_age), 16, 70, user.age) { value, _ ->
                age.text = getString(R.string.age_value_template, value)
                user.age = value
            }
        }

        viewModel.error.observe(this, Observer {
            snackbar(it)
        })

        viewModel.setupFeatures.observe(this, Observer {
            activity?.addFragmentSafely(R.id.containerLayout, SetupWizardThreeFragment.newInstance(user), "step3", true)
        })
    }


    private fun isValid(): Boolean {
        if (user.age > 0 && user.height > 0) {
            if (user.sex == Sex.FEMALE.value && !TextUtils.isEmpty(user.cup)) {
                return true
            }
            if (user.sex == Sex.MALE.value && user.weight > 0) {
                return true
            }
        }
        return false
    }

    companion object {
        /**
         * argument 0,female; 1,male
         */
        @JvmStatic
        fun newInstance(user: User) = SetupWizardTwoFragment().apply {
            arguments = Bundle().apply {
                putParcelable(ARG_USER, user)
            }
        }
    }
}
