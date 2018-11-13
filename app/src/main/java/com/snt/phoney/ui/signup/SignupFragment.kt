package com.snt.phoney.ui.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.snt.phoney.R
import com.snt.phoney.base.BaseFragment
import com.snt.phoney.databinding.SignupFragmentBinding
import com.snt.phoney.extensions.autoCleared
import kotlinx.android.synthetic.main.fragment_signup_1.*
import kotlinx.android.synthetic.main.fragment_signup_2.*
import kotlinx.android.synthetic.main.fragment_signup_3.*

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
        binding.viewModel = viewModel
        back.setNavigationOnClickListener { activity?.finish() }
        back2.setNavigationOnClickListener { step1.bringToFront() }
        back3.setNavigationOnClickListener { step2.bringToFront() }

        male.setOnClickListener { step2.bringToFront() }
        female.setOnClickListener { step2.bringToFront() }
        confirmStep2.setOnClickListener { step3.bringToFront() }
        confirmStep3.setOnClickListener {
            context?.let {
                BindPhoneFragment.newInstance().show(childFragmentManager, "bindPhone")
//                startActivity(MainActivity.newIntent(it))
            }
        }


    }

}
