package com.snt.phoney.utils

import android.app.DatePickerDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.snt.phoney.R
import com.snt.phoney.extensions.toStringArray
import com.snt.phoney.ui.picker.PickerFragment
import com.zaaach.citypicker.CityPickerFragment
import com.zaaach.citypicker.adapter.CityPicker
import com.zaaach.citypicker.model.City
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.engine.impl.GlideEngine
import com.zhihu.matisse.filter.Filter
import com.zhihu.matisse.internal.entity.CaptureStrategy
import java.util.*

object Picker {
    @JvmStatic
    fun showPicker(activity: FragmentActivity?, title: String, minValue: Int, maxValue: Int, value: Int = 0, tag: String, handler: ((value: Int, _: Int) -> Unit)) {
        activity?.supportFragmentManager?.let {
            val pickerFragment = PickerFragment.newInstance(title, minValue, maxValue)
            pickerFragment.setOnResultListener(handler)
            pickerFragment.value1 = value
            pickerFragment.show(it, tag)
        }
    }

    @JvmStatic
    fun <T1, T2> showPicker(activity: FragmentActivity?, title: String, column1: Array<T1>, value1: Int, column2: Array<T2>, value2: Int, tag: String, handler: ((value1: Int, value2: Int) -> Unit)) {
        activity?.supportFragmentManager?.let {
            val pickerFragment = PickerFragment.newInstance(title, column1 = column1.toStringArray(), column2 = column2.toStringArray())
            pickerFragment.setOnResultListener(handler)
            pickerFragment.value1 = value1
            pickerFragment.value2 = value2
            pickerFragment.show(it, tag)
        }
    }

    @JvmStatic
    fun showPicker(activity: FragmentActivity?, title: String, value: Int, tag: String, provider: ((picker: PickerFragment) -> Unit), handler: ((value1: Int, value2: Int) -> Unit)) {
        activity?.supportFragmentManager?.let {
            val pickerFragment = PickerFragment.newInstance(title)
            pickerFragment.setOnResultListener(handler)
            pickerFragment.value1 = value
            pickerFragment.show(it, tag)
            provider.invoke(pickerFragment)
        }
    }

    @JvmStatic
    fun showCityPicker(activity: FragmentActivity?, provider: ((picker: CityPicker) -> Unit), handler: ((cities: List<City>) -> Unit)) {
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

    @JvmStatic
    val REQUEST_CODE_CHOOSE = 23

    @JvmStatic
    fun showPhotoPicker(activity: FragmentActivity? = null, fragment: Fragment? = null, max: Int = 1) {
        if (activity == null && fragment == null) {
            return
        }
        val matisse = if (activity != null) Matisse.from(activity) else Matisse.from(fragment)
        matisse.choose(MimeType.ofImage())
                .showSingleMediaType(true)
                .theme(R.style.Matisse_Dracula)
                .countable(true)
                .addFilter(GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                .maxSelectable(max)
                .spanCount(4)
                .originalEnable(true)
                .maxOriginalSize(10)
                .capture(true)
                .captureStrategy(CaptureStrategy(true, "com.snt.phoney.fileprovider"))
                .imageEngine(GlideEngine())
                .forResult(REQUEST_CODE_CHOOSE)
    }

    @JvmStatic
    fun showDatePicker(activity: FragmentActivity? = null, handler: ((year: Int, monthOfYear: Int, dayOfMonth: Int) -> Unit)) {
        if (activity == null) {
            return
        }
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(activity,
                DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                    handler.invoke(year, monthOfYear, dayOfMonth)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH))

        datePickerDialog.datePicker.minDate = System.currentTimeMillis()
        datePickerDialog.show()
    }

}