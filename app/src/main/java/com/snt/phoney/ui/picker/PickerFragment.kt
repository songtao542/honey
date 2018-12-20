package com.snt.phoney.ui.picker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import com.snt.phoney.R
import com.snt.phoney.base.NoInjectBottomDialogFragment
import com.snt.phoney.extensions.colorOf
import com.snt.phoney.extensions.setDividerColor
import com.snt.phoney.extensions.setDividerHeight
import kotlinx.android.synthetic.main.fragment_picker.*

const val EXTRA_TITLE = "title"
const val EXTRA_MIN = "min_value"
const val EXTRA_MAX = "max_value"
const val EXTRA_COLUMN_1_VALUES = "column1"
const val EXTRA_COLUMN_2_VALUES = "column2"
const val EXTRA_SHOW_PROGRESS = "progress"

/**
 *
 */
class PickerFragment : NoInjectBottomDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_picker, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        column1.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
        column1.wrapSelectorWheel = false
        column1.setDividerHeight(0.4f)
        column1.setDividerColor(colorOf(R.color.picker_title))

        column2.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
        column2.wrapSelectorWheel = false
        column2.setDividerHeight(0.4f)
        column2.setDividerColor(colorOf(R.color.picker_title))

        arguments?.let {
            val title = it.getString(EXTRA_TITLE)
            this.title.text = title ?: getString(R.string.please_select)
            val showProgress = it.getBoolean(EXTRA_SHOW_PROGRESS, false)
            showProgress(showProgress)

            val minValue = it.getInt(EXTRA_MIN, -1)
            val maxValue = it.getInt(EXTRA_MAX, -1)
            if (minValue != -1 && maxValue != -1) {
                setColumnInternal(minValue, maxValue)
            }

            val column1Values = it.getStringArray(EXTRA_COLUMN_1_VALUES)
            val column2Values = it.getStringArray(EXTRA_COLUMN_2_VALUES)
            setColumnInternal(column1Values, column2Values)
        }

        confirm.setOnClickListener {
            dismiss()
            onResultListener?.invoke(column1.value, column2.value)
        }
    }

    private var onResultListener: ((value1: Int, value2: Int) -> Unit)? = null

    fun setOnResultListener(onResultListener: ((value: Int, value2: Int) -> Unit)) {
        this.onResultListener = onResultListener
    }

    var value1: Int = 0
    var value2: Int = 0

    var minValue: Int = -1
    var maxValue: Int = -1

    var column1Values: Array<String>? = null
    var column2Values: Array<String>? = null


    private fun showProgress(show: Boolean) {
        if (progress != null && pickers != null) {
            if (show) {
                progress.visibility = View.VISIBLE
                pickers.visibility = View.GONE
            } else {
                progress.visibility = View.GONE
                pickers.visibility = View.VISIBLE
            }
        }
    }

    fun setColumn(minValue: Int, maxValue: Int) {
        this.minValue = minValue
        this.maxValue = maxValue
        setColumnInternal(minValue, maxValue)
    }

    private fun setColumnInternal(minValue: Int, maxValue: Int) {
        if (column1 != null && minValue >= 0 && maxValue >= 0) {
            column1.minValue = minValue
            column1.maxValue = maxValue
            if (value1 in minValue..maxValue) {
                column1.value = value1
            }
            showProgress(false)
        }
    }

    override fun onResume() {
        super.onResume()
        if (minValue != -1 && maxValue != -1) {
            setColumnInternal(minValue, maxValue)
        } else {
            setColumnInternal(column1Values, column2Values)
        }
    }

    fun setColumn(column1Values: Array<String>?, column2Values: Array<String>? = null) {
        this.column1Values = column1Values
        this.column2Values = column2Values
        setColumnInternal(column1Values, column2Values)
    }

    private fun setColumnInternal(column1Values: Array<String>?, column2Values: Array<String>? = null) {
        if (column1Values != null && column1 != null) {
            column1.displayedValues = column1Values
            column1.minValue = 0
            column1.maxValue = column1Values.size - 1
            if (value1 > 0 && value1 < column1Values.size) {
                column1.value = value1
            }
        }

        if (column2Values != null && column2 != null) {
            column2.visibility = View.VISIBLE
            column2.displayedValues = column2Values
            column2.minValue = 0
            column2.maxValue = column2Values.size - 1
            if (value2 > 0 && value2 < column2Values.size) {
                column2.value = value2
            }
        }
        showProgress(false)
    }

    companion object {

        @JvmStatic
        fun newInstance(title: String) = PickerFragment().apply {
            arguments = Bundle().apply {
                this.putString(EXTRA_TITLE, title)
                this.putBoolean(EXTRA_SHOW_PROGRESS, true)
            }
        }

        @JvmStatic
        fun newInstance(title: String, minValue: Int, maxValue: Int) = PickerFragment().apply {
            arguments = Bundle().apply {
                this.putString(EXTRA_TITLE, title)
                this.putInt(EXTRA_MIN, minValue)
                this.putInt(EXTRA_MAX, maxValue)
            }
        }

        @JvmStatic
        fun newInstance(title: String, values: Array<String>) = PickerFragment().apply {
            arguments = Bundle().apply {
                this.putString(EXTRA_TITLE, title)
                this.putStringArray(EXTRA_COLUMN_1_VALUES, values)
            }
        }

        @JvmStatic
        fun newInstance(title: String, column1: Array<String>, column2: Array<String>) = PickerFragment().apply {
            arguments = Bundle().apply {
                this.putString(EXTRA_TITLE, title)
                this.putStringArray(EXTRA_COLUMN_1_VALUES, column1)
                this.putStringArray(EXTRA_COLUMN_2_VALUES, column2)
            }
        }
    }
}
