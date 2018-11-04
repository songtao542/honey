package com.snt.phoney.ui.dating.list

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.snt.phoney.R
import com.snt.phoney.base.BaseFragment
import com.snt.phoney.databinding.DatingListFragmentBinding
import com.snt.phoney.extensions.autoCleared
import kotlinx.android.synthetic.main.fragment_dating_list.*

class DatingListFragment : BaseFragment() {

    companion object {
        fun newInstance(arguments: Bundle? = null) = DatingListFragment().apply {
            this.arguments = arguments
        }
    }

    private lateinit var viewModel: DatingListViewModel

    var binding by autoCleared<DatingListFragmentBinding>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dating_list, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(DatingListViewModel::class.java)
        binding.viewModel = viewModel
        list?.let {
            it.layoutManager = LinearLayoutManager(context)
            it.adapter = DatingListRecyclerViewAdapter()
        }
        toolbar.setNavigationOnClickListener { activity?.finish() }
    }

}
