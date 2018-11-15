package com.snt.phoney.ui.setup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.snt.phoney.R
import com.snt.phoney.base.BaseFragment
import com.snt.phoney.domain.model.CityPickerConvertor
import com.snt.phoney.domain.model.User
import com.zaaach.citypicker.CityPickerFragment
import com.zaaach.citypicker.model.City
import kotlinx.android.synthetic.main.fragment_signup_3.*
import kotlinx.android.synthetic.main.fragment_signup_3.view.*

private const val ARG_USER = "user"

class SetupWizardThreeFragment : BaseFragment() {

    private lateinit var user: User

    private var selectCities: List<City>? = null

    lateinit var viewModel: SetupWizardViewModel

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
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(SetupWizardViewModel::class.java)
        back3.setNavigationOnClickListener { activity?.supportFragmentManager?.popBackStack() }
        confirmStep3.setOnClickListener {
            context?.let {
                //BindPhoneFragment.newInstance().show(childFragmentManager, "bindPhone")

            }
        }

        cityButton.setOnClickListener {
            pickCity()
        }
        purposeButton.setOnClickListener {

        }
        jobButton.setOnClickListener {

        }


    }

    private fun togglePicker(title: String, minValue: Int, maxValue: Int, tag: String, handler: ((value: Int, _: Int) -> Unit)) {
        activity?.supportFragmentManager?.let {
            val pickerFragment = PickerFragment.newInstance(title, minValue, maxValue)
            pickerFragment.setOnResultListener(handler)
            pickerFragment.show(it, tag)
        }
    }

    private fun pickCity() {
        CityPickerFragment.Builder()
                .fragmentManager(childFragmentManager)
                .animationStyle(R.style.DefaultCityPickerAnimation)
                .multipleMode(true)
                .enableHotCities(false)
                .enableLocation(false)
                .requestCitiesListener { cityPicker ->
                    viewModel.cities.observe(this@SetupWizardThreeFragment, Observer {
                        cityPicker.setCities(CityPickerConvertor.convert(it))
                    })
                }
                .resultListener {
                    selectCities = it
                }
                .show()
    }

    companion object {
        @JvmStatic
        fun newInstance(user: User) = SetupWizardThreeFragment().apply {
            arguments = Bundle().apply {
                putParcelable(ARG_USER, user)
            }
        }
    }
}
