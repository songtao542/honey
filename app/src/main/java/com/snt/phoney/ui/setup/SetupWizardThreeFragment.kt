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
import com.snt.phoney.domain.model.Career
import com.snt.phoney.domain.model.CityPickerConverter
import com.snt.phoney.domain.model.Purpose
import com.snt.phoney.domain.model.User
import com.snt.phoney.ui.main.MainActivity
import com.zaaach.citypicker.CityPickerFragment
import com.zaaach.citypicker.model.City
import kotlinx.android.synthetic.main.fragment_signup_3.*
import kotlinx.android.synthetic.main.fragment_signup_3.view.*

private const val ARG_USER = "user"

class SetupWizardThreeFragment : BaseFragment() {

    private lateinit var user: User

    private var selectedCities: List<City>? = null
    private var selectedJob: Career? = null
    private var selectedJobIndex: Int = 0
    private var selectedPurpose: Purpose? = null
    private var selectedPurposeIndex: Int = 0

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
        Log.d("TTTT", "vm=======3==========================$viewModel")
        back3.setNavigationOnClickListener { activity?.supportFragmentManager?.popBackStack() }
        confirmStep3.setOnClickListener {
            if (isValid()) {
                var cities = java.lang.StringBuilder()
                selectedCities?.forEach { city ->
                    cities.append(city.code).append(",")
                }
                cities.delete(cities.length - 1, cities.length)
                viewModel.setUserInfo(cities.toString(), selectedJob?.name
                        ?: "", selectedPurpose?.name ?: "")
            }
        }

        cityButton.setOnClickListener {
            pickCity()
        }
        purposeButton.setOnClickListener {
            togglePicker(getString(R.string.select_job), selectedPurposeIndex, "purpose", provider = { picker ->
                viewModel.purposes.observe(this, Observer { purposes ->
                    val purposeNames = Array(purposes.size) { index ->
                        purposes[index].name!!
                    }
                    picker.setColumn(purposeNames)
                })
            }) { value1, _ ->
                selectedPurposeIndex = value1
                selectedPurpose = viewModel.purposes.value?.get(value1)
                purpose.text = selectedPurpose?.name
            }
        }
        jobButton.setOnClickListener {
            togglePicker(getString(R.string.select_job), selectedJobIndex, "career", provider = { picker ->
                viewModel.careers.observe(this, Observer { careers ->
                    val careerNames = Array(careers.size) { index ->
                        careers[index].name!!
                    }
                    picker.setColumn(careerNames)
                })
            }) { value1, _ ->
                selectedJobIndex = value1
                selectedJob = viewModel.careers.value?.get(value1)
                job.text = selectedJob?.name
            }
        }

        viewModel.setupUserInfo.observe(this, Observer {
            context?.let {
                startActivity(MainActivity.newIntent(it))
                activity?.finish()
                return@let
            }
        })
    }

    private fun isValid(): Boolean {
        if (selectedCities != null && selectedJob != null && selectedPurpose != null) {
            return true
        }
        return false
    }

    private fun togglePicker(title: String, value: Int, tag: String, provider: ((picker: PickerFragment) -> Unit), handler: ((value1: Int, value2: Int) -> Unit)) {
        activity?.supportFragmentManager?.let {
            val pickerFragment = PickerFragment.newInstance(title)
            pickerFragment.setOnResultListener(handler)
            pickerFragment.value1 = value
            pickerFragment.show(it, tag)
            provider.invoke(pickerFragment)
        }
    }

    private fun pickCity() {
        CityPickerFragment.Builder()
                .fragmentManager(childFragmentManager)
                .animationStyle(R.style.DefaultCityPickerAnimation)
                .multipleMode(true)
                .enableHotCities(false)
                .enableLocation(false)
                .useDefaultCities(false)
                .requestCitiesListener { cityPicker ->
                    viewModel.cities.observe(this@SetupWizardThreeFragment, Observer {
                        cityPicker.setCities(CityPickerConverter.convert(it))
                    })
                }
                .resultListener {
                    if (it.isNotEmpty()) {
                        selectedCities = it
                        var text = StringBuilder()
                        selectedCities!!.forEach { city ->
                            text.append(city.name).append(" ")
                        }
                        city.text = text.toString()
                    }
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
