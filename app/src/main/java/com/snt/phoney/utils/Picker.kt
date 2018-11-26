package com.snt.phoney.utils

import androidx.fragment.app.FragmentActivity
import com.snt.phoney.R
import com.snt.phoney.extensions.toStringArray
import com.snt.phoney.ui.setup.PickerFragment
import com.zaaach.citypicker.CityPickerFragment
import com.zaaach.citypicker.adapter.CityPicker
import com.zaaach.citypicker.model.City

object Picker {
    fun togglePicker(activity: FragmentActivity?, title: String, minValue: Int, maxValue: Int, value: Int = 0, tag: String, handler: ((value: Int, _: Int) -> Unit)) {
        activity?.supportFragmentManager?.let {
            val pickerFragment = PickerFragment.newInstance(title, minValue, maxValue)
            pickerFragment.setOnResultListener(handler)
            pickerFragment.value1 = value
            pickerFragment.show(it, tag)
        }
    }

    fun <T1, T2> togglePicker(activity: FragmentActivity?, title: String, column1: Array<T1>, value1: Int, column2: Array<T2>, value2: Int, tag: String, handler: ((value1: Int, value2: Int) -> Unit)) {
        activity?.supportFragmentManager?.let {
            val pickerFragment = PickerFragment.newInstance(title, column1 = column1.toStringArray(), column2 = column2.toStringArray())
            pickerFragment.setOnResultListener(handler)
            pickerFragment.value1 = value1
            pickerFragment.value2 = value2
            pickerFragment.show(it, tag)
        }
    }

    fun togglePicker(activity: FragmentActivity?, title: String, value: Int, tag: String, provider: ((picker: PickerFragment) -> Unit), handler: ((value1: Int, value2: Int) -> Unit)) {
        activity?.supportFragmentManager?.let {
            val pickerFragment = PickerFragment.newInstance(title)
            pickerFragment.setOnResultListener(handler)
            pickerFragment.value1 = value
            pickerFragment.show(it, tag)
            provider.invoke(pickerFragment)
        }
    }

    fun toggleCityPicker(activity: FragmentActivity?, provider: ((picker: CityPicker) -> Unit), handler: ((cities: List<City>) -> Unit)) {
        CityPickerFragment.Builder()
                .fragmentManager(activity?.supportFragmentManager)
                .animationStyle(R.style.DefaultCityPickerAnimation)
                .multipleMode(true)
                .enableHotCities(false)
                .enableLocation(false)
                .useDefaultCities(false)
                .requestCitiesListener { cityPicker ->
                    provider.invoke(cityPicker)
                }
                .resultListener {
                    handler.invoke(it)
                }
                .show()
    }
}