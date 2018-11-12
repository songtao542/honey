package com.snt.phoney.ui.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import com.snt.phoney.R
import com.snt.phoney.base.BottomDialogFragment
import com.snt.phoney.extensions.colorOf
import com.snt.phoney.extensions.setDividerColor
import com.snt.phoney.extensions.setDividerHeight
import kotlinx.android.synthetic.main.fragment_picker.*

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


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        picker.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
        picker.wrapSelectorWheel = false
        picker.setDividerHeight(1)
        picker.setDividerColor(colorOf(R.color.colorPrimaryMale))
        picker.maxValue = 230
        picker.minValue = 0
    }

    companion object {

        @JvmStatic
        fun newInstance() = PickerFragment().apply {
            arguments = Bundle().apply {

            }
        }
    }
}
