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
import com.snt.phoney.domain.model.Sex
import com.snt.phoney.utils.Picker
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.fragment_friend_more_filter.*

class FilterFragment : BaseFragment() {

    private val cupCharArray = arrayOf("A", "B", "C", "D", "E", "F", "G")

    private val cupArray = Array(77) {
        "${30 + it / 7}${cupCharArray[it % 7]}"
    }

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

        weightBar.setFormatter {
            "$it${getString(R.string.unit_kg)}"
        }

        if (viewModel.getUser()?.sex == Sex.FEMALE.value) {
            cupLabel.visibility = View.GONE
            cupBar.visibility = View.GONE
        } else {
            cupLabel.visibility = View.VISIBLE
            cupBar.visibility = View.VISIBLE
            cupBar.setFormatter {
                cupArray[it.toInt()]
            }
        }

        programButton.setOnClickListener {
            Picker.showPicker(activity, getString(R.string.select_dating_program), 0, { picker ->
                if (viewModel.programs.value != null) {
                    val programs = viewModel.programs.value!!
                    val programNames = Array(programs.size) { index ->
                        programs[index].name!!
                    }
                    picker.setColumn(programNames)
                } else {
                    viewModel.programs.observe(this, Observer { programs ->
                        val programNames = Array(programs.size) { index ->
                            programs[index].name!!
                        }
                        picker.setColumn(programNames)
                    })
                }
            }, handler = { value1, _ ->
                selectedProgramIndex = value1
                selectedProgram = viewModel.programs.value?.get(value1)
                datingProgram.text = selectedProgram?.name
            })
        }

        confirmButton.setOnClickListener {
            val result = Result(heightBar.leftPinValue.toInt(), heightBar.rightPinValue.toInt(),
                    ageBar.leftPinValue.toInt(), ageBar.rightPinValue.toInt(),
                    weightBar.leftPinValue.toInt(), weightBar.rightPinValue.toInt(),
                    getCupStart(), getCupEnd())
            onResultListener?.invoke(result)
            activity?.onBackPressed()
        }
    }

    private fun getCupStart(): String {
        return if (viewModel.getUser()?.sex == Sex.FEMALE.value) {
            ""
        } else {
            cupArray[cupBar.leftPinValue.toInt()]
        }
    }

    private fun getCupEnd(): String {
        return if (viewModel.getUser()?.sex == Sex.FEMALE.value) {
            ""
        } else {
            cupArray[cupBar.rightPinValue.toInt()]
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
            val startWeight: Int,
            val endWeight: Int,
            val startCup: String,
            val endCup: String)

}