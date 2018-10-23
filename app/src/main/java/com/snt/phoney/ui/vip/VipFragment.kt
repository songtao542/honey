package com.snt.phoney.ui.vip

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.snt.phoney.R
import com.snt.phoney.base.BaseFragment
import com.snt.phoney.databinding.VipFragmentBinding
import com.snt.phoney.extensions.autoCleared

class VipFragment : BaseFragment() {

    companion object {
        fun newInstance() = VipFragment()
    }

    private lateinit var viewModel: VipViewModel

    var binding by autoCleared<VipFragmentBinding>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_vip, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(VipViewModel::class.java)
        binding.viewModel = viewModel
    }

}
