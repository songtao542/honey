package com.snt.phoney.ui.album

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.snt.phoney.R
import com.snt.phoney.base.BaseFragment
import com.snt.phoney.extensions.dip
import com.snt.phoney.extensions.setSoftInputMode
import com.snt.phoney.widget.itemdecoration.MonospacedItemDecoration
import kotlinx.android.synthetic.main.fragment_pay_setting.*

class PaySettingFragment : BaseFragment() {
    companion object {
        @JvmStatic
        fun newInstance(arguments: Bundle? = null) = PaySettingFragment().apply {
            this.arguments = arguments
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    lateinit var viewModel: PaySettingViewModel
    lateinit var adapter: PaySettingRecyclerViewAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pay_setting, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(PaySettingViewModel::class.java)
        toolbar.setNavigationOnClickListener { activity?.onBackPressed() }
        adapter = PaySettingRecyclerViewAdapter(this)
        list.adapter = adapter
        list.addItemDecoration(MonospacedItemDecoration(dip(8)))//VerticalDividerItemDecoration.Builder(requireContext()).size(dip(8)).build())//MonospacedItemDecoration(dip(8))
        list.layoutManager = GridLayoutManager(requireContext(), 4)


    }

}
