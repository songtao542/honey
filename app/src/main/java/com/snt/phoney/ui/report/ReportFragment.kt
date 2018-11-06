package com.snt.phoney.ui.report

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders

import com.snt.phoney.R
import com.snt.phoney.base.BaseFragment

class ReportFragment : BaseFragment() {

    companion object {
        fun newInstance(arguments: Bundle? = null) = ReportFragment().apply {
            this.arguments = arguments
        }


    }

    private lateinit var viewModel: ReportViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_report, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ReportViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
