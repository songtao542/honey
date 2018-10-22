package com.snt.phoney.ui.signup

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.snt.phoney.R
import com.snt.phoney.base.BaseDialogFragment
import com.snt.phoney.databinding.BindPhoneFragmentBinding
import com.snt.phoney.extensions.autoCleared
import com.snt.phoney.ui.main.MainActivity
import kotlinx.android.synthetic.main.fragment_bind_phone.view.*

class BindPhoneFragment : BaseDialogFragment() {

    companion object {
        fun newInstance() = BindPhoneFragment()
    }

    private lateinit var viewModel: BindPhoneViewModel

    var binding by autoCleared<BindPhoneFragmentBinding>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_bind_phone, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(BindPhoneViewModel::class.java)
        binding.viewModel = viewModel

        binding.root.bindPhone.setOnClickListener { context?.let { startActivity(MainActivity.newIntent(it)) } }
    }

}
