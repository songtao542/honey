package com.snt.phoney.ui.setup

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.snt.phoney.R
import com.snt.phoney.base.BaseFragment
import com.snt.phoney.domain.model.User
import com.snt.phoney.extensions.addFragmentSafely
import com.snt.phoney.extensions.snackbar
import com.snt.phoney.extensions.toStringArray
import kotlinx.android.synthetic.main.fragment_signup_2.*
import kotlinx.android.synthetic.main.fragment_signup_2.view.*

private const val ARG_SEX = "sex"

class SetupWizardTwoFragment : BaseFragment() {

    lateinit var viewModel: SetupWizardViewModel

    private var sex: Int = 0
    private var user: User = User()
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
            sex = it.getInt(ARG_SEX, 0)
            user.sex = sex
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_signup_2, container, false)
        when (sex) {
            0 -> {
                view.cupButton.visibility = View.VISIBLE
                view.weightButton.visibility = View.GONE
                view.confirmStep2.setBackgroundResource(R.drawable.button_femail_circle_corner_selector)
            }
            else -> {
                view.cupButton.visibility = View.GONE
                view.weightButton.visibility = View.VISIBLE
                view.confirmStep2.setBackgroundResource(R.drawable.button_mail_circle_corner_selector)
            }
        }
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(SetupWizardViewModel::class.java)
        back2.setNavigationOnClickListener { activity?.supportFragmentManager?.popBackStack() }
        confirmStep2.setOnClickListener {
            if (isValid()) {
                viewModel.setUserFeatures(user.height, user.weight, user.age, user.cup ?: "")
            }
        }

        heightButton.setOnClickListener {
            togglePicker(getString(R.string.pick_height), 150, 230, user.height
                    ?: 0, "height") { value, _ ->
                height.text = getString(R.string.height_value_template, value)
                user.height = value
            }
        }
        weightButton.setOnClickListener {
            togglePicker(getString(R.string.pick_weight), 40, 150, user.weight?.toInt()
                    ?: 0, "weight") { value, _ ->
                weight.text = getString(R.string.weight_value_template, value)
                user.weight = value.toFloat()
            }
        }
        cupButton.setOnClickListener {
            togglePicker(getString(R.string.pick_cup), cupNumberArray, cupNumber, cupSizeArray, cupSize, "cup") { value1, value2 ->
                val cupValue = "${cupNumberArray[value1]}${cupSizeArray[value2]}"
                cupNumber = value1
                cupSize = value2
                cup.text = cupValue
                user.cup = cupValue
            }
        }
        ageButton.setOnClickListener {
            togglePicker(getString(R.string.pick_age), 16, 70, user.age ?: 0, "age") { value, _ ->
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


    private fun togglePicker(title: String, minValue: Int, maxValue: Int, value: Int = 0, tag: String, handler: ((value: Int, _: Int) -> Unit)) {
        activity?.supportFragmentManager?.let {
            val pickerFragment = PickerFragment.newInstance(title, minValue, maxValue)
            pickerFragment.setOnResultListener(handler)
            pickerFragment.value1 = value
            pickerFragment.show(it, tag)
        }
    }

    private fun <T1, T2> togglePicker(title: String, column1: Array<T1>, value1: Int, column2: Array<T2>, value2: Int, tag: String, handler: ((value1: Int, value2: Int) -> Unit)) {
        activity?.supportFragmentManager?.let {
            val pickerFragment = PickerFragment.newInstance(title, column1 = column1.toStringArray(), column2 = column2.toStringArray())
            pickerFragment.setOnResultListener(handler)
            pickerFragment.value1 = value1
            pickerFragment.value2 = value2
            pickerFragment.show(it, tag)
        }
    }

    private fun isValid(): Boolean {
        if (user.age > 0 && user.height > 0) {
            if (user.sex == 0 && !TextUtils.isEmpty(user.cup)) {
                return true
            }
            if (user.sex == 1 && user.weight > 0) {
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
        fun newInstance(sex: Int) = SetupWizardTwoFragment().apply {
            arguments = Bundle().apply {
                putInt(ARG_SEX, sex)
            }
        }
    }
}
