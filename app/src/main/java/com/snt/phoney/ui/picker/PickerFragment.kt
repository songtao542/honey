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
import java.util.*

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
        column1View.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
        column1View.wrapSelectorWheel = false
        column1View.setDividerHeight(0.4f)
        column1View.setDividerColor(colorOf(R.color.picker_title))

        column2View.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
        column2View.wrapSelectorWheel = false
        column2View.setDividerHeight(0.4f)
        column2View.setDividerColor(colorOf(R.color.picker_title))

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
            if (column1Values != null) {
                this.column1Values = column1Values
            }
            if (column2Values != null) {
                this.column2Values = column2Values
            }
            if (column1Values != null || column2Values != null) {
                setColumnInternal(column1Values, column2Values)
            }
        }

        confirm.setOnClickListener {
            dismiss()
            val index1 = column1View.value
            val index2 = column2View.value
            onResultListener?.invoke(index1, index2)
            onValueListener?.invoke(column1View.displayedValues[index1], column2View.displayedValues[index2])
        }
    }

    private var onResultListener: ((value1: Int, value2: Int) -> Unit)? = null

    fun setOnResultListener(onResultListener: ((value: Int, value2: Int) -> Unit)) {
        this.onResultListener = onResultListener
    }

    private var onValueListener: ((value1: String, value2: String) -> Unit)? = null

    fun setOnValueListener(onValueListener: ((value: String, value2: String) -> Unit)) {
        this.onValueListener = onValueListener
    }

    var value1: Int = 0
    var value2: Int = 0

    private var minValue: Int = -1
    private var maxValue: Int = -1

    private var column1Values: Array<String>? = null
    private var column2Values: Array<String>? = null

    private var columns: LinkedHashMap<String, out List<String>>? = null

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
        if (column1View != null && minValue >= 0 && maxValue >= 0) {
            column1View.minValue = minValue
            column1View.maxValue = maxValue
            if (value1 in minValue..maxValue) {
                column1View.value = value1
            }
            showProgress(false)
        }
    }

    override fun onResume() {
        super.onResume()
        if (minValue != -1 && maxValue != -1) {
            setColumnInternal(minValue, maxValue)
        } else if (column1Values != null || column2Values != null) {
            setColumnInternal(column1Values, column2Values)
        } else if (columns != null) {
            setColumnInternal(columns)
        }
        if (mIsColumn2SubOfColumn1) {
            setColumn2SubOfColumn1Internal(mIsColumn2SubOfColumn1)
        }
    }

    fun setColumn(column1Values: Array<String>?, column2Values: Array<String>? = null) {
        this.column1Values = column1Values
        this.column2Values = column2Values
        setColumnInternal(column1Values, column2Values)
    }

    private fun setColumnInternal(column1Values: Array<String>?, column2Values: Array<String>? = null) {
        if (column1Values != null && column1View != null) {
            //先置空，以免数组越界
            column1View.displayedValues = null
            column1View.minValue = 0
            column1View.maxValue = column1Values.size - 1
            column1View.displayedValues = column1Values
            if (value1 >= 0 && value1 < column1Values.size) {
                column1View.value = value1
            } else {
                column1View.value = 0
            }
            showProgress(false)
        }

        if (column2Values != null && column2View != null) {
            column2View.visibility = View.VISIBLE
            //先置空，以免数组越界
            column2View.displayedValues = null
            column2View.minValue = 0
            column2View.maxValue = column2Values.size - 1
            column2View.displayedValues = column2Values
            if (value2 >= 0 && value2 < column2Values.size) {
                column2View.value = value2
            } else {
                column2View.value = 0
            }
            showProgress(false)
        }
    }

    private var mIsColumn2SubOfColumn1 = false

    fun setColumn2SubOfColumn1(isColumn2SubOfColumn1: Boolean) {
        this.mIsColumn2SubOfColumn1 = isColumn2SubOfColumn1
        setColumn2SubOfColumn1Internal(mIsColumn2SubOfColumn1)
    }

    private fun setColumn2SubOfColumn1Internal(isColumn2SubOfColumn1: Boolean) {
        if (column1View != null && isColumn2SubOfColumn1 && column1Values != null) {
            val array: Array<String> = column1Values?.copyOfRange(0, 1) ?: emptyArray()
            setColumnInternal(null, array)

            column1View.setOnValueChangedListener { _, _, newVal ->
                val array: Array<String> = column1Values?.copyOfRange(0, if (newVal == 0) 1 else newVal)
                        ?: emptyArray()
                setColumnInternal(null, array)
            }
        }
    }

    /**
     * 使用 LinkedHashMap 是为了保持插入顺序
     */
    fun setColumn(columns: LinkedHashMap<String, out List<String>>) {
        this.columns = columns
        setColumnInternal(columns)
    }

    private fun setColumnInternal(columns: LinkedHashMap<String, out List<String>>?) {
        if (columns != null && columns.isNotEmpty() && column1View != null && column2View != null) {
            column1Values = columns.keys.toTypedArray()
            setColumnInternal(column1Values, null)

            val index = if (value1 >= 0 && value1 < column1Values!!.size) value1 else 0
            val array: Array<String> = columns[column1Values!![index]]!!.toTypedArray()
            setColumnInternal(null, array)

            column1View.setOnValueChangedListener { _, _, newVal ->
                val column1 = column1Values!![newVal]
                val array: Array<String> = columns[column1]!!.toTypedArray()
                setColumnInternal(null, array)
            }
        }
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
