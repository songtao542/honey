package com.snt.phoney.ui.signup

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.snt.phoney.R
import com.snt.phoney.base.BaseFragment
import com.snt.phoney.databinding.SignupFragmentBinding
import com.snt.phoney.extensions.autoCleared
import com.snt.phoney.ui.main.MainActivity
import kotlinx.android.synthetic.main.fragment_signup_1.*
import kotlinx.android.synthetic.main.fragment_signup_1.view.*
import kotlinx.android.synthetic.main.fragment_signup_2.*
import kotlinx.android.synthetic.main.fragment_signup_2.view.*
import kotlinx.android.synthetic.main.fragment_signup_3.*
import kotlinx.android.synthetic.main.fragment_signup_3.view.*

class SignupFragment : BaseFragment() {

    companion object {
        fun newInstance() = SignupFragment()
    }

    private lateinit var viewModel: SignupViewModel

    var binding by autoCleared<SignupFragmentBinding>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
//        return inflater.inflate(R.layout.fragment_signup, container, false)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_signup, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(SignupViewModel::class.java)

        binding.root.back.setNavigationOnClickListener { activity?.finish() }
        binding.root.back2.setNavigationOnClickListener { step1.bringToFront() }
        binding.root.back3.setNavigationOnClickListener { step2.bringToFront() }

        binding.root.male.setOnClickListener { step2.bringToFront() }
        binding.root.female.setOnClickListener { step2.bringToFront() }
        binding.root.confirmStep2.setOnClickListener { step3.bringToFront() }
        binding.root.confirmStep3.setOnClickListener { context?.let { startActivity(MainActivity.newIntent(it)) } }


    }

}
