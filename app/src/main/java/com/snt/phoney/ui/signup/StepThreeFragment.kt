package com.snt.phoney.ui.signup

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.snt.phoney.R
import com.snt.phoney.base.BaseFragment
import com.snt.phoney.domain.model.User
import com.zaaach.citypicker.CityPicker
import com.zaaach.citypicker.adapter.OnResultListener
import com.zaaach.citypicker.model.City
import com.zaaach.citypicker.model.LocateState
import com.zaaach.citypicker.model.LocatedCity
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

        city.setOnClickListener {
            pickCity()
        }
    }

    private fun pickCity() {
        CityPicker.Builder()
                .fragmentManager(childFragmentManager)
                .enableAnimation(true)
                .animationStyle(R.style.DefaultCityPickerAnimation)
                .locatedCity(null)
                .multipleMode(true)
                .enableHotCities(false)
                .enableLocation(true)
                .listener(object : OnResultListener {
                    override fun onResult(data: List<City>?) {

                    }

                    override fun onRequestLocation(picker: CityPicker) {
                        //开始定位，这里模拟一下定位
                        Handler().postDelayed({ picker.locationChanged(LocatedCity("深圳", "广东", "101280601"), LocateState.SUCCESS) }, 3000)
                    }
                })
                .show()
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
