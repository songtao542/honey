package com.snt.phoney.ui.main.home.friend

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.snt.phoney.R
import com.snt.phoney.base.BaseFragment
import com.snt.phoney.domain.model.DatingProgram
import com.snt.phoney.utils.Picker
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.fragment_friend_more_filter.*

class FilterFragment : BaseFragment() {

    private val cupArray = arrayOf("a", "b", "c", "d", "e", "f", "g")

    companion object {
        @JvmStatic
        fun newInstance() = FilterFragment()
    }

    private lateinit var viewModel: FilterViewModel
    private var selectedProgram: DatingProgram? = null
    private var selectedProgramIndex: Int = -1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_friend_more_filter, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(FilterViewModel::class.java)
        toolbar.setNavigationOnClickListener { activity?.onBackPressed() }
        titleTextView.setText(R.string.friend_more_filter)

        heightBar.setFormatter {
            "${it}cm"
        }
        ageBar.setFormatter {
            "$it${getString(R.string.unit_age)}"
        }
        cupBar.setFormatter {
            cupArray[it.toInt() - 1]
        }

        programButton.setOnClickListener {
            Picker.showPicker(activity, getString(R.string.select_dating_program), 0, { picker ->
                viewModel.programs.observe(this, Observer { programs ->
                    val programNames = Array(programs.size) { index ->
                        programs[index].name!!
                    }
                    picker.setColumn(programNames)
                })
            }, handler = { value1, _ ->
                selectedProgramIndex = value1
                selectedProgram = viewModel.programs.value?.get(value1)
                datingProgram.text = selectedProgram?.name
            })
        }

        confirmButton.setOnClickListener {
            val result = Result(heightBar.leftPinValue.toInt(), heightBar.rightPinValue.toInt(),
                    ageBar.leftPinValue.toInt(), ageBar.rightPinValue.toInt(),
                    cupArray[cupBar.leftPinValue.toInt() - 1], cupArray[cupBar.rightPinValue.toInt() - 1])
            onResultListener?.invoke(result)
            activity?.onBackPressed()
        }
    }

    private var onResultListener: ((result: Result) -> Unit)? = null

    fun setOnResultListener(onResultListener: ((result: Result) -> Unit)) {
        this.onResultListener = onResultListener
    }

    data class Result(
            val startHeight: Int,
            val endHeight: Int,
            val startAge: Int,
            val endAge: Int,
            val startCup: String,
            val endCup: String)

}