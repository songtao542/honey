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
import com.snt.phoney.domain.model.*
import com.snt.phoney.ui.main.MainActivity
import com.snt.phoney.utils.Picker
import com.zaaach.citypicker.model.City
import kotlinx.android.synthetic.main.fragment_signup_3.*
import kotlinx.android.synthetic.main.fragment_signup_3.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

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
        when (Sex.from(user.sex)) {
            Sex.MALE -> {
                view.confirmStep3.setBackgroundResource(R.drawable.button_mail_circle_corner_selector)
            }
            else -> {
                view.confirmStep3.setBackgroundResource(R.drawable.button_femail_circle_corner_selector)
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
                viewModel.setUserInfo(CityPickerConverter.reverseConvert(selectedCities!!), selectedJob!!.safeName, selectedPurpose!!.safeName)
            }
        }

        cityButton.setOnClickListener {
            Picker.showCityPicker(activity, { cityPicker ->
                viewModel.cities.observe(this@SetupWizardThreeFragment, Observer { cities ->
                    cityPicker.setCities(cities)
                })
            }) { cities ->
                if (cities.isNotEmpty()) {
                    selectedCities = cities
                    var text = StringBuilder()
                    selectedCities!!.forEach { city ->
                        text.append(city.name).append(" ")
                    }
                    city.text = text.toString()
                }
            }
        }
        purposeButton.setOnClickListener {
            Picker.showPicker(activity, getString(R.string.select_job), selectedPurposeIndex, provider = { picker ->
                viewModel.purposes.observe(this, Observer { purposes ->
                    val purposeNames = Array(purposes.size) { index ->
                        purposes[index].name!!
                    }
                    picker.setColumn(purposeNames)
                })
            }, handler = { value1, _ ->
                selectedPurposeIndex = value1
                selectedPurpose = viewModel.purposes.value?.get(value1)
                purpose.text = selectedPurpose?.name
            })
        }
        jobButton.setOnClickListener {
            Picker.showPicker(activity, getString(R.string.select_job), selectedJobIndex, provider = { picker ->
                viewModel.careers.observe(this, Observer { careers ->
                    val careerNames = Array(careers.size) { index ->
                        careers[index].name!!
                    }
                    picker.setColumn(careerNames)
                })
            }, handler ={ value1, _ ->
                selectedJobIndex = value1
                selectedJob = viewModel.careers.value?.get(value1)
                job.text = selectedJob?.name
            })
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

    companion object {
        @JvmStatic
        fun newInstance(user: User) = SetupWizardThreeFragment().apply {
            arguments = Bundle().apply {
                putParcelable(ARG_USER, user)
            }
        }
    }
}
