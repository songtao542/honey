package com.snt.phoney.ui.signup

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.NumberPicker
import com.snt.phoney.R
import com.snt.phoney.base.BottomDialogFragment
import com.snt.phoney.extensions.colorOf
import com.snt.phoney.extensions.setDividerColor
import com.snt.phoney.extensions.setDividerHeight
import kotlinx.android.synthetic.main.fragment_picker.*
import kotlin.math.max

const val EXTRA_TITLE = "title"
const val EXTRA_MIN = "min_value"
const val EXTRA_MAX = "max_value"
const val EXTRA_COLUMN_1_VALUES = "column1"
const val EXTRA_COLUMN_2_VALUES = "column2"

/**
 *
 */
class PickerFragment : BottomDialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_picker, container, false)
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        return dialog
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        column1.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
        column1.wrapSelectorWheel = false
        column1.setDividerHeight(1)
        column1.setDividerColor(colorOf(R.color.picker_title))

        column2.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
        column2.wrapSelectorWheel = false
        column2.setDividerHeight(1)
        column2.setDividerColor(colorOf(R.color.picker_title))

        arguments?.let {
            val title = it.getString(EXTRA_TITLE)
            this.title.text = title ?: getString(R.string.please_select)
            val minValue = it.getInt(EXTRA_MIN, 0)
            val maxValue = it.getInt(EXTRA_MAX, 0)
            if (minValue != 0 && maxValue != 0) {
                column1.minValue = minValue
                column1.maxValue = maxValue
                if (value1 in minValue..maxValue) {
                    column1.value = value1
                }
            }

            val column1Values = it.getStringArray(EXTRA_COLUMN_1_VALUES)
            if (column1Values != null) {
                column1.displayedValues = column1Values
                column1.minValue = 0
                column1.maxValue = column1Values.size - 1
                if (value1 > 0 && value1 < column1Values.size) {
                    column1.value = value1
                }
            }

            val column2Values = it.getStringArray(EXTRA_COLUMN_2_VALUES)
            if (column2Values != null) {
                column2.visibility = View.VISIBLE
                column2.displayedValues = column2Values
                column2.minValue = 0
                column2.maxValue = column2Values.size - 1
                if (value2 > 0 && value2 < column2Values.size) {
                    column2.value = value2
                }
            }
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


    companion object {
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
